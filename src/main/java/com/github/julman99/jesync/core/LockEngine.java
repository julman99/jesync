package com.github.julman99.jesync.core;

import com.google.common.collect.MapMaker;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 * Engine that generates Lock objects for a desired key
 * @author Julio Viera <julio.viera@gmail.com>
 */
public final class LockEngine {
    
    private final Map<String, Lock> table;
    
    public LockEngine(){
        this.table = new MapMaker().weakValues().makeMap();
    }
    
    public final Lock getSyncLock(String key){
        synchronized(this.table){
            Lock res=this.table.get(key);
            if(res==null){
                res=new Lock(key);
                this.table.put(key,res);
            }
            return res;
        }
    }
     
    public final Iterable<Lock> getByKey(){
        Collection<Lock> res ;
        synchronized(this.table){
            res = new ArrayList(table.values());
        }
        return Collections.unmodifiableCollection(res);
    }
    
    public final Iterable<Lock> getByRequests(){
        ArrayList<Lock> res;
        synchronized(table){
            res = new ArrayList<Lock>(table.values());
        }
        
        Collections.sort(res, new Comparator<Lock>() {

            @Override
            public int compare(Lock t, Lock t1) {
                return -(t.getCurrentRequestCount() - t1.getCurrentRequestCount());
            }
        });
        
        return Collections.unmodifiableCollection(res);
    }
    
    public final Iterable<Lock> getByGranted(){
        ArrayList<Lock> res;
        synchronized(table){
            res = new ArrayList<Lock>(table.values());
        }
        
        Collections.sort(res, new Comparator<Lock>() {

            @Override
            public int compare(Lock t, Lock t1) {
                return -(t.getCurrentGrantedCount() - t1.getCurrentGrantedCount());
            }
        });
        
        return Collections.unmodifiableCollection(res);
    }
    
}
