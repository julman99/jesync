package jesync.net;

import jesync.core.Lock;
import jesync.core.LockHandle;
import jesync.core.LockRequest;
import org.jboss.netty.channel.Channel;

/**
 *
 * @author Julio Viera <julio.viera@gmail.com>
 */
public class ServerLockRequest implements LockRequest {
    private Channel clientChannel;
    private LockHandle lockHandle;
    private int maxConcurrent=1;
    private int timeout=-1;
    private int expireTimeout=120;
    private ServerHandler serverHandler;

    public ServerLockRequest(Channel clientChannel,ServerHandler serverHandler) {
        this.clientChannel = clientChannel;
        this.serverHandler=serverHandler;
    }

    public LockHandle getLockHandle() {
        return lockHandle;
    }

    public void setLockHandle(LockHandle lockHandle) {
        this.lockHandle = lockHandle;
    }
    
    @Override
    public int getMaxConcurrent() {
        return this.maxConcurrent;
    }
    
    public void setMaxConcurrent(int max){
        this.maxConcurrent=max;
    }

    @Override
    public void lockGranted(LockHandle lockHandle) {
        this.lockHandle=lockHandle;
        this.serverHandler.writeResponse(this.clientChannel, "GRANTED", lockHandle.getLockKey(), lockHandle);
    }

    @Override
    public void lockTimeout(String lock, int seconds) {
        this.serverHandler.writeResponse(this.clientChannel, "TIMEOUT", lock, null);
    }
    
     @Override
    public void lockReleased(String lock) {
        //do nothing
    }

    @Override
    public void lockExpired(String lock) {
        this.serverHandler.writeResponse(this.clientChannel, "EXPIRED", lock, null);
    }

    @Override
    public int getTimeout() {
        return this.timeout;
    }
    
    @Override
    public int getExpireTimeout() {
        return expireTimeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    
    public void setExpireTimeout(int timeout){
        this.expireTimeout=timeout;
    }

}
