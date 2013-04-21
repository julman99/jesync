###JESync 0.8 RC

- Adds status-by-key command

###JESync 0.7 RC

- bin/jesync was incorrectly defined as a sh script instead of a bash script

###JESync 0.6 RC

- Converted to a maven project
- Created shell script to launch the server without manually invoking java

###JESync 0.5 BETA

- Fixed: When requesting a lock with expires_in set to zero, it was first expired and then immediately granted

###JESync 0.4 BETA

- Added the "expire" parameter on the lock command
