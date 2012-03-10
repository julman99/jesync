package jesync.core;

/**
 * Represents a lock handle.
 * A lock handle is created by the Lock object after a lock is granted. 
 * This class is used for the requester to release the lock
 * @author Julio Viera <julio.viera@gmail.com>
 */
public final class LockHandle {

    Lock lock;
    LockRequest request;

    public String getLockKey() {
        return lock.getLockKey();
    }

    LockHandle(Lock lock, LockRequest request) {
        this.lock = lock;
        this.request = request;
    }

    public final synchronized int release() {
        return lock.releaseLock(request);
    }
}
