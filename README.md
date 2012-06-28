#JESync 0.4 BETA

##1. Introduction
JESync is a fast portable mutex/semaphore server developed in Java. It provides an easy way to synchronize tasks among multiple processes that can be running  in different machines and need to access o perform critical tasks.

The principle behind JESync is to request a lock for a particular String. If no other connection is using the lock, the lock will be granted to the one requesting it. Otherwise, the connection is inserted in a FIFO queue and the lock will be granted when available.

JESync also supports multiple grants per lock. For example, if you need to run an algorithm, and you don't want to have more than (e.g.) 50 processes running it at the same time, you can request the locks with 50 on the max_concurrent parameter. The server will only grant the lock to 50 connections at the same time

##2. Current project status
JESync currently is on BETA status. Since it is still a BETA, it should not be used on any production enviroment, however it is stable enough for you to use it for development.

The current release roadmap is the following:

###Mid July 2012:    
- Add stats command to be able to print out how many request/sec, how many locks are issued and how much memory is being used
- Release version 1.0


##3. Backlog
- Add Unit Testing suite
- Improve protocol parsing engine, detect errors and provide feedback
- Develop the Java JESync Client driver
- Test on several Linux, Mac and Windows distributions
- Test and profile on high concurrency environments.
- Develop drivers for other languages than PHP and Java

1.0 Release date is expected to be somewhere in mid-july or august 2012.


##4. Dependencies
- [Netty](http://netty.io/) (included in lib/ directory)

##5. Drivers
- [PHP](https://github.com/julman99/JESync-php)
