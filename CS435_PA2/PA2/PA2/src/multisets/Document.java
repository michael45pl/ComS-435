package multisets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Document {

	private ArrayList<String> terms;
	private String filePath;
	public ArrayList<Integer> minHashs;
	public HashMap<String, Integer> termMap;


	/**
	 * Creates a Document from the given filePath.
	 * @param filePath
	 * @throws IOException
	 */
	public Document(String filePath) throws IOException{
		this.terms = new ArrayList<String>();
		this.filePath = filePath;
		this.termMap = new HashMap<>();
		this.minHashs = new ArrayList<Integer>();
		preProcessing();
	}

	/**
	 * Converts all characters into lower case, removes punctuation mark, words less than 
	 * length of 3 and the word the
	 * @throws IOException
	 */
	private void preProcessing() throws IOException{
		File doc = new File(filePath);
		FileReader fr = new FileReader(doc);
		BufferedReader br = new BufferedReader(fr);
		int count;

		String line;
		Pattern pattern = Pattern.compile("([A-Za-z]{3,})");//look for alphabetical characters size 3 or longer
		Matcher matcher;
		String tempString;
		while ((line = br.readLine()) != null) {
			matcher = pattern.matcher(line);
			while(matcher.find()){
				tempString = matcher.group();
				tempString = tempString.toLowerCase();//convert to lower case
				if(!tempString.matches("(?i)the")){//make sure it not the
					if(!terms.contains(tempString))
						this.terms.add(tempString);

					if(termMap.containsValue(tempString)) {//check if string is in the termMap
						count = termMap.get(tempString);
						termMap.put(tempString, count+1);//if so then update the count
					}else {
						termMap.putIfAbsent(tempString, 1);//if not then add to the map
					}
				}
			}
		}
		br.close();
	}

	/**
	 * Returns ArrayList of terms from the document
	 * @return terms
	 */
	public ArrayList<String> getTerms() {
		return terms;
	}

	/**
	 * Returns a String of the filepath
	 * @return filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Returns ArrayList of the minHashes of Document
	 * @return minHashs
	 */
	public ArrayList<Integer> getMinHashs() {
		return minHashs;
	}

	/**
	 * Returns Array of the minHashes of the Document
	 * @return
	 */
	public int[] getMinHashsReturn() {
		int[] ret = new int[minHashs.size()];
		for(int i = 0; i < minHashs.size(); i++) {
			ret[i]=minHashs.get(i);
		}
		return ret;
	}

	/**
	 * Returns the documents name
	 * @return
	 */
	public String getFileName(){
		File f = new File(this.filePath);
		String fName = f.getName();
		f = null;
		return fName;
	}

	/**
	 * Calculates the minHash of the term it gets the hash value from hashCode for the base
	 * value then it does the permutation with the given parameters to get the value
	 * then it checks it to see if its the minimum
	 * @param a random value
	 * @param b random value
	 * @param modP Prime Number
	 * @return the minimum hashValue
	 */
	public int calcMinHash(int a,int b, int modP){
		int hashValue;
		int permValue;
		int minPermValue = modP;

		for(String s: this.terms){
			hashValue = s.hashCode();
			permValue = (hashValue*a+b)%modP;
			if(minPermValue > permValue){
				minPermValue = permValue;
			}
		}
		this.minHashs.add(minPermValue);
		return minPermValue;
	}


	/**
	 * Calculates the exact Jaccard by getting the intersection size and then the union size
	 * and dividing the values
	 * @param d2 second document to check
	 * @return intersection size divided by the union size
	 */
	public double exactJaccard(Document d2){

		double d1Size = terms.size();
		double d2Size = d2.getTerms().size();
		double totalNum = d1Size + d2Size;//get total size of both sets

		ArrayList<String> tempD1 = (ArrayList<String>) terms.clone();//create a temp
		ArrayList<String> tempD2 = (ArrayList<String>) d2.getTerms().clone();
		tempD1.retainAll(tempD2);//keep the intersections
		float intersectionSize = tempD1.size();//get the size
		tempD1 = null;
		tempD2 = null;
		return intersectionSize/(totalNum-intersectionSize);//divide the intersection size by the union size
	}

	/**
	 * Approximates the Jaccard by comparing the minHashs of the 2 documents and seeing if 
	 * they're equal
	 * @param d2
	 * @return the number of intersections divided by the number of hashes
	 */
	public double approximateJaccard(Document d2){
		double intersectionSize = 0;
		double result = 0;
		int tempMin1,tempMin2;
		for(int i = 0; i < minHashs.size(); i++){
			tempMin1 = minHashs.get(i);
			tempMin2 = d2.getMinHashs().get(i);
			if(tempMin1 == tempMin2){
				intersectionSize++;
			}
		}
		result = intersectionSize/minHashs.size();
		return result;
	}
}
