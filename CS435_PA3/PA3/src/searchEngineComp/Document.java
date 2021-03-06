package searchEngineComp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Document {


	private String filePath;
	private ArrayList<String> terms;
	public HashMap<String, Integer> termMap;
	public HashMap<String, ArrayList<Integer>> posting;

	/**
	 * Creates a Document from the given filePath.
	 * @param filePath
	 * @throws IOException
	 */
	public Document(String filePath) throws IOException{
		this.filePath = filePath;
		this.terms = new ArrayList<String>();
		//this.termMap = new HashMap<String, Integer> ();
		this.posting = new HashMap<String, ArrayList<Integer>>();
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

		String line;
		Pattern pattern = Pattern.compile("([A-Za-z]{3,})");//look for alphabetical characters size 3 or longer
		Matcher matcher;
		String tempString;
		int pos = 0;
		while ((line = br.readLine()) != null) {
			matcher = pattern.matcher(line);
			while(matcher.find()){
				tempString = matcher.group();
				tempString = tempString.toLowerCase();//convert to lower case
				if(!tempString.matches("(?i)the")){//make sure it not the
					if(!terms.contains(tempString)) {
						this.terms.add(tempString);
					}
					if(posting.containsValue(tempString)) {//check if string is in the termMap
						ArrayList<Integer> arrCopy = posting.get(tempString);
						arrCopy.add(pos);
						posting.put(tempString, arrCopy);
					}else {
						ArrayList<Integer> tempArr = new ArrayList<Integer>();
						tempArr.add(pos);
						posting.put(tempString, tempArr);
					}
				}
				pos++;
			} 
		}
		br.close();
	}

	public boolean contains(String t) {
		return terms.contains(t);
	}

	public int getFrequency(String t) {
		if(!contains(t)) {
			return 0;
		}
		return posting.get(t).size();
	}

	/**
	 * Returns ArrayList of terms from the document
	 * @return terms
	 */
	public ArrayList<String> getTerms() {
		return terms;
	}

	/**
	public HashMap<String, Integer> getTermMap() {
		return termMap;
	}**/

	public HashMap<String, ArrayList<Integer>> getPostings() {
		return posting;
	}

	public ArrayList<Integer> getPosting(String key) {
		return posting.get(key);
	}

	/**
	 * Returns a String of the filepath
	 * @return filePath
	 */
	public String getFilePath() {
		return filePath;
	}


	/**
	 * Returns the documents name
	 * @return
	 */
	public String getFileName(){
		File f = new File(this.filePath);
		String fName = f.getName();
		return fName;
	}
}
