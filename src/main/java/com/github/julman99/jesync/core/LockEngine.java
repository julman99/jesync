package com.github.julman99.jesync.core;

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
     
    
}
