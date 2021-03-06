package bloomfilters;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.BitSet;

public class BloomDifferential {


	public class BloomFilter 
	{
		public int filterSize;
		public int numData;
		public int numHashes;	
		public BitSet filter;
		public HashFunction[] hashFuncs;


		public BloomFilter(int setSize, int bitsPerElement)
		{
			filterSize = setSize * bitsPerElement;
			numHashes = (int) Math.round(Math.log(2) * (filterSize / setSize));
			filter = new BitSet(filterSize);
			hashFuncs = new HashFunction[numHashes];
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
				key = hashFuncs[0].hash(s);
				filter.set(key);
			}	
		}	


		public boolean appears(String s)
		{
			int key;
			s = s.toLowerCase();

			for(int i = 0; i < numHashes; i++){
				s = s + i;
				key = hashFuncs[0].hash(s);
				if(!filter.get(key)){
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

	String diff_file_path = "src/Files/DiffFile.txt";
	String grams_file_path = "src/Files/grams.txt";
	BloomFilter bloomFilter;


	BloomFilter createFilter() {
		int setSize = GetSetSize(diff_file_path);
		bloomFilter = new BloomFilter(setSize, 4);
		ReadDiffFile(diff_file_path);
		return bloomFilter;
	}

	String RetrieveRecord(String s) {
		if (bloomFilter.appears(s)) {
			return s + " (FROM THE FILTER)";
		} else {
			return RetrieveRecordFromDatabase(s, grams_file_path);
		}
	}

	String RetrieveRecordFromDatabase(String s, String fileName) {

		String line = null;
		boolean isFound = false;

		try {

			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while((line = bufferedReader.readLine()) != null) {
				if (line.equals(s)) {
					isFound = true;
					break;
				}
			}

			bufferedReader.close();
			fileReader.close();
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		}
		catch(IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}

		if (isFound) {
			return s;
		}
		return "";
	}

	int GetSetSize(String fileName) {
		String line = null;

		String[] words;
		int count = 0;

		try {

			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while((line = bufferedReader.readLine()) != null) {

				words = line.split(" ");
				count += words.length;
			}   

			bufferedReader.close();
			fileReader.close();
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		}
		catch(IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
		return count;
	}

	void ReadDiffFile(String fileName) {

		String line = null;

		String whole_line;
		String[] words;

		try {

			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while((line = bufferedReader.readLine()) != null) {

				whole_line = "";
				words = line.split(" ");

				for (int i = 0; i < 4; i++) {
					if (i > 0) { whole_line += " "; }
					whole_line += words[i];
				}

				bloomFilter.add(whole_line);
			}   

			bufferedReader.close();
			fileReader.close();
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		}
		catch(IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
	}

}
