/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.julman99.jesync.core;

import com.github.julman99.test.support.SynchronousLockRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author julio
 */
public class LockEngineTest {
    
    public LockEngineTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getSyncLock method, of class LockEngine.
     */
    @Test
    public void testGetSyncLock() throws InterruptedException {
        
        LockEngine engine = new LockEngine();
        Lock a = engine.getSyncLock("a");
        Lock a2 = engine.getSyncLock("a");
        
        SynchronousLockRequest r = new SynchronousLockRequest(1, 0, 1);
        a.requestLock(r);
        r.waitForRequest();
        r.getLockHandle().release();
        
        //Tests the cache is used, both a and a2 should be the same object
        assertEquals(a.hashCode(), a2.hashCode());
        
        //Test GC is working
        int hashCode = a.hashCode();
        a = null;
        a2 = null;
        r = null;
        
        Thread.sleep(1000 + 100);
        System.gc();
        
        //The Lock instance should not be the same as before
        Lock a3 = engine.getSyncLock("a");
        
        assertFalse(hashCode == a3.hashCode());
    }

    /**
     * Test of getByKey method, of class LockEngine.
     */
    @Test
    public void testGetByKey() {
    }

    /**
     * Test of getByRequests method, of class LockEngine.
     */
    @Test
    public void testGetByRequests() {
    }

    /**
     * Test of getByGranted method, of class LockEngine.
     */
    @Test
    public void testGetByGranted() {
    }
}