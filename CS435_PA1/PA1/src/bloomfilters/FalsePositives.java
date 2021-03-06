package bloomfilters;

import java.util.ArrayList;
import java.util.Random;

public class FalsePositives 
{
	static final String CANDIDATES = "0123456789abcdefghijklmnopqrstuvwxyz";
	static final int wordlen = 10;
	static Random r;
	private static BloomFilterFNV FNV;
	private static BloomFilterRan Ran;
	private static MultiMultiBloomFilter Multi;
	private static NaiveBloomFilter Naive;
	private static int[] falsePositives;
	private static ArrayList<String> list;
	private static final double numTests = 1000;
	private static final int setSize = 1000;
	private static final int bitsPerElement = 10;


	public static void main(String[] args){
		initialize();//initialize my filters

		String s;

		double i = numTests;
		do {
			s = randomString(wordlen);//get random s

			if(list.contains(s.toLowerCase()))//check if s was added to the filters
				continue;//if so then go back to top
			if(FNV.appears(s)) //check filter
				falsePositives[0]++;
			if(Ran.appears(s))
				falsePositives[1]++;
			if(Multi.appears(s)) 
				falsePositives[2]++;
			if(Naive.appears(s))
				falsePositives[3]++;

			i--;
		}while (i > 0);

		System.out.printf("BloomFilterFNV False Positive Rate: %.2f%%\n", (((double) falsePositives[0] / numTests) * 100));
		System.out.printf("BloomFilterRan False Positive Rate: %.2f%%\n", (((double) falsePositives[1] / numTests) * 100));
		System.out.printf("MultiMultiBloomFilter False Positive Rate: %.2f%%\n", (((double) falsePositives[2] / numTests) * 100));
		System.out.printf("NaiveBloomFilter False Positive Rate: %.2f%%\n", (((double) falsePositives[3] / numTests) * 100));

	}

	/**
	 * Initializes all Bloom Filters and adds random strings to them
	 */
	private static void initialize()
	{
		r = new Random();
		falsePositives = new int[] {0,0,0,0}; 
		list = new ArrayList<String>();
		FNV = new BloomFilterFNV(setSize, bitsPerElement);
		Ran = new BloomFilterRan(setSize, bitsPerElement);
		Multi = new MultiMultiBloomFilter(setSize, bitsPerElement);
		Naive = new NaiveBloomFilter(setSize, bitsPerElement);

		String random;
		for(int i = 0; i < setSize; i++){
			random = randomString(wordlen);

			FNV.add(random);
			Ran.add(random);
			Multi.add(random);
			Naive.add(random);

			list.add(random.toLowerCase());
		}
	}

	private static String randomString(int len) 
	{
	   StringBuilder sb = new StringBuilder( len );
	   for( int i = 0; i < len; i++ ) 
	      sb.append(CANDIDATES.charAt(r.nextInt(CANDIDATES.length())));
	   return sb.toString();
	}
}