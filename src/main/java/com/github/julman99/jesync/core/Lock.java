package com.github.julman99.jesync.core;

import java.security.InvalidParameterException;

/**
 * Defines a lock and all the logic associated with granting and releasing them
 *
 * @author Julio Viera <julio.viera@gmail.com>
 */
public class Lock {

    private String lockKey;
    private LockRequestList lockRequests = new LockRequestList();
    private LockRequestMap locksGranted = new LockRequestMap();
    private int lockedMaxConcurrent = Integer.MAX_VALUE;
    
    public String getLockKey() {
        return lockKey;
    }

    /**
     * @return How many request are currently using the lock
     */
    public int getCurrentGrantedCount(){
        return locksGranted.size();
    }
    
    /**
     * @return How many requests are currently waiting to get the lock
     */
    public int getCurrentRequestCount(){
        return lockRequests.size();
    }
    
    Lock(String lockKey) {
        this.lockKey = lockKey;
        this.lockRequests = new LockRequestList();
    }

    /**
     * Process the lock request
     *
     * @param request
     */
    public final synchronized void requestLock(LockRequest request) {
        if(request.getMaxConcurrent()<=0)
            throw new InvalidParameterException("Max concurrent should be greater or equal than 1");
        if(request.getTimeout()<-1)
            throw new InvalidParameterException("Timeout should be greater or equal than -1");
        if(request.getExpireTimeout()<0)
            throw new InvalidParameterException("Expire timeout should be greater or equal than 0");
                
        if (this.locksGranted.containsKey(request)) {
            //this lock is already granted for this request, lets re-grant it
            this.grantLock(request);
        } else if(!this.lockRequests.contains(request)) {
            this.lockRequests.add(request);
            this.processRequestList();
            LockRequestTimeout.scheduleTimeout(this,request);
        }

    }
    
    
    public final synchronized boolean cancelRequest(LockRequest request) {
        return this.lockRequests.remove(request);
    }

    /**
     * This function loops trough all the requests and grants the lock checking
     * that the request entering the lock accepts the number of concurrent
     * locks. Also it will respect the max number of concurrent locks for a
     * particular lock.
     */
    private void processRequestList() {
        int newSize = locksGranted.size() + 1;

        for (int i = 0; i < this.lockRequests.size(); i++) {
            LockRequest req = this.lockRequests.get(i);
            if (req.getMaxConcurrent() >= newSize && newSize <= this.lockedMaxConcurrent) {
                this.grantLock(req);
                newSize++;
                i--;
                continue;
            }
        }

    }

    /**
     * Grants a lock. This function assumes that all the conditions to grant a
     * lock are met. It shifts the request from the request list to the granted
     * list. Also the callback "lockGranted" is done from within this function.
     *
     * @param request
     */
    private void grantLock(LockRequest request) {
        LockHandle handle = this.locksGranted.get(request);
        if (handle == null) { //We have never granted this lock to this request.
            //Recalculate the lockedMaxConcurrent to respect the maxConcurrent
            //values for the already locked requests
            if (this.lockedMaxConcurrent > request.getMaxConcurrent()) {
                this.lockedMaxConcurrent = request.getMaxConcurrent();
            }
            
            this.lockRequests.remove(request);

            //Create the handle that will be used by the request to release the lock
            handle = new LockHandle(this, request);
            this.locksGranted.put(request,handle);
            
        }else{
            handle.setExpiresIn(request.getExpireTimeout());
        }
        //Schedule the expiration timeout
        LockHandleTimeout.scheduleTimeout(handle);
        
        //Callback
        if(this.locksGranted.get(request)!=null) //only call the callback if the request stills granted
            request.lockGranted(handle);
    }

    /**
     * Releases the lock. This function removes the request from the granted
     * list. It also checks if we need to recalculate lockedMaxConcurrent, in
     * case we do it triggers the recalculation
     *
     * @param request
     * @return True if the request was using the lock, otherwise false.
     */
    final synchronized boolean releaseLock(LockRequest request) {
        if (this.locksGranted.containsKey(request)) {
            this.locksGranted.remove(request);
            if (request.getMaxConcurrent() == this.lockedMaxConcurrent) {
                this.buildLockedMaxConcurrent();
            }
            this.processRequestList();
            return true;
        } else {
            return false;
        }
    }

    final synchronized boolean expireLock(LockRequest request) {
        boolean res = this.releaseLock(request);
        if (res) {
            request.lockExpired(this.lockKey);
        }
        return res;
    }

    /**
     * Builds the lockedMaxConcurrent value. Loops through the locksGranted list
     * and saves the minimum maxConcurrent among all the requests
     */
    private void buildLockedMaxConcurrent() {
        this.lockedMaxConcurrent = Integer.MAX_VALUE;
        
        for (LockRequest req : locksGranted.keySet()) {
            if (req.getMaxConcurrent() < this.lockedMaxConcurrent) {
                this.lockedMaxConcurrent = req.getMaxConcurrent();
            }
        }

    }
}
