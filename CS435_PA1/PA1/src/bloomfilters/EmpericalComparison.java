package bloomfilters;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import bloomfilters.BloomDifferential.BloomFilter;

public class EmpericalComparison {

	BloomDifferential bloomDifferential;
	NaiveDifferential naiveDifferential = new NaiveDifferential();
	BloomFilter bloomFilterDet;

	int[] bitsPerElement = {4, 8, 10};
	int number_of_hash_functions;
	int setSize = 1200000;
	double numb = Math.log(2) / Math.log(Math.E);

	String grams_file_path;
	Random generator;
	int number_of_queries;
	String[] querySet;

	EmpericalComparison(int number_of_queries) {

		grams_file_path = "src/Files/grams.txt";
		generator = new Random();
		this.number_of_queries = number_of_queries;
		querySet = new String[number_of_queries];

		number_of_hash_functions = (int) (numb * (double) bitsPerElement[1]);
		bloomFilterDet = bloomDifferential.createFilter();

		naiveDifferential = new NaiveDifferential();
	}

	void Run() {

		ReadGramsFile(grams_file_path, number_of_queries);

		long sum_for_bloom = 0, sum_for_naive = 0;
		long startTime, estimatedTime;

		String query;
		String result_from_bloomDifferential, result_from_naiveDifferential;
		System.out.println();

		for (int i = 0; i < number_of_queries; i++) {
			query = querySet[i];
			//System.out.println("Query number #" + (i + 1));

			startTime = System.currentTimeMillis();
			result_from_bloomDifferential = bloomDifferential.RetrieveRecord(query);
			//System.out.println("Retrieval of BloomDifferential: " + bloomDifferential.RetrieveRecord(query));
			estimatedTime = System.currentTimeMillis() - startTime;
			//System.out.println("Time spent: " + estimatedTime + " ms");
			sum_for_bloom += estimatedTime;
			//System.out.println();

			startTime = System.currentTimeMillis();
			result_from_naiveDifferential = naiveDifferential.RetrieveRecord(query);
			//System.out.println("Retrieval of NaiveDifferential: " + naiveDifferential.RetrieveRecord(query));
			estimatedTime = System.currentTimeMillis() - startTime;
			//System.out.println("Time spent: " + estimatedTime + " ms");
			sum_for_naive += estimatedTime;
			//System.out.println();
		}

		System.out.println("Number of queries: " + number_of_queries);
		System.out.println("Bits per element: " + bitsPerElement[1]);
		System.out.println(String.format("Average time (ms) for BloomDifferential: %.5f", (double) sum_for_bloom / number_of_queries));
		System.out.println(String.format("Average time (ms) for NaiveDifferential: %.5f", (double) sum_for_naive / number_of_queries));
	}

	void ReadGramsFile(String fileName, int number_of_queries) {

		String line = null;

		try {

			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			int index = 0;
			while((line = bufferedReader.readLine()) != null && number_of_queries > 0) {

				if (index == 0) {
					index = 100;
					querySet[--number_of_queries] = line;
				}
				index--;
			}   

			bufferedReader.close();
			fileReader.close();
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		}
		catch(IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
	}
}

