package simpledb;

import java.util.Map;
import java.util.Scanner;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferAbortException;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.server.SimpleDB;

public class Testing {
   public static void main(String args[])
   {
      
      SimpleDB.init("simpleDB");
      
      Block blk1 = new Block("filename1.txt", 1);
      Block blk2 = new Block("filename2.txt", 2);
      Block blk3 = new Block("filename3.txt", 3);
      Block blk4 = new Block("filename4.txt", 4);
      Block blk5 = new Block("filename5.txt", 5);
      Block blk6 = new Block("filename6.txt", 6);
      Block blk7 = new Block("filename7.txt", 7);
      Block blk8 = new Block("filename8.txt", 8);
      Block blk9 = new Block("filename9.txt", 9);
      //Block blk2 = new Block("filename.txt", 2);
      
      BufferMgr basicBufferMgr = SimpleDB.bufferMgr();
      basicBufferMgr.pin(blk1);
      SimpleDB.logMgr().printLogPageBuffer();
      
     
      try {
      basicBufferMgr.pin(blk2);
      System.out.println("##### ADD BLK 2 #####");
      Map<Block, Buffer> bufferPoolMap = basicBufferMgr.getPool(); 
//      for(Map.Entry<Block, Buffer> index : bufferPoolMap.entrySet())
//      {
//         System.out.println(index.getKey());
//         System.out.println(index.getValue().contents.getString(0));
//      }
      
      basicBufferMgr.pin(blk3);
      System.out.println("##### ADD BLK 3 #####");
      bufferPoolMap = basicBufferMgr.getPool();
      System.out.println("LOGMGR PAGE : ");
      System.out.println(SimpleDB.logMgr().mypage.getString(0));
      for(Map.Entry<Block, Buffer> index : bufferPoolMap.entrySet())
      {
         System.out.println(index.getKey());
         System.out.println(index.getValue().contents.getString(0));
      }
      
      
      
//      basicBufferMgr.pin(blk4);
//      System.out.println("##### ADD BLK 4 #####");
//      basicBufferMgr.pin(blk5);
//      System.out.println("##### ADD BLK 5 #####");
//      basicBufferMgr.pin(blk6);
//      System.out.println("##### ADD BLK 6 #####");
//      basicBufferMgr.pin(blk7);
//      System.out.println("##### ADD BLK 7 #####");
//      basicBufferMgr.pin(blk8);
//      System.out.println("##### ADD BLK 8 #####");
//      System.out.println(basicBufferMgr.available());

         
      }
      catch (BufferAbortException e) {}
//      basicBufferMgr.unpin(basicBufferMgr.getMapping(blk6));
//      System.out.println("##### UNPIN BLK 6 #####");
//      SimpleDB.logMgr().printLogPageBuffer();
//      basicBufferMgr.pin(blk7);
//      System.out.println("##### ADD BLK 6 #####");
//      basicBufferMgr.pin(blk9);
//      System.out.println("##### ADD BLK 9 #####");
      
      //System.out.println(bufferPoolMap.toString());
      
   }
   
   Scanner sc = new Scanner(System.in);
   int age = sc.nextInt();

}