package bloomfilters;

import java.util.Random;

public class HashFunctionRan extends HashFunction{

	int a, b, p, filterSize;

	public HashFunctionRan(int filterSize) {
		Random r = new Random();
		this.filterSize = filterSize;
		this.p = getPrime(filterSize);//get prime
		this.a = r.nextInt(p-1);//get random
		this.b = r.nextInt(p-1);
	}

	public int hash(String s) {
		return hash(s.hashCode());
	}

	private int hash(int x) {
		int res = mod((a*x) + b, p);//perform equation
		if(res >= filterSize) {
			res = res%filterSize;
		}
		return res;
	}

	private int getPrime(int n) {
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

	private boolean isPrime(int a) {
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

}
