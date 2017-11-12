package simpledb;

import simpledb.buffer.BufferAbortException;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.server.SimpleDB;

public class Testing {
   public static void main(String args[])
   {
      
      SimpleDB.init("simpleDB");
      
      Block blk1 = new Block("filename.txt", 1);
      
      BufferMgr basicBufferMgr = new SimpleDB().bufferMgr();
      basicBufferMgr.pin(blk1);

      try {
      basicBufferMgr.pin(blk1);
      }
      catch (BufferAbortException e) {}
      
   }

}