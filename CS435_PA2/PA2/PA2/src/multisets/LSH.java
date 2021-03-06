package multisets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class LSH {

	private int[][] minHashMatrix;
	private int bands;
	private String[] docNames;
	private ArrayList<HashMap<String,TreeSet<String>>> hashTableList;
	
	
	/**
	 * 
	 * @param minHashMatrix
	 * @param docNames
	 * @param bands
	 */
	public LSH(int[][] minHashMatrix, String[] docNames, int bands) {
		this.minHashMatrix = minHashMatrix;
		this.bands = bands;
		this.docNames = docNames;
		this.hashTableList = new ArrayList<HashMap<String,TreeSet<String>>>();
		
		int numPermutations = this.minHashMatrix.length;
		int rows = numPermutations/this.bands;
		
		HashMap<String,TreeSet<String>> tempHashMap;
		StringBuffer tempSB;
		TreeSet<String> tempLinkSet = null;
		
		for(int i = 0; i < bands; i++){//create bands amount hash tables
			tempHashMap = new HashMap<String,TreeSet<String>>();//create temp hash table
			for(int j = 0; j < docNames.length; j++){//loop through all documents
				tempSB = new StringBuffer();
				for(int r = 0; r < rows; r++){//loop through all rows in the document
					tempSB.append(minHashMatrix[i*rows+r][j]);//store the hashvalue
					tempSB.append("$");
				}
				if(tempHashMap.containsKey(tempSB.toString())){//check if hashvalue is in the temp hash table
					tempLinkSet = tempHashMap.get(tempSB.toString());//if so store it as a link
				}else{
					tempLinkSet = new TreeSet<String>();//else create an empty set cause it has no links
				}
				tempLinkSet.add(docNames[j]);//add document checked
				tempHashMap.put(tempSB.toString(), tempLinkSet);
			}
			hashTableList.add(tempHashMap);//put the temp hash table into the rest
		}
		
		int leftRows = numPermutations - bands*rows;
		if(leftRows > 0){
			tempHashMap = new HashMap<String,TreeSet<String>>();
			for(int j = 0; j < docNames.length; j++){
				tempSB = new StringBuffer();
				for(int r = bands*rows;r<numPermutations;r++){
					tempSB.append(minHashMatrix[r][j]);
					tempSB.append("$");
				}
				if(tempHashMap.containsKey(tempSB.toString())){
					tempLinkSet = tempHashMap.get(tempSB.toString());
				}else{
					tempLinkSet = new TreeSet<String>();
				}
				tempLinkSet.add(docNames[j]);
				tempHashMap.put(tempSB.toString(), tempLinkSet);
			}
			hashTableList.add(tempHashMap);
		}
		
		
	}
	
	
	/**
	 * 
	 * @param docName
	 * @return
	 * @throws IOException 
	 */
	public ArrayList<String> retrieveSim(String docName) {
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> docList = new ArrayList<String>();
		
		for(String d: docNames) {
			docList.add(d);	
		}
		
		int numPermutations = this.minHashMatrix.length;
		int rows = numPermutations/this.bands;
		int currentBand = 0;
		for(HashMap<String,TreeSet<String>> hm: this.hashTableList){//loop through all the tables
			for(String s:hm.keySet()){
				if(hm.get(s).contains(docName)){//see if the document name were searching for is this table
					boolean isFalsePositive;
					int docNameIndex = docList.indexOf(docName);
					int fileName2Index;
					for(String fileName2:hm.get(s)){//loop through all the documents in the set
						isFalsePositive = false;
						fileName2Index = docList.indexOf(fileName2);
						for(int i = 0; i < rows; i++){
							if(currentBand*rows+i < numPermutations){
								if(this.minHashMatrix[currentBand*rows+i][docNameIndex]!=this.minHashMatrix[currentBand*rows+i][fileName2Index]){//
									isFalsePositive = true;
									break;
								}
							}
						}
						if(!isFalsePositive){
							result.addAll(hm.get(s));
						}else{
							System.err.println("False Positive!");
						}
					}
				}
			}
			currentBand++;
		}
		result.remove(docName);
		return result;
	}
	
}
