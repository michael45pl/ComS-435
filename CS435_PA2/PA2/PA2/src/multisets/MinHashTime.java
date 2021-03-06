package multisets;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MinHashTime {


	/**
	 * Function to test timing of different events specifically 3 events
	 * create an instance of MinHashSimilarites, for every pair of files 
	 * compute the exact Jaccard Similarity and use the MinHashMatrix to 
	 * estimate Jaccard Similarity
	 * @param folderName
	 * @param numPermutations
	 * @throws IOException
	 */
	public static void timer(String folderName, int numPermutations) throws IOException {		
		long timeStart;
		long timeEnd;

		timeStart = System.currentTimeMillis();
		@SuppressWarnings("unused")
		MinHashSimilarities mhs = new MinHashSimilarities(folderName, numPermutations);
		timeEnd = System.currentTimeMillis();

		System.out.println("Time taken to construct MinHashSimilarities in millisconds is: " + (timeEnd-timeStart));

		//get all documents in the folder
		ArrayList<Document> allDocuments = new ArrayList<>();
		File folder = new File(folderName);	
		File[] listOfFiles = folder.listFiles();
		Document d;
		for(int i=0;i<listOfFiles.length;i++){
			d = new Document(listOfFiles[i].getAbsolutePath());	
			allDocuments.add(d);
		}


		Document tempD1;
		Document tempD2;
		@SuppressWarnings("unused")
		double exactJacSimilarity;

		timeStart = System.currentTimeMillis();

		for(int i = 0; i < allDocuments.size(); i++){//loop through all documents
			tempD1 = allDocuments.get(i);//temp to compare against
			for(int j = i+1; j < allDocuments.size(); j++){//loop through remaining documents
				tempD2 = allDocuments.get(j);
				exactJacSimilarity = tempD1.exactJaccard(tempD2);//get the exact Jaccard
			}
		}
		timeEnd = System.currentTimeMillis();

		System.out.println("Time taken to compute every exact Jaccard Similarity in millisconds is: " + (timeEnd-timeStart));


		ArrayList<String> allTerms = new ArrayList<>();
		ArrayList<ParameterPair> paraList = new ArrayList<>();

		for(Document r: allDocuments) {
			for(String s:r.getTerms()){
				if(!allTerms.contains(s)) {//don't want duplicates
					allTerms.add(s);//adds if not in List
				}
			}
		}
		int numTerms = allTerms.size();

		int modP = HelperFunctions.nextPrime(numTerms*10);
		paraList = HelperFunctions.getParameters(numPermutations, numTerms);
		int pos = 0;
		for(Document l: allDocuments){
			for(ParameterPair pp: paraList){
				l.calcMinHash(pp.getA(), pp.getB(), modP);
			}
			pos++;
		}	



		d = null;
		@SuppressWarnings("unused")
		int approximateJaccardSimiliarity = 0;
		int docNum = 0;

		timeStart = System.currentTimeMillis();
		int[][] minHashMatrix = new int[numPermutations][allDocuments.size()];
		for(int i=0; i<allDocuments.size(); i++){
			d = allDocuments.get(i);
			for(int j = 0; j < numPermutations; j++){
				minHashMatrix[j][i] = d.getMinHashs().get(j);
			}
		}

		for(int i = 0; i < numPermutations; i++) {
			for(int j = docNum+1; j< allDocuments.size(); j++) {
				if(minHashMatrix[i][docNum] == minHashMatrix[i][j])
					approximateJaccardSimiliarity++;
			}
			docNum++;
		}

		timeEnd = System.currentTimeMillis();

		System.out.println("Time taken to estimate every pair of Jacard Similarity in millisconds is: " + (timeEnd-timeStart));

	}
}
