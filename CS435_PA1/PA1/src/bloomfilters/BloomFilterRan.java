package bloomfilters;

import java.util.BitSet;

public class BloomFilterRan{

	public int filterSize;
	public int numData;
	public int numHashes;	
	public BitSet filter;
	public HashFunction[] hashFuncs;//array of hashfunctions  


	public BloomFilterRan(int setSize, int bitsPerElement) {
		filterSize = setSize * bitsPerElement;
		numHashes = (int) Math.round(Math.log(2) * (bitsPerElement));
		filter = new BitSet(filterSize);
		hashFuncs = new HashFunctionRan[numHashes];

		for(int i = 0; i < numHashes; i++) {
			hashFuncs[i] = new HashFunctionRan(filterSize);
		}
		numData = 0;
	}

	public void add(String s) {
		int key;
		s = s.toLowerCase();

		if(!appears(s)) {
			numData++;
		}

		for(int i = 0; i < numHashes; i++) {
			key = hashFuncs[i].hash(s);//go through each hash function 
			filter.set(HashFunction.mod(key, filterSize));//get the key and mod it to insure bounds
		}
	}

	public boolean appears(String s) {
		int key;
		s = s.toLowerCase();
		for(int i = 0; i < numHashes; i++) {
			key = hashFuncs[i].hash(s);
			if(!filter.get(HashFunction.mod(key, filterSize))){
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