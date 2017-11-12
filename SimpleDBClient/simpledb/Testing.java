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
      Block blk2 = new Block("filename.txt", 1);
      Block blk3 = new Block("filename.txt", 1);
      Block blk4 = new Block("filename.txt", 1);
      Block blk6 = new Block("filename.txt", 1);
      Block blk7 = new Block("filename.txt", 1);
      Block blk8 = new Block("filename.txt", 1);
      Block blk9 = new Block("filename.txt", 1);
      Block blk5 = new Block("filename.txt", 1);
      
      BufferMgr basicBufferMgr = new BufferMgr(8);
     

      try {
         basicBufferMgr.pin(blk1);
         basicBufferMgr.pin(blk2);
         basicBufferMgr.pin(blk3);
         basicBufferMgr.pin(blk4);
         basicBufferMgr.pin(blk5);
         basicBufferMgr.pin(blk6);
         basicBufferMgr.pin(blk7);
         basicBufferMgr.pin(blk8);
         basicBufferMgr.pin(blk4);
         basicBufferMgr.pin(blk2);
         basicBufferMgr.pin(blk7);
         basicBufferMgr.pin(blk1);
         basicBufferMgr.unpin(basicBufferMgr.getMapping(blk8));
         basicBufferMgr.unpin(basicBufferMgr.getMapping(blk7));
         basicBufferMgr.unpin(basicBufferMgr.getMapping(blk6));
         basicBufferMgr.unpin(basicBufferMgr.getMapping(blk5));
         basicBufferMgr.unpin(basicBufferMgr.getMapping(blk4));
         basicBufferMgr.unpin(basicBufferMgr.getMapping(blk1));
         basicBufferMgr.unpin(basicBufferMgr.getMapping(blk7));
         basicBufferMgr.unpin(basicBufferMgr.getMapping(blk4));
         basicBufferMgr.unpin(basicBufferMgr.getMapping(blk2));
         basicBufferMgr.unpin(basicBufferMgr.getMapping(blk2));
         basicBufferMgr.pin(blk9);
         
      }
      catch (BufferAbortException e) {}
      
   }

}