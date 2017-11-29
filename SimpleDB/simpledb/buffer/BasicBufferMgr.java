package simpledb.buffer;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import simpledb.file.*;
import simpledb.server.SimpleDB;

/**
 * Manages the pinning and unpinning of buffers to blocks.
 * 
 * @author Edward Sciore
 *
 */
public class BasicBufferMgr {
	private int k = 2;
	Map<Block, ArrayList<Long>> timeMap;
	public Buffer[] bufferpool;
	private int numAvailable;
	public Map<Block, Buffer> bufferPoolMap;

	/**
	 * Creates a buffer manager having the specified number of buffer slots. This
	 * constructor depends on both the {@link FileMgr} and
	 * {@link simpledb.log.LogMgr LogMgr} objects that it gets from the class
	 * {@link simpledb.server.SimpleDB}. Those objects are created during system
	 * initialization. Thus this constructor cannot be called until
	 * {@link simpledb.server.SimpleDB#initFileAndLogMgr(String)} or is called
	 * first.
	 * 
	 * @param numbuffs
	 *            the number of buffer slots to allocate
	 */
	
	BasicBufferMgr(int numbuffs) {
		timeMap = new HashMap<Block, ArrayList<Long>>();
		bufferPoolMap = new HashMap<Block, Buffer>();
		bufferpool = new Buffer[numbuffs];
		numAvailable = numbuffs;
		
		for (int i = 0; i < numbuffs; i++)
			bufferpool[i] = new Buffer(i);
		// System.out.println("YOlo");
//		SimpleDB.logMgr().realConstructor();
//		bufferPoolMap.put(bufferpool[0].block(), bufferpool[0]);
//		pin(bufferpool[0].block());
	}

	/**
	 * Flushes the dirty buffers modified by the specified transaction.
	 * 
	 * @param txnum
	 *            the transaction's id number
	 */
	synchronized void flushAll(int txnum) {
		for (Buffer buff : bufferpool)
			if (buff.isModifiedBy(txnum))
				buff.flush();
	}

	/**
	 * Pins a buffer to the specified block. If there is already a buffer assigned
	 * to that block then that buffer is used; otherwise, an unpinned buffer from
	 * the pool is chosen. Returns a null value if there are no available buffers.
	 * 
	 * @param blk
	 *            a reference to a disk block
	 * @return the pinned buffer
	 */
	// synchronized Buffer pin(Block blk) {
	// Buffer buff = findExistingBuffer(blk);
	// if (buff == null) {
	// buff = chooseUnpinnedBuffer();
	// if (buff == null)
	// return null;
	// buff.assignToBlock(blk);
	// }
	// if (!buff.isPinned())
	// numAvailable--;
	// buff.pin();
	// return buff;
	// }

	//
	synchronized Buffer pin(Block blk) {

		System.out.println("pin");
		Buffer buff = findExistingBuffer(blk);
		if (buff == null) {
			buff = chooseUnpinnedBuffer();
			if (buff == null)
				return null;
			bufferPoolMap.remove(buff.block());
			buff.assignToBlock(blk);
			bufferPoolMap.put(blk, buff);
			// buff.count = System.currentTimeMillis();

		}
		if (!buff.isPinned())
			numAvailable--;

		ArrayList<Long> times = timeMap.get(blk);
		// timeMap.remove(blk);
		if (times == null) {
			times = new ArrayList<Long>();
		}
		times.add(System.nanoTime());
		// System.out.println("-------------------adding into list------------------");
		// System.out.println(times.size());
		timeMap.put(blk, times);

		buff.pin();

		System.out.println("Buffer pool content");
		System.out.println(bufferPoolMap.get(bufferpool[0].block()));
		for (Block l : bufferPoolMap.keySet()) {
			System.out.println("Buffer no : " + findExistingBuffer(l).getBufferNumber() + "block : " + l + " pins : "
					+ bufferPoolMap.get(l).getPins());
			// System.out.print();
		}

		// System.out.println("----------------------");

		// for(Block l:timeMap.keySet()) {
		// System.out.println(l);
		// Iterator<Long> it=timeMap.get(l).iterator();
		// while(it.hasNext()) {
		// System.out.print(it.next()+" ");
		// }
		// System.out.println();
		// }

		return buff;
	}

	private Buffer findExistingBuffer(Block blk) {
		Buffer result = bufferPoolMap.get(blk);
		if (result != null)
			return result;
		else
			return null;
	}

	// private Buffer findExistingBuffer(Block blk) {
	// for (Buffer buff : bufferpool) {
	// Block b = buff.block();
	// if (b != null && b.equals(blk))
	// {
	// System.out.println("BOOLK :? "+ b + blk);
	// System.out.println("Pappu :" + bufferPoolMap);
	// return buff;
	// }
	//
	// }
	//
	// return null;
	// }

	synchronized Buffer pinNew(String filename, PageFormatter fmtr) {
		Buffer buff = chooseUnpinnedBuffer();
		if (buff == null)
			return null;
		bufferPoolMap.remove(buff.block());
		buff.assignToNew(filename, fmtr);
		numAvailable--;
		buff.pin();
		ArrayList<Long> times = timeMap.get(buff.block());
		timeMap.remove(buff.block());
		if (times == null) {
			times = new ArrayList<Long>();
		}
		times.add(System.nanoTime());
		// Iterator it=times.iterator();
		// System.out.println("adding
		// value-------------------------------------------");
		// System.out.println(times.size());
		timeMap.put(buff.block(), times);
		bufferPoolMap.put(buff.block(), buff);
		return buff;
	}

	//
	synchronized void unpin(Buffer buff) {
		System.out.println("unpin");
		// Block blk = buff.block();
		// bufferPoolMap.remove(blk);

		buff.unpin();

		if (!buff.isPinned())
			numAvailable++;
	}

	/**
	 * Allocates a new block in the specified file, and pins a buffer to it. Returns
	 * null (without allocating the block) if there are no available buffers.
	 * 
	 * @param filename
	 *            the name of the file
	 * @param fmtr
	 *            a pageformatter object, used to format the new block
	 * @return the pinned buffer
	 */
	// synchronized Buffer pinNew(String filename, PageFormatter fmtr) {
	// Buffer buff = chooseUnpinnedBuffer();
	// if (buff == null)
	// return null;
	// buff.assignToNew(filename, fmtr);
	// numAvailable--;
	// buff.pin();
	// return buff;
	// }

	/**
	 * Unpins the specified buffer.
	 * 
	 * @param buff
	 *            the buffer to be unpinned
	 */
	// synchronized void unpin(Buffer buff) {
	// buff.unpin();
	// if (!buff.isPinned())
	// numAvailable++;
	// }

	/**
	 * Returns the number of available (i.e. unpinned) buffers.
	 * 
	 * @return the number of available buffers
	 */
	int available() {
		return numAvailable;
	}

	private Buffer chooseUnpinnedBuffer() {
		if(SimpleDB.bufferMgr().available()==0) {
			throw new IllegalArgumentException("All Buffers are pinned currently.");
		}
		for (Buffer buff : bufferpool)
			if (!buff.isPinned() && !bufferPoolMap.containsValue(buff))
				return buff;
		// return null;
		long earliestTime = Long.MAX_VALUE;

		long earliestTimeLessThanK = Long.MAX_VALUE;
		Block earliestBlock = null;
		Block earliestBlockLessThanK = null;
		int flag = 0;
		for (Entry<Block, ArrayList<Long>> set : timeMap.entrySet()) {
			if (bufferPoolMap.get(set.getKey()) == null)
				continue;
			if (!bufferPoolMap.get(set.getKey()).isPinned()) {
				if (set.getValue().size() >= k && set.getValue().get(set.getValue().size() - k) < earliestTime) {
					earliestTime = set.getValue().get(set.getValue().size() - k);
					earliestBlock = set.getKey();
				} else if (set.getValue().size() < k
						&& earliestTimeLessThanK > set.getValue().get(set.getValue().size() - 1)) {
					earliestTimeLessThanK = set.getValue().get(set.getValue().size() - 1);
					earliestBlockLessThanK = set.getKey();
					flag = 1;
				}
			}
		}
		if (flag == 1) {
			earliestTime = earliestTimeLessThanK;
			earliestBlock = earliestBlockLessThanK;
		}
		Buffer earliestbuff = bufferPoolMap.get(earliestBlock);

		return earliestbuff;
	}

	boolean containsMapping(Block blk) {
		return bufferPoolMap.containsKey(blk);
	}

	public Buffer getMapping(Block blk) {
		return bufferPoolMap.get(blk);
	}

}