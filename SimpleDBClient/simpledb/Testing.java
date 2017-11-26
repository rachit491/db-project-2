package simpledb;

import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import simpledb.buffer.Buffer;
import simpledb.buffer.BufferAbortException;
import simpledb.buffer.BufferMgr;
import simpledb.file.Block;
import simpledb.file.Page;
import simpledb.log.BasicLogRecord;
import simpledb.server.SimpleDB;

public class Testing {
	public static void main(String args[]) {

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
		// Block blk2 = new Block("filename.txt", 2);

		BufferMgr basicBufferMgr = SimpleDB.bufferMgr();
		
//		SimpleDB.logMgr().printLogPageBuffer();

		try {
			
			// basicBufferMgr.pin(blk3);
			basicBufferMgr.pin(blk1);
			basicBufferMgr.pin(blk2);
			basicBufferMgr.pin(blk3);
			basicBufferMgr.pin(blk4);
			basicBufferMgr.pin(blk5);
			basicBufferMgr.pin(blk6);
			basicBufferMgr.pin(blk7);
			basicBufferMgr.unpin(basicBufferMgr.getMapping(blk6));
			basicBufferMgr.unpin(basicBufferMgr.getMapping(blk6));
			basicBufferMgr.unpin(basicBufferMgr.getMapping(blk6));
			basicBufferMgr.unpin(basicBufferMgr.getMapping(blk6));
			basicBufferMgr.unpin(basicBufferMgr.getMapping(blk6));
			basicBufferMgr.unpin(basicBufferMgr.getMapping(blk6));
			basicBufferMgr.unpin(basicBufferMgr.getMapping(blk6));
			
//			basicBufferMgr.pin(blk8);
//			basicBufferMgr.pin(blk3);
//			System.out.println("##### ADD BLK 3 #####");
//			System.out.println(SimpleDB.logMgr().mypage.getString(0));
//			System.out.println("Start  --->");
//			Iterator<BasicLogRecord> it = SimpleDB.logMgr().iterator();
//			while (it.hasNext()) {
//				BasicLogRecord temp = it.next();
//				System.out.println(temp.nextInt() + "   " + temp.nextString());
//			}

		} catch (BufferAbortException e) {
		}
	}

	Scanner sc = new Scanner(System.in);
	int age = sc.nextInt();

}
