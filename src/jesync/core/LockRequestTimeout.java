/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jesync.core;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Julio Viera <julio.viera@gmail.com>
 */
public class LockRequestTimeout extends TimerTask {
    private static Timer timer;
    
    private Lock lock;
    private LockRequest request;
    
    
    public LockRequestTimeout(Lock lock,LockRequest request){
        this.lock=lock;
        this.request=request;
    }
    
    public final static void scheduleTimeout(Lock lock,final LockRequest request){
        int timeout=request.getTimeout();
        if(timeout>0){
            if(timer==null)
                timer=new Timer();
            timer.schedule(new LockRequestTimeout(lock,request), timeout*1000);
        }else if(timeout==0){
            new LockRequestTimeout(lock,request).run();
        }
    }

    @Override
    public final void run() {
        if(this.lock.cancelRequest(this.request))
            this.request.lockTimeout(lock.getLockKey(), request.getTimeout());
    }
    
    
}
