package com.github.julman99.jesync.core;

import com.github.julman99.test.support.SynchronousLockRequest;
import java.util.Random;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author julio
 */
public class LockTest {
    LockEngine lockEngine;
    
    public LockTest() {
        this.lockEngine=new LockEngine();
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of requestLock method, of class Lock.
     */
    @Test
    public void testRequestLock() throws InterruptedException {
        String lockKey="TEST-A";
        Lock instance = this.lockEngine.getSyncLock(lockKey);
        
        //Test single lock
        SynchronousLockRequest request = new SynchronousLockRequest(1, 1, 10);
        instance.requestLock(request);
        SynchronousLockRequest.LockState result = request.waitForRequest();
        assertEquals(result, SynchronousLockRequest.LockState.LOCKED);
        
        SynchronousLockRequest request2 = new SynchronousLockRequest(1, 0, 10);
        instance.requestLock(request2);
        result = request2.waitForRequest();
        assertEquals(result, SynchronousLockRequest.LockState.TIMEOUT);
        
        request.getLockHandle().release(); //housekeeping
    }

    /**
     * Test of getLockKey method, of class Lock.
     */
    @Test
    public void testGetLockKey() {
        Lock instance = this.lockEngine.getSyncLock("TEST-A");
        String expResult = "TEST-A";
        String result = instance.getLockKey();  
        assertEquals(expResult, result);
    }

    /**
     * Test of getCurrentGrantedCount method, of class Lock.
     */
    @Test
    public void testGetCurrentGrantedCount() throws InterruptedException {
        String lockKey="TEST-A";
        Lock instance = this.lockEngine.getSyncLock(lockKey);
        int concurrent=new Random().nextInt(10)+3;
        SynchronousLockRequest request[]=new SynchronousLockRequest[concurrent];
        
        for(int i=0;i<concurrent-1;i++){
            request[i]=new SynchronousLockRequest(concurrent, 0, 90);
            instance.requestLock(request[i]);
            SynchronousLockRequest.LockState result = request[i].waitForRequest();
            assertEquals(SynchronousLockRequest.LockState.LOCKED, result);
            assertEquals(i+1,instance.getCurrentGrantedCount());
        }
        
       assertEquals(instance.getCurrentGrantedCount(), concurrent-1); 
       
       for(int i=concurrent-2;i>=0;i--){
           request[i].getLockHandle().release();
           assertEquals(instance.getCurrentGrantedCount(), i);
       }
    }

    /**
     * Test of getCurrentRequestCount method, of class Lock.
     */
    @Test
    public void testGetCurrentRequestCount() throws InterruptedException {
        String lockKey="TEST-A";
        Lock instance = this.lockEngine.getSyncLock(lockKey);
        int size=new Random().nextInt(10)+3;
        SynchronousLockRequest request[]=new SynchronousLockRequest[size];
        
        for(int i=0;i<size;i++){
            request[i]=new SynchronousLockRequest(1, 900, 900);
            instance.requestLock(request[i]);
            if(i==0){
                SynchronousLockRequest.LockState result = request[i].waitForRequest();
                assertEquals(SynchronousLockRequest.LockState.LOCKED, result);
            }else{
                assertEquals(i,instance.getCurrentRequestCount());
            }
            
            
        }
        
        assertEquals(instance.getCurrentRequestCount(), size-1); 
        int requestSize=size-1;
        for(int i=0;i<size;i++){
            request[i].getLockHandle().release();
            requestSize--;
            assertEquals(instance.getCurrentRequestCount(), Math.max(requestSize,0));
        }
    }

    /**
     * Test of cancelRequest method, of class Lock.
     */
    @Test
    public void testCancelRequest() throws InterruptedException {
        String lockKey="TEST-A";
        Lock instance = this.lockEngine.getSyncLock(lockKey);
        
        //Test single lock
        SynchronousLockRequest request = new SynchronousLockRequest(1, 1, 100);
        instance.requestLock(request);
        SynchronousLockRequest.LockState result = request.waitForRequest();
        assertEquals(result, SynchronousLockRequest.LockState.LOCKED);
        
        //Now issue and cancel a request
        SynchronousLockRequest requestToCancel = new SynchronousLockRequest(1, 100, 100);
        instance.requestLock(requestToCancel);
        assertEquals(instance.getCurrentRequestCount(), 1);
        instance.cancelRequest(requestToCancel);
        assertEquals(instance.getCurrentRequestCount(), 0);
        
    }
    
    /**
     * Test the lock request timeout when trying to acquire a lock.
     */
    @Test
    public void testRequestTimeout() throws InterruptedException {
        int expiresIn = 1;
        String lockKey="TEST-A";
        Lock instance = this.lockEngine.getSyncLock(lockKey);
        
        //Test single lock
        SynchronousLockRequest request = new SynchronousLockRequest(1, 1, 100);
        instance.requestLock(request);
        SynchronousLockRequest.LockState result = request.waitForRequest();
        assertEquals(result, SynchronousLockRequest.LockState.LOCKED);
        
        //Now issue a lock that will timeout in 1 second
        SynchronousLockRequest requestToCancel = new SynchronousLockRequest(1, expiresIn, 100);
        instance.requestLock(requestToCancel);
        assertEquals(instance.getCurrentRequestCount(), 1);
        Thread.sleep(expiresIn*1000+100);
        assertEquals(instance.getCurrentRequestCount(), 0);
        
    }

    /**
     * Test of releaseLock method, of class Lock.
     */
    @Test
    public void testReleaseLock() throws InterruptedException {
        String lockKey="TEST-A";
        Lock instance = this.lockEngine.getSyncLock(lockKey);
        
        //Test single lock
        SynchronousLockRequest request = new SynchronousLockRequest(1, 1, 100);
        instance.requestLock(request);
        SynchronousLockRequest.LockState result = request.waitForRequest();
        assertEquals(result, SynchronousLockRequest.LockState.LOCKED);
        assertEquals(instance.getCurrentGrantedCount(), 1);
        request.getLockHandle().release();
        assertEquals(instance.getCurrentGrantedCount(), 0);
        
    }

    /**
     * Test of expireLock method, of class Lock.
     */
    @Test
    public void testExpireLock() throws InterruptedException {
        String lockKey="TEST-EXPIRE";
        Lock instance = this.lockEngine.getSyncLock(lockKey);
        int expiresIn=0;
        
        
        while(expiresIn<=1){
            //Test single lock
            SynchronousLockRequest request = new SynchronousLockRequest(1, 1, expiresIn);
            instance.requestLock(request);
            SynchronousLockRequest.LockState result = request.waitForRequest();
            if(expiresIn>0)
                assertEquals(SynchronousLockRequest.LockState.LOCKED,result);

            Thread.sleep(expiresIn*1000+100);
            assertEquals(SynchronousLockRequest.LockState.EXPIRED, request.getState());
            
            expiresIn+=1;
        }
        
    }
    
}
