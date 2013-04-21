package com.github.julman99.jesync.net;

import java.security.InvalidParameterException;
import com.github.julman99.jesync.core.Lock;
import com.github.julman99.jesync.core.LockEngine;
import com.github.julman99.jesync.core.LockHandle;
import java.util.Map;
import org.jboss.netty.channel.*;

/**
 * Class responsible for getting the messages from the client and invoking the
 * corresponding functionality of jesync.core package.
 * There will be one ServerHandler instance per client. We can safely assume
 * that no instance of this class will be shared by two different connections.
 *
 * @author Julio Viera
 */
public final class ServerHandler extends SimpleChannelUpstreamHandler {

    private ServerLockRequestMap lockRequests;
    private LockEngine syncCore;

    public ServerHandler(LockEngine syncCore) {
        this.syncCore = syncCore;
        this.lockRequests = new ServerLockRequestMap();
    }

    @Override
    public final void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
        String msg = (String) e.getMessage();
        String[] args = msg.split(" ");
        String command = args[0];
        try{
            if (command.equals("lock")) {
                String lockKey = args[1];
                int maxConcurrent = 1;
                int timeout = -1;
                int expireInSeconds=120;
                if (args.length > 2) {
                    maxConcurrent = Integer.parseInt(args[2]);
                }
                if (args.length > 3) {
                    timeout = Integer.parseInt(args[3]);
                }
                if (args.length > 4) {
                    expireInSeconds = Integer.parseInt(args[4]);
                }
                this.lock(e.getChannel(), lockKey, maxConcurrent, timeout, expireInSeconds);
            } else if (command.equals("release")) {
                this.release(e.getChannel(), args[1]);
            } else if (command.equals("quit")) {
                this.quit(e.getChannel());
            } else if (command.equals("status")) {
                this.status(e.getChannel(), args[1]);
            } else if (command.equals("status-by-key")){
                this.statusByKey(e.getChannel());
            } else {
                e.getChannel().write("INVALID_COMMAND\n");
            }
        }catch(InvalidParameterException ex){
            e.getChannel().write("INVALID_PARAMETER: "+ex.getMessage()+"\n");
        }
    }

    @Override
    public final void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelDisconnected(ctx, e);
        this.releaseAllLocks();
    }

    final void writeResponse(Channel channel, String msg) {
        this.writeResponse(channel, msg, null,null);
    }

    final void writeResponse(Channel channel, String msg, String lockKey, LockHandle lockHandle) {
        StringBuilder response=new StringBuilder(msg);
        if (lockKey != null) {
            Lock lock = syncCore.getSyncLock(lockKey);
            response.append(" ");
            response.append(lock.getCurrentGrantedCount());
            response.append(" ");
            response.append(lock.getCurrentRequestCount());
            response.append(" ");
            response.append(lockKey);
            if(lockHandle!=null){
                response.append(" ");
                response.append(lockHandle.getSecondsRemaining());
            }
        }
        response.append("\n");
        channel.write(response.toString());
    }

    /**
     * Releases all locks granted of the current ServerHandle. Used when a
     * client disconnects
     */
    private void releaseAllLocks() {
        //Release all lock requests
        for (Map.Entry<String, ServerLockRequest> entry: this.lockRequests.entrySet()) {

            String lockKey = entry.getKey();
            ServerLockRequest request = entry.getValue();

            Lock lock = this.syncCore.getSyncLock(lockKey);
            
            //Cancel the request in case it has not been granted yet
            lock.cancelRequest(request);

            //Release the lock in case it has been granted
            LockHandle handle=request.getLockHandle();
            if(handle!=null) {
                handle.release();
            }
        }
    }

    /**
     * Returns a LockRequest for a particular channel/lock key
     *
     * @param channel Channel used to respond.
     * @param lockKey The lock key
     * @return If the LockRequest exists, it returns it, if not it creates a new
     * one
     */
    private ServerLockRequest getLockRequest(Channel channel, String lockKey) {
        ServerLockRequest res = lockRequests.get(lockKey);

        if (res == null) {
            res = new ServerLockRequest(channel, this);
            lockRequests.put(lockKey, res);
        }
        return res;
    }

    //*************** SERVER COMMANDS ******************//
    private void lock(Channel channel, String lockKey, int maxConcurrent, int timeout, int expireTimeout) {
        ServerLockRequest request = getLockRequest(channel, lockKey);
        request.setMaxConcurrent(maxConcurrent);
        request.setTimeout(timeout);
        request.setExpireTimeout(expireTimeout);

        Lock l = syncCore.getSyncLock(lockKey);
        l.requestLock(request);
    }

    private void release(Channel channel, String lockKey) {
        ServerLockRequest request = getLockRequest(channel, lockKey);
        LockHandle handle;
        if (request != null && (handle=request.getLockHandle())!=null && handle.release()) {
            lockRequests.remove(lockKey);
            this.writeResponse(channel, "RELEASED",lockKey , handle);

        } else {
            this.writeResponse(channel, "NOT_RELEASED", lockKey, null);
        }
    }

    private void status(Channel channel, String lockKey) {
        this.writeResponse(channel, "STATUS", lockKey, null);
    }
    
    private void statusByKey(Channel channel) {
        for(Lock l: this.syncCore.getByKey()){
            status(channel, l.getLockKey());
        }
    }

    private void quit(Channel channel) {
        this.releaseAllLocks();
        channel.close();
    }
}
