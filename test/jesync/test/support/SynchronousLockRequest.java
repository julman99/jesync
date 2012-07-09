package jesync.test.support;

import java.util.concurrent.CountDownLatch;
import jesync.core.LockHandle;
import jesync.core.LockHandle;
import jesync.core.LockRequest;
import jesync.core.LockRequest;

/**
 * This class should not be used in production for ANY reason
 * It is only a helper for testing
 * @author julio
 */
public class SynchronousLockRequest implements LockRequest{
    public enum LockState {LOCKED,NOT_LOCKED,REQUESTING,TIMEOUT,EXPIRED}
    
    private CountDownLatch count;
    private int maxConcurrent;
    private int timeout;
    private int expiresIn;
    private LockState state=LockState.NOT_LOCKED;
    private LockHandle lockHandle=null;
    
    public SynchronousLockRequest(int maxConcurrent, int timeout,int expiresIn){
        this.count=new CountDownLatch(1);
        this.maxConcurrent=maxConcurrent;
        this.timeout=timeout;
        this.expiresIn=expiresIn;
    }
    
    public LockHandle getLockHandle(){
        return this.lockHandle;
    }
    
    public LockState getState(){
        return this.state;
    }

    @Override
    public void lockGranted(LockHandle lockHandle) {
        this.state=LockState.LOCKED;
        this.lockHandle=lockHandle;
        this.count.countDown();
    }

    @Override
    public void lockTimeout(String lock, int seconds) {
        this.state=LockState.TIMEOUT;
        this.count.countDown();
    }

    @Override
    public void lockReleased(String lock) {
        this.state=LockState.NOT_LOCKED;
        this.count.countDown();
    }

    @Override
    public void lockExpired(String lock) {
        this.state=LockState.EXPIRED;
        this.count.countDown();
    }

    @Override
    public int getMaxConcurrent() {
        return this.maxConcurrent;
    }

    @Override
    public int getTimeout() {
        return this.timeout;
    }

    @Override
    public int getExpireTimeout() {
        return this.expiresIn;
    }
    
    public LockState waitForRequest() throws InterruptedException{
        count.await();
        return this.state;
    }
    
}
