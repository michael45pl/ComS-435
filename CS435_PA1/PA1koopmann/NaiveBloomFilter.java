package bloomfilters;

public class NaiveBloomFilter {

	public int filterSize;
	public int numData;
	public int numHashes;	
	public int[] filter;
	public HashFunctionRan hashFunc;


	public NaiveBloomFilter(int setSize, int bitsPerElement)
	{
		filterSize = setSize * bitsPerElement;
		numHashes = (int) Math.round(Math.log(2) * (filterSize / setSize));
		filter = new int[filterSize];
		hashFunc = new HashFunctionRan(filterSize);//get random hash function
		numData = 0;
	}


	public void add(String s)
	{
		int key;
		s = s.toLowerCase();

		if(!appears(s)){
			numData++;
		}

		for(int i = 0; i < numHashes; i++){
			s = s + i;
			key = hashFunc.hash(s);
			filter[key] = 1;//set the array bit
		}	
	}	


	public boolean appears(String s)
	{
		int key;
		s = s.toLowerCase();

		for(int i = 0; i < numHashes; i++){
			s = s + i;
			key = hashFunc.hash(s);
			if(filter[key] != 1){
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
	
	public boolean getBit(int j){
		return(filter[j] == 1);//check if set
	}

}
