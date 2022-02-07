package multisets;

import java.io.IOException;
import java.util.ArrayList;

public class MinHashSimilarities{

	private int minHashMatrix[][];
	private int termDocMatrix[][];
	private ArrayList<Document> allDocuments;
	private ArrayList<ParameterPair> paraList;
	private int numPermutations;
	private int modP;


	/**
	 * Creates an instance of MinHash and stores the termDocumentMatrix and minHashMatrix
	 * @param folder
	 * @param numPermutations
	 * @throws IOException
	 */
	public MinHashSimilarities(String folder, int numPermutations) throws IOException{
		MinHash m =  new MinHash(folder, numPermutations);
		this.minHashMatrix = m.minHashMatrix();
		this.termDocMatrix = m.termDocumentMatrix();
		this.allDocuments = m.getAllDocuments();
		this.numPermutations = numPermutations;
		this.paraList = m.getParaList();
		this.modP = m.getModP();
	}

	/**
	 * Uses the exactJaccard function in the Document class
	 * @param file1
	 * @param file2
	 * @return the calculated double
	 * @throws NullPointerException
	 */
	public double exactJaccard(String file1, String file2) throws NullPointerException {
		Document d1 = null;
		Document d2 = null;
		for(Document d : allDocuments) {
			String temp = d.getFileName();
			if(temp.equals(file1)) {
				d1 = d;
			}
			if(temp.equals(file2)) {
				d2 = d;
			}
		}
		if(d1 == null || d2 == null) {
			throw new NullPointerException("Couldn't not find file in given folderpath");
		}else {
			return d1.exactJaccard(d2);
		}
	}

	/**
	 * Uses the approximateJaccard in the Document class
	 * @param file1
	 * @param file2
	 * @return the calculated double
	 * @throws NullPointerException
	 */
	public double approximateJaccard(String file1, String file2) throws NullPointerException {
		Document d1 = null;
		Document d2 = null;
		for(Document d : allDocuments) {
			String temp = d.getFileName();
			if(temp.equals(file1)) {
				d1 = d;
			}
			if(temp.equals(file2)) {
				d2 = d;
			}
		}
		if(d1 == null || d2 == null) {
			throw new NullPointerException("Couldn't not find file in given folderpath");
		}else {
			return d1.approximateJaccard(d2);
		}
	}

	/**
	 * Uses the calcMinHash function in the Document class along with the parameters and modP
	 * calculated in from the MinHash to get all the MinHashs
	 * @param fileName
	 * @return array of ints
	 * @throws NullPointerException
	 */
	public int[] minHashSig(String fileName) throws NullPointerException {
		Document d = null;
		for(Document tempd : allDocuments) {
			String temp = tempd.getFileName();
			if(temp.equals(fileName)) {
				d = tempd;
			}
		}
		if(d == null) {
			throw new NullPointerException("Couldn't not find file in given folderpath");
		}else {
			for(ParameterPair pp: paraList){
				d.getMinHashs().add(d.calcMinHash(pp.getA(), pp.getB(), modP));
			}
			return d.getMinHashsReturn();
		}
	}


}
