package multisets;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;


public class HelperFunctions {

	/**
	 * Same function used in PA1 just increases the entered value accordingly then checks
	 * if that number is prime if so stops if not then repeats
	 * @param n the number we want to start our search from
	 * @return the next prime number
	 */
	static int nextPrime(int n) {
		boolean done = false;

		while(!done) {
			if(isPrime(n)) {
				done = true;
			}else {
				if(n == 1 || n % 2 == 0) {
					n = n + 1;
				}else {
					n = n + 2;
				}
			}
		}
		return n;
	}
	
	/**
	 * Same function used in PA1 just checks if the number is prime
	 * @param a the number to check
	 * @return true or false depending if a is prime
	 */
	private static boolean isPrime(int a) {
		if(a > 2 && a % 2 == 0) {//check divisible by 2
			return false;
		}
		int bound = (int) Math.sqrt(a) + 1;//get highest number that could be a factor
		for(int i = 3; i < bound; i+=2) {//start at 3 since checked 2 only check odd 
			if(a % i == 0) {//check number
				return false;
			}
		}
		return true;
	}

	
	/**
	 * Uses a random generator to get me 2 random values to use in my hashing function
	 * the (ax+b)%p function
	 * @param numPermutations
	 * @param numTerms
	 * @return a random int to be used for a and b
	 */
	public static ArrayList<ParameterPair> getParameters(int numPermutations, int numTerms) {
		ArrayList<ParameterPair> paraList = new ArrayList<ParameterPair>();
		Random randomGenerator = new SecureRandom();
		int tempA;
		int tempB;
		for(int i = 0; i < numPermutations; i++){
			tempA = randomGenerator.nextInt(numTerms-1);
			tempB = randomGenerator.nextInt(numTerms-1);
			paraList.add(new ParameterPair(tempA,tempB));
		}
		return paraList;
	}
	
}
