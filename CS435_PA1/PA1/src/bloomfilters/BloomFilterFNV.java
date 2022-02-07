package bloomfilters;

import java.util.BitSet;

public class BloomFilterFNV{

	public int filterSize;//keeps track of size of bloom filter
	public int numData;//keeps track of number of data entered
	public int numHashes;//calculated best number of hash function	
	public BitSet filter;//the bloom filter
	public HashFunction hashFunc;//hash function

	public BloomFilterFNV(int setSize, int bitsPerElement) {
		filterSize = setSize * bitsPerElement;
		numHashes = (int) Math.round(Math.log(2) * (filterSize / setSize));//equation to estimate number hash functions
		filter = new BitSet(filterSize);
		numData = 0;
		hashFunc = new HashFunctionFNV();
	}

	private class HashFunctionFNV extends HashFunction
	{
		private static final long FNV_64_INIT = 0xcbf29ce484222325L;
		private static final long FNV_64_PRIME = 1099511628211L;


		public int hash(String s) {
			return hash(s.getBytes());
		}

		public int hash(byte[] a) {
			long result = FNV_64_INIT;//set to INIT
			int len = a.length;
			for(int i = 0; i < len; i++) {//repeat for each bit of the entry
				result ^= a[i];//xor
				result = result * FNV_64_PRIME;//multiply by the PRIME
			}
			return mod(result, filterSize);//mod by filterSize so it's within bounds
		}
	}

	public void add(String s)
	{
		int key;
		s = s.toLowerCase();

		if(!appears(s)){//checks if its already in filter
			numData++;//if not increase data number tracker
		}

		for(int i = 0; i < numHashes; i++){//hash numHashes times
			s = s + i;//add the number to the string
			key = hashFunc.hash(s);
			filter.set(key);//set bits
		}	
	}	


	public boolean appears(String s)
	{
		int key;
		s = s.toLowerCase();

		for(int i = 0; i < numHashes; i++){//get the hash code back
			s = s + i;
			key = hashFunc.hash(s);
			if(!filter.get(key)){//get bits
				return false;
			}
		}
		return true;
	}


	public int filterSize()
	{
		return filterSize;
	}


	public int dataSize()
	{
		return numData;
	}


	public int numHashes()
	{
		return numHashes;
	}


	public boolean getBit(int j) {
		return filter.get(j);
	}



}

