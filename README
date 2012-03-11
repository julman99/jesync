JESync Alpha 0.1 
Author: Julio Viera


1. Introduction
----------------------------
JESync is a fast portable mutex/semaphore server developed in Java. It provides
an easy way to syncronize task among multiple processes that can be running 
in different machines and need to access o perform critical tasks.

The principle behind JESync is to request a lock for a particular String. If
no other connection is using the lock, the lock will be granted to the one
requesting it. Otherwize, the connection is inserted in a FIFO queue and the
lock will be granted when available.

JESync also supports multiple grants per lock. For example, if you need to run
an algorithm, and you dont want to have more than 50 processes running it at 
the same time, you can request the locks with 50 on the max_concurrent 
parameter. The server will only grant the lock to 50 connections at the same
time

2. Current project status
----------------------------
JESync currently is on Alpha status and not used in any production enviroment.

The current release roadmap is the following:

Late March 2012:    - Implement the PHP driver
                    - Put JESync on production replacing a memcache spinlock 
                      algorithm wich currently handles more than 100000
                      request per minute

Mid April 2012:     - Release version 1.0


3. Backlog
----------------------------
- Implement PHP driver
- Add command line options parser and feedback
- Add Unit Testing suite
- Improve protocol parsing engine, detect errors and provide feedback
- Develop the Java JESync Client driver
- Test on several Linux, Mac and Windows distributions
- Test and profile on high concurrency environments.
- Develop drivers for other languages than PHP and Java

1.0 Release date is expected to be somewhere in mid-april.


4. Dependencies
- http://netty.io/