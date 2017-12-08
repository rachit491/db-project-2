# CSC540: Project 2
## Systems​ ​Project​ ​-​ ​Buffer​ ​&​ ​Log​ ​Management
## Team E

### Unity IDs:
Rishabh Sinha (rsinha2)
Rachit Shrivastava (rshriva)
Kshitij Patil (kspatil2)
Sahil Dorwat (ssdorwat)
Kunal Kulkarni (krkulkar)

### List of files changed
1. SimpleDB/simpledb/buffer/BasicBufferMgr.java
2. SimpleDB/simpledb/buffer/BufferMgr.java
3. SimpleDB/simpledb/buffer/Buffer.java
4. SimpleDB/simpledb/log/LogMgr.java

### Note on how to test for a scenario in the description
1. We used SimpleDBClient and created a file called Testing.java to test.
2. Steps to run code:
  a. Goto SimpleDB in terminal and then type  java simpledb.server.Startup simpleDB
  b. Once the server is ready.
  c. Open Eclipse Project for SimpleDBClient, paste the provided Testing.java file from zip folder.
  d. You need to have 9 txt files named as filename{}.txt ({} - number from 1 to 9), along with the Testing.java
  e. Right click on Testing.java, and run as Application to see final result.
3. Please refer to Testing.java for the test cases.
4. Check BufferPoolContent
5. Pin1, pin2, pin3, pin4, pin5, pin6, unipn6, pin7, unpin7, pin8, unpin8, pin9
6. Check the logBuffer by calling SimpleDB.logMgr().printLogPageBuffer(); -> this is a function we made to check logs
