package bloomfilters;

public abstract class HashFunction {

	public abstract int hash(String s);

	public static int mod(long x, long y) {//mod function to insure that the hash is within the filter's bounds
		long result = x % y;
		if(result < 0) {
			result += y;
		}
		Long l = new Long(result);
		return l.intValue();
	}


}
