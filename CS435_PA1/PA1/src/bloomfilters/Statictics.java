package bloomfilters;

public class Statictics {


	public static int estimateSetSize(BloomFilterFNV f) {
		int zeros = 0;
		int k = f.numHashes;//get k
		int m = f.filterSize;//get m
		for(int i = 0; i < m; i++) {
			if(!f.getBit(i)) {//check for zeros
				zeros++;
			}
		}

		return (int) Math.round((Math.log(zeros/m) / (k * Math.log(1-(1/m)))));
	}

	public static int estimateIntersectSize(BloomFilterFNV f1, BloomFilterFNV f2) {
		int zZeros = 0;
		int z1Zeros = 0;
		int z2Zeros = 0;
		int k = f1.numHashes;
		int m = f1.filterSize;
		for(int i = 0; i < m; i++) {
			if(!f1.getBit(i)) {//check if ith bit of f1 is zero
				z1Zeros++;
				if(!f2.getBit(i)) {//check if ith bit of f2 is zero
					z2Zeros++;//if so then both have zero
					zZeros++;
				}

			}else if(!f2.getBit(i)) {//check f2
				z2Zeros++;
			}
		}

		double z = (z1Zeros+z2Zeros-zZeros)/(z1Zeros*z2Zeros);
		return (int) (Math.log1p(z * m) / -k * Math.log1p(1-(1/m)));
	}
}
