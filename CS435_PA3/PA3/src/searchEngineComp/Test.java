package searchEngineComp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import test.Graph;
import test.PageRank;

public class Test {
	
	public static void main(String args[]){
		
		HashMap<String, ArrayList<Integer>> termMap = new HashMap<>();
		String s ="hi";
		ArrayList<Integer> arr = new ArrayList<>();
		arr.add(1);
		termMap.put(s, arr);
	
		termMap.get(s).add(2);
		
		
		
		System.out.println(termMap.get(s));
	}
	

}
