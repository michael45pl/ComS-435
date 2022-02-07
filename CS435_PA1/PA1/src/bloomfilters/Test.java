package bloomfilters;


import java.util.HashSet;
import java.util.Random;


public class Test {

	static final String CANDIDATES = "0123456789abcdefghijklmnopqrstuvwxyz";
	static final int wordlen = 10;
	static Random r;
	private static BloomFilterFNV FNV;
	private static BloomFilterFNV F1;
	private static BloomFilterFNV F2;
	private static HashSet<String> H1;
	private static HashSet<String> H2;
	static final double numTests = 1000;
	static final int setSize = 1000;
	static final int bitsPerElement = 10;
	static Random rand;

	public static void main (String[] args) {
		estimateSetTest();
		unionTest();
	}

	private static void unionTest() {
		int count = 0;
		rand = new Random();
		int j = 0;
		int s = setSize;
		for(int i = 0; i < numTests; i++) {
			j = rand.nextInt(s-1);
			F1 = initialize(j);
			H2 = new HashSet<String>(H1);
			F2 = initialize(j);
			H2.retainAll(H1);
			if(Statictics.estimateIntersectSize(F1, F2) != H2.size())
				count++;
		}

		System.out.printf("estimateIntersectSize False Rate: %.2f%%\n", (((double) count / numTests) * 100));
	}

	private static void estimateSetTest() {
		int count = 0;
		int guess = 0;
		for(int i = 0; i < numTests; i++) {
			FNV = initialize(i);
			guess = Statictics.estimateSetSize(FNV);
			if(guess != i)
				count++;
		}

		System.out.printf("estimateSet False Rate: %.2f%%\n", (((double) count / numTests) * 100));
	}

	private static BloomFilterFNV initialize(int n) {
		H1 = new HashSet<String>();
		r = new Random();
		BloomFilterFNV f = new BloomFilterFNV(setSize, bitsPerElement);

		String random;
		for(int i = 0; i < n; i++){
			random = randomString(wordlen);

			H1.add(random);
			f.add(random);
		}
		return f;	
	}


	private static String randomString(int len) 
	{
		StringBuilder sb = new StringBuilder( len );
		for( int i = 0; i < len; i++ ) 
			sb.append(CANDIDATES.charAt(r.nextInt(CANDIDATES.length())));
		return sb.toString();
	}

}


