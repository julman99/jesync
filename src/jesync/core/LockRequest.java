package jesync.core;

/**
 * Represent a request to a lock.
 * The Lock class will call the methods in this interface to notify that:
 * 1. A lock has been granted
 * or
 * 2. A timeout has been reached
 * @author Julio Viera <julio.viera@gmail.com>
 */
public interface LockRequest {
    /**
     * Called by the Lock class when a lock has been granted
     * @param lock Handle used to release the lock
     * the one represented by the handle
     */
    void lockGranted(LockHandle lockHandle);
    
    /**
     * Called by the Lock class when a timeout has been reached
     * @param lock The lock key that was tried to lock
     * @param seconds The number of seconds the timeout took
     */
    void lockTimeout(String lock,int seconds);
    
    /**
     * Called by the Lock class when a granted lock is released for this request
     * @param lock
     * @param seconds 
     */
    void lockReleased(String lock);
    
    /**
     * Called by the Lock class when a granted lock expires for this request
     * @param lock
     * @param seconds 
     */
    void lockExpired(String lock);
    
    /**
     * Called by the Lock class to determine if a lock can be granted to this or another request.
     * This value is used for two conditions:
     * 1. If the current request does not have the lock, then this value is compared
     * against the current number of request using the lock to determine if the lock
     * can be granted
     * 
     * 2. If the current request has the lock, then this value is used to prevent
     * other request with higher concurrency values to enter the lock.
     * @return Maximum number of request that can be using the requested lock
     * at the same time with the current request
     */
    int getMaxConcurrent();
    
    
    /**
     * Called by the Lock class to know whats the max number of seconds this
     * request is willing to wait for the lock to be granted.
     * Note: -1 means unlimited
     */
    int getTimeout();
    
    /**
     * Called by the Lock class to know how many seconds the lock should be granted
     * to this particular request
     */
    int getExpireTimeout();
}
