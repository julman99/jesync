package com.github.julman99.jesync.core;

import java.util.Timer;
import java.util.TimerTask;
import sun.jvmstat.perfdata.monitor.protocol.local.LocalEventTimer;

/**
 *
 * @author Julio Viera <julio.viera@gmail.com>
 */
public abstract class LockHandleTimeout {

    private static Timer timer;
    
    static{
        timer = new Timer();
    }
    
    public void scheduleTimeout(final LockHandle lockHandle){
        int timeout=lockHandle.getSecondsRemaining();
        if(timeout>0){
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if(lockHandle.getSecondsRemaining() == 0){
                        expire(lockHandle);
                    }
                }
            }, timeout*1000);
        }else if(timeout==0){
            expire(lockHandle);
        }
    }

    protected abstract void expire(LockHandle handle);
}
