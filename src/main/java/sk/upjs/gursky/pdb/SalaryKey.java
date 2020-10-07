package sk.upjs.gursky.pdb;

import sk.upjs.gursky.bplustree.BPKey;

import java.io.Serializable;
import java.nio.ByteBuffer;


public class SalaryKey implements BPKey<SalaryKey>, Serializable
{

	private static final long serialVersionUID = 330263900419603008L;
	private int key;

	public SalaryKey() {}

	public SalaryKey(int key)
	{
		this.key = key;
	}
	
	public void load(ByteBuffer bb)
	{
		key = bb.getInt();
	}
	
	public void save(ByteBuffer bb)
	{
		bb.putInt(key);
	}
	
	public int getSize()
	{
		return 4;
	}

	@Override
	public String toString()
	{
		return new Integer(key).toString();
	}
	
	/**
	 * Compares this object with the specified object for order. 
	 * Returns a negative integer, zero, or a positive integer as this object is less than, 
	 * equal to, or greater than the specified object.
	 * 
	 * @param o The object to be compared. 
	 * @return A negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object. 
	 */
	public int compareTo(SalaryKey o){
		if (key < o.key) return -1;
		if (key > o.key) return 1;
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof SalaryKey)) return false;
		return key == ((SalaryKey)obj).key;
	}

	public int getKeyInt() {
		return key;
	}

}
