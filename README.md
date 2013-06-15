#JESync 0.8.1 RC
Download [here](http://goo.gl/u784D)

##1. Introduction
JESync is a fast portable mutex/semaphore server developed in Java. It provides an easy way to synchronize tasks among multiple processes that can be running  in different machines and need to access o perform critical tasks.

The principle behind JESync is to request a lock for a particular String. If no other connection is using the lock, the lock will be granted to the one requesting it. Otherwise, the connection is inserted in a FIFO queue and the lock will be granted when available.

JESync also supports multiple grants per lock. For example, if you need to run an algorithm, and you don't want to have more than (e.g.) 50 processes running it at the same time, you can request the locks with 50 on the max_concurrent parameter. The server will only grant the lock to 50 connections at the same time

##2. Current project status
JESync currently is on Release Candidate status. It has been used inside [Pixable](http://www.pixable.com) for almost a year. After the upgrade to v0.5 it has been running without any issues for several months.

There are more unit tests and load test need to be done until I consider this to be a 1.0 version, however, it does not crash and there are no known bugs in functionallity.

Feel free to use it and improve it!

##3. Backlog
- Continue developing the JUnit tests
- Improve protocol parsing engine, detect errors and provide feedback
- Develop the Java JESync Client driver
- Test on Windows servers (and create executable launchers for windows)
- Develop drivers for other languages than PHP and Java

##4. Drivers
- [PHP](https://github.com/julman99/JESync-php)
- [Java](https://github.com/julman99/jesync-java) (Work in progress)
