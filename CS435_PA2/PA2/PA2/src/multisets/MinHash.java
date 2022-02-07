package multisets;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MinHash {

	private String folderPath;
	private int numPermutations;
	private ArrayList<String> allTerms;
	private ArrayList<Document> allDocuments;
	private int numTerms;
	private int modP;
	
	private ArrayList<ParameterPair> paraList;
	
	private int minHashMatrix[][];
	private int termDocMatrix[][];
	

	/**
	 * Constructor class to set all my necessary values and calls the addDocument and my
	 * helper functions.
	 * @param folder 
	 * @param numPermutations
	 * @throws IOException
	 */
	public MinHash(String folder, int numPermutations) throws IOException{
		folderPath = folder;
		this.numPermutations = numPermutations;
		allDocuments = new ArrayList<Document>();
		allTerms = new ArrayList<String>();
		paraList = new ArrayList<ParameterPair>();
		addDocuments();

		numTerms = allTerms.size();
		termDocMatrix = new int[numTerms][allDocuments.size()];
		minHashMatrix = new int[numPermutations][allDocuments.size()];
		modP = HelperFunctions.nextPrime(numTerms*10);//gets the next Prime from helper function
		paraList = HelperFunctions.getParameters(this.numPermutations, this.numTerms);//get a and b from helper functions

		setUp();
	}

	/**
	 * Returns the names of all documents in the given folder
	 * @return String array of document names
	 */
	public String[] allDocs(){
		File f = new File(folderPath);
		return f.list();
	}

	/**
	 * Returns the termDocument Matrix which is the frequency matrix
	 * @return the termDocumnet Matrix
	 */
	public int[][] termDocumentMatrix() {
		return termDocMatrix;
	}

	/**
	 * Returns the number of terms recorded
	 * @return number of terms
	 */
	public int permutationDomain() {
		return numTerms;
	}
	
	/**
	 * Returns the number of permutations used for the minHashMatrix
	 * @return number of permutations
	 */
	public int numPermutations(){
		return numPermutations;
	}

	/**
	 * Returns the minHash Matrix of the given folder
	 * @return minHash Matrix
	 */
	public int[][] minHashMatrix(){
		Document d;
		for(int i = 0; i < allDocuments.size(); i++){
			d = allDocuments.get(i);
			for(int j = 0; j < numPermutations; j++){
				minHashMatrix[j][i] = d.getMinHashs().get(j);
			}
		}
		return this.minHashMatrix;
	}


	/**
	 * Creates a document object for each file in the given folder then gets all the terms from the
	 * Document then gets the frequency of the term from the Document and adds it to the termDocMatrix
	 * @throws IOException
	 */
	private void addDocuments() throws IOException{
		File folder = new File(this.folderPath);	
		File[] listOfFiles = folder.listFiles();

		Document d;
		for(int i = 0; i < listOfFiles.length; i++){
			d = new Document(listOfFiles[i].getAbsolutePath());	
			allDocuments.add(d);//adds the document to ArrayList
			for(String s:d.getTerms()){
				if(!allTerms.contains(s)) {//don't want duplicates
					allTerms.add(s);//adds if not in List
				}
			}
		}
	}
	
	private void setUp() throws IOException {
		File folder = new File(this.folderPath);	
		File[] listOfFiles = folder.listFiles();

		int pos = 0;
		for(Document d: allDocuments){
			for(String s:d.getTerms()){
				termDocMatrix[allTerms.indexOf(s)][pos]=d.termMap.get(s); //gets the frequency from document then inserts it into the termDoc Matrix
			}
			for(ParameterPair pp: paraList){
				d.calcMinHash(pp.getA(), pp.getB(), this.modP);
			}
			pos++;
		}	
	}

	/**
	 * Returns the parameter list used for the hashing functions
	 * @return list of Parameters
	 */
	public ArrayList<ParameterPair> getParaList() {
		return paraList;
	}
	
	/**
	 * Returns the value used to mod in the hashing functions
	 * @return modP
	 */
	public int getModP() {
		return modP;
	}
	
	/**
	 * Returns the ArrayList of Documents which is a created object type
	 * @return all Documents
	 */
	public ArrayList<Document> getAllDocuments() {
		return allDocuments;
	}
	
	

}
