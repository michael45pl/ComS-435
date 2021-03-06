package multisets;

import java.io.IOException;
import java.util.ArrayList;

public class SimilarDocuments {

	private LSH lsh;

	/**
	 * Creates an instance of MinHash that it uses to get minHashMatrix and docNames
	 * then calculates the number of rows with the given number of permutations to use
	 * then creates an instance of LSH with the found parameters
	 * @param folder the folder to check
	 * @param numPermutations number of permutations to use in hashing
	 * @param simThreshold the value to check similarity
	 * @throws IOException
	 */
	public SimilarDocuments(String folder, int numPermutations,  double simThreshold) throws IOException {
		MinHash mh = new MinHash(folder, numPermutations);
		int[][]mhMatrix = mh.minHashMatrix();//get Matrix from MinHash
		String[] docNames = mh.allDocs();//get file names
		
		double s = simThreshold;
		int r = 1;//start with only 1 row
		int tempResult = (int) Math.ceil((1/Math.pow(s, r))*r);
		while(numPermutations > tempResult && r < numPermutations){
			r++;
			tempResult = (int)Math.ceil((1/Math.pow(s, r))*r);
		}
		int bands = numPermutations/r;//divide permutations by rows to get bands
		
		lsh = new LSH(mhMatrix, docNames, bands);//construct the LSH
	}
	
	/**
	 * Uses LSH retreveSim to get similar documents from the LSH we created in SimilarDocuments
	 * @param document 
	 * @return a list of documents at least s-similar to the given document
	 * @throws IOException 
	 */
	public ArrayList<String> similaritySearch(String document) {
		return lsh.retrieveSim(document);
	}
}
