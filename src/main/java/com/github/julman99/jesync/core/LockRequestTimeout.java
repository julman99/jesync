package com.github.julman99.jesync.core;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Julio Viera <julio.viera@gmail.com>
 */
public abstract class LockRequestTimeout {
    private static Timer timer;
    
    static {
        timer = new Timer();
    }
    
    public void scheduleTimeout(final LockRequest request){
        int timeout=request.getTimeout();
        if(timeout>0){
            
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    requestTimedOut(request);
                }
            }, timeout * 1000);
        }else if(timeout ==0){
            requestTimedOut(request);
        }
    }

    protected abstract void requestTimedOut(LockRequest request);
    
//    private void cancelRequest(LockRequest request) {
//    }
    
    
}
