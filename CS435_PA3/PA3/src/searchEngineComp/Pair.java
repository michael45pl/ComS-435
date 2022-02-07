package searchEngineComp;


	

public class Pair {

	Document doc;
	double rev;
	int rank;
	
	public Pair(Document doc, double rev) {
		this.doc = doc;
		this.rev = rev;
		this.rank = -1;
	}
	
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public int getRank() {
		return rank;
	}
	
	public String getName() {
		return doc.getFileName();
	}
	
	public double getRev() {
		return rev;
	}
}
