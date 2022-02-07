package test;

import java.io.IOException;
import java.math.BigDecimal;

import searchEngineComp.QueryProcessor;

public class delete {

	public static void main(String[] args) throws IOException {
		int freq = 2;
		int count = 6000;
		int N = 9000;
		double result = (Math.sqrt(freq) * Math.log10(N/count));
		System.out.println(result);
	}
}
