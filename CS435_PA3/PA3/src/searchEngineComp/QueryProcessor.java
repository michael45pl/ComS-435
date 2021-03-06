package searchEngineComp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import test.Graph;
import test.PageRank;

public class QueryProcessor {

	PositionalIndex index;
	
	public static void main(String args[]) throws IOException{
		String folderName = "IR";
	
		QueryProcessor q = new QueryProcessor(folderName);
		q.topKDocs("miss bad", 10);
	}

	public QueryProcessor(String folderName) throws IOException{
		this.index = new PositionalIndex(folderName);
	}

	public String[] topKDocs(String query, int k){
		String[] str = query.split(" ");
		String[] result = new String[k];
		HashMap<Double, ArrayList<String>> map2 = new HashMap<>();
		ArrayList<Document> docList = index.getDocsToCheck(str);
		
	
		System.out.println(index.postingList(str[0]));

		for(Document d : docList) {
			double val = index.Relevance(query, d.getFileName());
			if(map2.containsKey(val)) {
				map2.get(val).add(d.getFileName());
			}else {
				ArrayList<String> tempArr = new ArrayList<>();
				tempArr.add(d.getFileName());
				map2.put(val, tempArr);
			}
		}
		
		ArrayList<Double> temp = new ArrayList<>();
		
		temp.addAll(map2.keySet());
		
		Collections.sort(temp, Collections.reverseOrder());
		
		ArrayList<String> r = new ArrayList<>();
		int i =0;
		while(r.size() < k) {
			temp.get(i);
			r.addAll(map2.get(temp.get(i)));
			i++;
		}
		
		for(int j = 0; j < k; j++) {
			result[j] = r.get(j);
			String doc = result[j];
			double TPS = index.TPScore(query, doc);
			double VSS = index.VSScore(query, doc);
			double Rel = index.Relevance(query, doc);
			System.out.println(doc +", "+ TPS +", "+ 
					VSS +", "+ Rel);
		}
		
		return result;
		
	}
}

