package simpledb;

import java.util.Scanner;

import simpledb.buffer.BufferAbortException;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.server.SimpleDB;

public class Testing {
   public static void main(String args[])
   {
      
      SimpleDB.init("simpleDB");
      
      Block blk1 = new Block("filename.txt", 1);
      Block blk2 = new Block("filename.txt", 2);
      Block blk3 = new Block("filename.txt", 3);
      Block blk4 = new Block("filename.txt", 4);
      Block blk5 = new Block("filename.txt", 5);
      Block blk6 = new Block("filename.txt", 6);
      Block blk7 = new Block("filename.txt", 7);
      Block blk8 = new Block("filename.txt", 8);
      Block blk9 = new Block("filename.txt", 9);
      //Block blk2 = new Block("filename.txt", 2);
      
      BufferMgr basicBufferMgr = SimpleDB.bufferMgr();
      basicBufferMgr.pin(blk1);
      SimpleDB.logMgr().printLogPageBuffer();
      try {
      basicBufferMgr.pin(blk2);
      basicBufferMgr.pin(blk3);
      basicBufferMgr.pin(blk4);
      basicBufferMgr.pin(blk5);
      basicBufferMgr.pin(blk6);
      basicBufferMgr.pin(blk7);
      basicBufferMgr.pin(blk8);
      System.out.println(basicBufferMgr.available());
      }
      catch (BufferAbortException e) {}
      basicBufferMgr.pin(blk9);
      SimpleDB.logMgr().printLogPageBuffer();
   }
   
   Scanner sc = new Scanner(System.in);
   int age = sc.nextInt();

}