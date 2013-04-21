package com.github.julman99.jesync.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Engine that generates Lock objects for a desired key
 * @author Julio Viera <julio.viera@gmail.com>
 */
public final class LockEngine {
    
    private LocksMap table;
    
    public LockEngine(){
        this.table=new LocksMap();
    }
    
    public final synchronized Lock getSyncLock(String key){
        Lock res=this.table.get(key);
        if(res==null){
            res=new Lock(key);
            this.table.put(key,res);
        }
        return res;
    }
     
    public final synchronized Iterable<Lock> getByKey(){
        return table.values();
    }
    
    public final synchronized Iterable<Lock> getByRequests(){
        ArrayList<Lock> res = new ArrayList<Lock>(table.values());
        
        Collections.sort(res, new Comparator<Lock>() {

            @Override
            public int compare(Lock t, Lock t1) {
                return -(t.getCurrentRequestCount() - t1.getCurrentRequestCount());
            }
        });
        
        return Collections.unmodifiableCollection(res);
    }
    
    public final synchronized Iterable<Lock> getByGranted(){
        ArrayList<Lock> res = new ArrayList<Lock>(table.values());
        
        Collections.sort(res, new Comparator<Lock>() {

            @Override
            public int compare(Lock t, Lock t1) {
                return -(t.getCurrentGrantedCount() - t1.getCurrentGrantedCount());
            }
        });
        
        return Collections.unmodifiableCollection(res);
    }
    
}
