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
    private ServerHandler serverHandler;

    public ServerLockRequest(Channel clientChannel,ServerHandler serverHandler) {
        this.clientChannel = clientChannel;
        this.serverHandler=serverHandler;
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
        this.serverHandler.writeResponse(this.clientChannel, "GRANTED", lockHandle.getLockKey());
    }

    @Override
    public void lockTimeout(String lock, int seconds) {
        this.serverHandler.writeResponse(this.clientChannel, "TIMEOUT", lock);
    }

    @Override
    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    
    public boolean release(){
        if(this.lockHandle!=null){
            return this.lockHandle.release();
        }else
            return false;
    }
    
}
