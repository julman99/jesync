package jesync.core;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Julio Viera <julio.viera@gmail.com>
 */
public class LockHandleTimeout extends TimerTask {
    private static Timer timer;
    
    private LockHandle handle;
    
    public LockHandleTimeout(LockHandle lockHandle){
        this.handle=lockHandle;
    }
    
    public static void scheduleTimeout(LockHandle lockHandle){
        int timeout=lockHandle.getSecondsRemaining();
        if(timeout>0){
            if(timer==null)
                timer=new Timer();
            timer.schedule(new LockHandleTimeout(lockHandle), timeout*1000);
        }else if(timeout==0){
            new LockHandleTimeout(lockHandle).run();
        }
    }

    @Override
    public final void run() {
        if(handle.getSecondsRemaining()==0)
            handle.expire();
    }
    
    
}
