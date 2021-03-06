package searchEngineComp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PositionalIndex {

	private String folderName;
	private HashMap<String, Integer> freq;
	private HashMap<String, ArrayList<Integer>> index;
	private HashMap<String, ArrayList<String>> posting;
	private ArrayList<Document> docList;
	private ArrayList<String> docNames;
	private int numOfDocs;

	public PositionalIndex(String folderName) throws IOException {
		this.folderName = folderName;
		this.freq = new HashMap<String, Integer>();
		this.index = new HashMap<String, ArrayList<Integer>>();
		this.posting = new HashMap<String, ArrayList<String>>();
		this.docList = new ArrayList<Document>();
		this.docNames = new ArrayList<String>();
		this.numOfDocs = -1;

		buildIndex(this.folderName);
	}


	private void buildIndex(String folderPath) throws IOException {
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();
		numOfDocs = listOfFiles.length;

		Document d;
		String tempString;
		ArrayList<String> tempStrList;
		for(int i = 0; i < numOfDocs; i++) {
			d = new Document(listOfFiles[i].getAbsolutePath());
			docList.add(d);
			docNames.add(d.getFileName());
			for(String s : d.getTerms()) {
				if(freq.containsKey(s)) {
					freq.put(s, freq.get(s) + 1);
				}else {
					freq.put(s, 1);
				}
				if(index.containsKey(s)) {
					index.get(s).add(i);
				}else {
					ArrayList<Integer> Positions = new ArrayList<Integer>();
					Positions.add(i);
					index.put(s, Positions);
				}
				ArrayList<Integer> tempPosting = d.getPosting(s);
				tempString = "{" + d.getFileName() + d.getPosting(s).toString();
				tempString = tempString.replace(" ", "").replace("[", " :").replace("]", "}");
				if(posting.containsKey(s)) {
					posting.get(s).add(tempString);
				}else {
					ArrayList<String> tempArr = new ArrayList<String>();
					tempArr.add(tempString);
					posting.put(s, tempArr);
				}

			}

		}

	}
	
	public ArrayList<Document> getDocsToCheck(String[] str){
		ArrayList<Document> result = new ArrayList<>();
		ArrayList<Integer> indexes = new ArrayList<>();
		for(String s : str) {
			ArrayList<Integer> temp = index.get(s);
			for(int x : temp) {
				if(!indexes.contains(x)) {
					indexes.add(x);
				}
			}
		}
		for(int i : indexes) {
			result.add(docList.get(i));
		}
		return result;
	}

	public ArrayList<Document> getDocList(){
		return docList;
	}

	public int termFrequency(String term, String Doc){
		int index = docNames.indexOf(Doc);
		Document d = docList.get(index);
		return d.getFrequency(term);
	}

    public int docFrequency(String term) {
		return freq.get(term);
	}

	public String postingList(String t) {
		String tempStr = String.join(",", posting.get(t));
		return tempStr;
	}

	public double weight(String t, String d) {
		double freq = termFrequency(t, d);
		double count = docFrequency(t);
		double N = numOfDocs;
		double result = (Math.sqrt(freq) * Math.log10(N/count));
		return result;
	}

	public double TPScore(String query, String doc) {
		int index = docNames.indexOf(doc);
		Document d = docList.get(index);
		String[] Ts = query.split(" ");
		double l = Ts.length;
		double sum = 0;
		if(l == 1) {
			return 0;
		}
		else {
			for(int i = 0; i < l-1; i++) {
				String t1 = Ts[i];
				String t2 = Ts[i+1];
				ArrayList<Integer> Ps = d.getPosting(t1);
				ArrayList<Integer> Rs = d.getPosting(t2);
				double min = 17;
				if(Rs == null || Ps == null) {
					sum += min;
					continue;
				}
				for(int r : Rs) { 
					for(int p : Ps) {
						if(r>p) {
							double temp = r - p;
							if(temp < min) {
								min = temp;
							}
						}
					}
				}
				sum += min;
			}
		}

		return l / sum;
	}

	private static double cosineSim(double[] vectorA, double[] vectorB) {
		double result = 0.0;
		double A = 0.0;
		double B = 0.0;
		for (int i = 0; i < vectorA.length; i++) {
			result += vectorA[i] * vectorB[i];
			A += Math.pow(vectorA[i], 2);
			B += Math.pow(vectorB[i], 2);
		}   
		result = result / (Math.sqrt(A) * Math.sqrt(B));
		return result;
	} 

	public double VSScore(String query, String doc) {
		String[] Ts = query.split(" ");
		int l = Ts.length;
		double[] Vq = new double[l];
		double[] Vd = new double[l];

		for(int i = 0; i < l; i++) {
			Vq[i] = queryWeight(Ts[i], Ts);
			Vd[i] = weight(Ts[i], doc);
		}

		double result = cosineSim(Vq, Vd);
		if(Double.isNaN(result)) {
			result = 0;
		}
		return result;
		
	}

	private double queryWeight(String t, String[] query) {
		double freq = 0;
		for(String s : query) {
			if(s.equals(t)) {
				freq++;
			}
		}
		return freq;
	}

	public double Relevance(String query, String doc) {
		double tp = (.6*TPScore(query, doc));
		double vs = (.4*VSScore(query, doc));
		double result = tp+vs;
		return result;
	}

}

