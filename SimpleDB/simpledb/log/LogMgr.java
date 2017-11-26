package simpledb.log;

import simpledb.server.SimpleDB;
import simpledb.buffer.Buffer;
import simpledb.file.*;
import static simpledb.file.Page.*;

import java.util.*;

/**
 * The low-level log manager.
 * This log manager is responsible for writing log records
 * into a log file.
 * A log record can be any sequence of integer and string values.
 * The log manager does not understand the meaning of these
 * values, which are written and read by the
 * {@link simpledb.tx.recovery.RecoveryMgr recovery manager}.
 * @author Edward Sciore
 */
public class LogMgr implements Iterable<BasicLogRecord> {
   /**
    * The location where the pointer to the last integer in the page is.
    * A value of 0 means that the pointer is the first value in the page.
    */
   public static final int LAST_POS = 0;

   private String logfile;
   public Page mypage;
   private Block currentblk;
   private int currentpos;
   private Buffer currentBuffer;

   /**
    * Creates the manager for the specified log file.
    * If the log file does not yet exist, it is created
    * with an empty first block.
    * This constructor depends on a {@link FileMgr} object
    * that it gets from the method
    * {@link simpledb.server.SimpleDB#fileMgr()}.
    * That object is created during system initialization.
    * Thus this constructor cannot be called until
    * {@link simpledb.server.SimpleDB#initFileMgr(String)}
    * is called first.
    * @param logfile the name of the log file
    */
   public LogMgr(String logfile) {
//      
	   
      System.out.println("LogMgr constructor");
      this.logfile = logfile;
      System.out.println("logfile : "+ logfile);
//      int logsize = SimpleDB.fileMgr().size(logfile);
//      if (logsize == 0)
//         appendNewBlock();
//      else {
//         currentblk = new Block(logfile, logsize-1);
//         System.out.println("Current block : " + currentblk);
//         ///System.out.println(SimpleDB.bufferMgr().available());
//         //SimpleDB.bufferMgr().pin(currentblk);
//         //SimpleDB.bufferMgr().pinNew(logfile, );
//         mypage.read(currentblk);
//         currentpos = getLastRecordPosition() + INT_SIZE;
//      }
   }

   public void realConstructor() {
//	 currentBuffer=cBuff;
//	 mypage=currentBuffer.contents;
	 int logsize = SimpleDB.fileMgr().size(logfile);
     if (logsize == 0)
        appendNewBlock();
     else {
        currentblk = new Block(logfile, logsize-1);
        currentBuffer = SimpleDB.bufferMgr().setLogBlockToBufferPool(currentblk);
//        currentBuffer.assignToBlock(currentblk);
        System.out.println("Current block : " + currentblk);
        ///System.out.println(SimpleDB.bufferMgr().available());
        //SimpleDB.bufferMgr().pin(currentblk);
        //SimpleDB.bufferMgr().pinNew(logfile, );
        
//        currentpos = getLastRecordPosition() + INT_SIZE;
//        currentBuffer.pin();
//        cBuff.assignToBlock(currentblk);
        currentBuffer.contents.read(currentblk);
        currentpos = getLastRecordPosition() + INT_SIZE;
        SimpleDB.bufferMgr().bufferMgr.bufferPoolMap.put(currentblk,currentBuffer);
        currentBuffer.pin();
     }
   }
   
   
   /**
    * Ensures that the log records corresponding to the
    * specified LSN has been written to disk.
    * All earlier log records will also be written to disk.
    * @param lsn the LSN of a log record
    */
   public void flush(int lsn) {
      System.out.println("LogMgr: flush");
      if (lsn >= currentLSN())
         flush();
   }

   /**
    * Returns an iterator for the log records,
    * which will be returned in reverse order starting with the most recent.
    * @see java.lang.Iterable#iterator()
    */
   public synchronized Iterator<BasicLogRecord> iterator() {
      System.out.println("LogMgr: iterator");
      flush();
      return new LogIterator(SimpleDB.bufferMgr().bufferMgr.bufferpool[0].block());
   }
   
//   public void printLogPageBuffer()
//   {
//      
//      System.out.println("Buffer number pinned to the log block:" + currentBuffer.getBufferNumber());
//      System.out.println("Contents of Buffer :" + currentBuffer.toString());
//      
//            
//   }

   /**
    * Appends a log record to the file.
    * The record contains an arbitrary array of strings and integers.
    * The method also writes an integer to the end of each log record whose value
    * is the offset of the corresponding integer for the previous log record.
    * These integers allow log records to be read in reverse order.
    * @param rec the list of values
    * @return the LSN of the final value
    */
   public synchronized int append(Object[] rec) {
      System.out.println("LogMgr: append");
      System.out.println(rec.getClass());
      int recsize = INT_SIZE;  // 4 bytes for the integer that points to the previous log record
      for (Object obj : rec)
         recsize += size(obj);
      if (currentpos + recsize >= BLOCK_SIZE){ // the log record doesn't fit,
         flush();        // so move to the next block.
         appendNewBlock();
        // System.out.println("Buffer unpinned : " + currentBuffer.getBufferNumber());
        
      }
//      currentBuffer = SimpleDB.bufferMgr().pin(currentblk);
      for (Object obj : rec)
         appendVal(obj);
      finalizeRecord();
      return currentLSN();
   }

   
   /**
    * Adds the specified value to the page at the position denoted by
    * currentpos.  Then increments currentpos by the size of the value.
    * @param val the integer or string to be added to the page
    */
   private void appendVal(Object val) {
      System.out.println("LogMgr: appendVal");
      System.out.println("Write to Log : " + val);
      if (val instanceof String)
    	  currentBuffer.contents.setString(currentpos, (String)val);
      else
    	  currentBuffer.contents.setInt(currentpos, (Integer)val);
      currentpos += size(val);
   }

   /**
    * Calculates the size of the specified integer or string.
    * @param val the value
    * @return the size of the value, in bytes
    */
   private int size(Object val) {
      System.out.println("LogMgr: size");
      if (val instanceof String) {
         String sval = (String) val;
         return STR_SIZE(sval.length());
      }
      else
         return INT_SIZE;
   }

   /**
    * Returns the LSN of the most recent log record.
    * As implemented, the LSN is the block number where the record is stored.
    * Thus every log record in a block has the same LSN.
    * @return the LSN of the most recent log record
    */
   private int currentLSN() {
      System.out.println("LogMgr: currentLSN");
      return SimpleDB.bufferMgr().bufferMgr.bufferpool[0].block().number();
   }

   /**
    * Writes the current page to the log file.
    */
   private void flush() {
      System.out.println("LogMgr: flush");
      SimpleDB.bufferMgr().bufferMgr.bufferpool[0].contents.write(SimpleDB.bufferMgr().bufferMgr.bufferpool[0].block());
   }

   /**
    * Clear the current page, and append it to the log file.
    */
   private void appendNewBlock() {
      System.out.println("LogMgr: appendNewBlock");
      setLastRecordPosition(0);
      currentpos = INT_SIZE;
      
      Block newBlock=currentBuffer.contents.append(logfile);
      currentBuffer=SimpleDB.bufferMgr().changeBuffer(currentBuffer.block(), newBlock);
//      currentblk=newBlock;
//      mypage=currentBuffer.contents;
//      currentblk = mypage.append(logfile);
   }

   /**
    * Sets up a circular chain of pointers to the records in the page.
    * There is an integer added to the end of each log record
    * whose value is the offset of the previous log record.
    * The first four bytes of the page contain an integer whose value
    * is the offset of the integer for the last log record in the page.
    */
   private void finalizeRecord() {
      System.out.println("LogMgr: finalizeRecord");
      currentBuffer.contents.setInt(currentpos, getLastRecordPosition());
      setLastRecordPosition(currentpos);
      currentpos += INT_SIZE;
   }

   public int getLastRecordPosition() {
      System.out.println("LogMgr: getLastRecordPosition");
      return currentBuffer.contents.getInt(LAST_POS);
   }

   private void setLastRecordPosition(int pos) {
      System.out.println("LogMgr: setLastRecordPosition");
      currentBuffer.contents.setInt(LAST_POS, pos);
   }
   
   
   public Page rearRecords() {
	   return currentBuffer.contents;
   }
}
