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

    public ServerLockRequest(Channel clientChannel) {
        this.clientChannel = clientChannel;
    }

    @Override
    public int getMaxConcurrent() {
        return this.maxConcurrent;
    }
    
    public void setMaxConcurrent(int max){
        this.maxConcurrent=max;
    }

    @Override
    public void lockGranted(LockHandle lock, int concurrent) {
        this.lockHandle=lock;
        this.clientChannel.write("GRANTED "+concurrent+" "+lock.getLockKey()+"\n");
    }

    @Override
    public void lockTimeout(String lock, int seconds) {
        this.clientChannel.write("TIMEOUT "+seconds+" "+lock+"\n");
    }
    
    public int release(){
        if(this.lockHandle!=null){
            return this.lockHandle.release();
        }else
            return 0;
    }
    
}
