package com.github.julman99.jesync.core;

import com.github.julman99.jesync.core.LockHandle;
import com.github.julman99.jesync.core.LockRequest;

/**
 *
 * @author Julio Viera <julio.viera@gmail.com>
 */
public abstract class AbstractLockRequest implements LockRequest {
    
    private LockHandle lockHandle;
    private int maxConcurrent=1;
    private int timeout=-1;
    private int expireTimeout=120;

    public AbstractLockRequest() {

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
        this.onLockGranted(lockHandle);
    }

    @Override
    public void lockTimeout(String lock, int seconds) {
        this.onLockTimeout(lock, seconds);
    }
    
     @Override
    public void lockReleased(String lock) {
        this.onLockReleased(lock);
    }

    @Override
    public void lockExpired(String lock) {
        this.onLockExpired(lock);
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
    
    protected abstract void onLockGranted(LockHandle lockHandle);
    protected abstract void onLockTimeout(String lock, int seconds);
    protected abstract void onLockReleased(String lock);
    protected abstract void onLockExpired(String lock);
}
