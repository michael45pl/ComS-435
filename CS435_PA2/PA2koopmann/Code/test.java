package multisets;

import java.io.IOException;
import java.util.ArrayList;

public class test {

	
	public static void main(String[] args) throws IOException {
		String folder = "/Users/michaelk/Desktop/LSHData";
		int n = 400;
		double s = 0.02;
		SimilarDocuments sd = new SimilarDocuments(folder, n, s);
		ArrayList<String> list = sd.similaritySearch("baseball2.txt");
		ArrayList<String> dup = new ArrayList<>();
		for(String str: list) {
			if(!dup.contains(str)) {
				dup.add(str);
				System.out.println(str);
			}
		}
	}

	
	
}
