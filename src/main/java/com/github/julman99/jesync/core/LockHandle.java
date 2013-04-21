package com.github.julman99.jesync.core;

import java.util.Date;

/**
 * Represents a lock handle.
 * A lock handle is created by the Lock object after a lock is granted. 
 * This class is used for the requester to release the lock
 * @author Julio Viera <julio.viera@gmail.com>
 */
public abstract class LockHandle {

    private Lock lock;
    private LockRequest request;
    private int dateExpires;

    protected final LockRequest getLockRequest(){
        return this.request;
    }
    
    public final String getLockKey() {
        return lock.getLockKey();
    }

    public final Lock getLock() {
        return lock;
    }
    
    public final int getSecondsRemaining(){
        int res=this.dateExpires - ((int)new Date().getTime()/1000);
        return res>=0?res:0;
    }

    LockHandle(Lock lock, LockRequest request) {
        this.lock = lock;
        this.request = request;
        this.setExpiresIn(request.getExpireTimeout());
    }

    public abstract boolean release();
    
    final synchronized void setExpiresIn(int seconds){
        this.dateExpires=((int)new Date().getTime()/1000)+request.getExpireTimeout();
    }
}
