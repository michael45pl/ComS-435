package multisets;

import java.io.IOException;

public class MinHashAccuracy {

	/**
	 * Reports the number of pairs for which exact and approximate similarities differ by 
	 * more than e or more
	 * @param folderName 
	 * @param numPermutations
	 * @param errorParameter
	 * @return number of pairs where similarities differ by e
	 * @throws IOException
	 */
	public static int accuracy(String folderName, int numPermutations, float errorParameter) throws IOException {
		String folderPath = folderName;
		int numPermutation = numPermutations;
		float e = errorParameter;
		
		MinHash mh = new MinHash(folderPath, numPermutation);//create a minHash

		Document tempD1;
		Document tempD2;
		double exactJacSimilarity;
		double approJacSimilarity;

		int count = 0;
		for(int i = 0; i < mh.getAllDocuments().size(); i++){//loop through all documents
			tempD1 = mh.getAllDocuments().get(i);//create a temp value to compare with
			for(int j = i+1; j < mh.getAllDocuments().size(); j++){//loop through all the remaining documents
				tempD2 = mh.getAllDocuments().get(j);//get the next document
				exactJacSimilarity = tempD1.exactJaccard(tempD2);
				approJacSimilarity = tempD1.approximateJaccard(tempD2);
				if(Math.abs(exactJacSimilarity-approJacSimilarity)>e)//check if differ by more than e
					count++;//increment
			}
		}
		return count;//return count;

	}
}
