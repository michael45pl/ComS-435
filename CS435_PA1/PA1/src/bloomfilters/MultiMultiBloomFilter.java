package bloomfilters;

public class MultiMultiBloomFilter{

	public int filterSize;
	public int numData;
	public int numHashes;	
	public int[][] k_arrays;
	public HashFunctionRan[] hashFuncs;


	public MultiMultiBloomFilter(int setSize, int bitsPerElement) {
		numData = 0;
		filterSize = setSize*bitsPerElement;
		numHashes = bitsPerElement;
		k_arrays = new int[bitsPerElement][setSize];//make arrays
		for(int i = 0; i < bitsPerElement; i++) {
			for(int j = 0; j < setSize; j++) {
				k_arrays[i][j] = 0;//set arrays
			}
		}

		hashFuncs = new HashFunctionRan[numHashes];//get random hash functions

		for(int i = 0; i < numHashes; i++) {
			hashFuncs[i] = new HashFunctionRan(setSize);
		}

	}


	public void add(String s) {
		int key;
		s = s.toLowerCase();

		if(!appears(s)) {
			numData++;
		}

		for(int i = 0; i < numHashes; i++) {
			key = hashFuncs[i].hash(s);//get the ith hash value
			k_arrays[i][key] = 1;//set the ith array with the hash value
		}
	}

	public boolean appears(String s) {
		int key;
		s = s.toLowerCase();
		for(int i = 0; i < numHashes; i++) {
			key = hashFuncs[i].hash(s);
			if(k_arrays[i][key] != 1){
				return false;
			}
		}
		return true;
	}

	public int filterSize() {
		return filterSize;
	}

	public int dataSize() {
		return numData;
	}

	public int numHashes() {
		return numHashes;
	}

	public boolean getBit(int j){
		int array_num = Math.round(j / numHashes);//get the array
		int pos = j % numHashes;//get the array pos

		return(k_arrays[array_num][pos] == 1);//check if set
	}
}
