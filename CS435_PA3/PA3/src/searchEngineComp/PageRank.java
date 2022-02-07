package searchEngineComp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PageRank {
	
	String fileName;
	Graph g;
	
	public static void main(String args[]) {
		String f = "wikiSportsGraph.txt";
		double b = .5;
		double e = .001;
		PageRank p = new PageRank(f, e, b);
		String[] pages = p.topKPageRank(10);
		System.out.println(p.g.getNumVertices());
		for(String s: pages) { 
			System.out.println(s);
		}
	}
	
	
	private PageRank(String fileName, double e, double B) {
		this.fileName = fileName;
		this.g = new Graph(fileName);
		setPageRanks(e, B, this.g);
	}
	
	public void setPageRanks(double approxPara, double telePara, Graph graph) {
		Vertex curr;
		double initialRank = (double) (1.0/graph.getNumVertices());
		HashMap<Integer, Vertex> gMap = graph.getGraph(); //step 1
		for(int key: gMap.keySet()) {//step 2
			curr = gMap.get(key);
			curr.setRank(initialRank);
		}
		int n = 0;//step 3
		double currentP[] = new double[gMap.size()];
		double nextP[] = new double[gMap.size()];
		boolean converged = false; //step 4
		while(!converged) {//step 5
			currentP = graph.getVertexRanks(currentP);
			runA(g, telePara);
			nextP = graph.getVertexRanks(nextP);
		
			converged = hasConverged(currentP, nextP, approxPara);
			n++;
		}
		System.out.println("Number of Iterations to converge was: " + n);
	}
	
	private void runA(Graph g, double B) {
		Vertex v;
		Vertex w;
		HashMap<Integer, Vertex> gMap = g.getGraph();
		double tempRank;
		int numOfVertice = g.getNumVertices();
		double baseRank = (1-B)/numOfVertice;
		double tempRanks[] = new double[numOfVertice];
		Arrays.fill(tempRanks, baseRank);
		
		double oldRanks[] = new double[numOfVertice];
		g.getVertexRanks(oldRanks);
		
		g.setVertexRanks(tempRanks);
		double vertexOldRank;
		int count = 0;
		
		for(Integer i : gMap.keySet()){
			v = gMap.get(i);
			vertexOldRank = oldRanks[count];
			if(v.getNumEdges() == 0){
				tempRank = B*vertexOldRank/numOfVertice;
				for(Integer j : gMap.keySet()){	
					w = gMap.get(j);
					w.setRank(w.getRank()+tempRank);
				}
			}else{
				tempRank = B*vertexOldRank/v.getNumEdges();
				for(Vertex x : v.getEdges()){	
					x.setRank(x.getRank()+tempRank);
				}
			}
			count++;
		}
	}
	
	private boolean hasConverged(double[] a, double[] b, double e) {
		if(a.length != b.length) {
			System.err.println("Vectors have differnt length");
			return false;
		}else {
			for(int i = 0; i < a.length; i++) {
				if(Math.abs(a[i]-b[i]) > e) {
					return false;
				}
			}
			return true;
		}
	}

	@SuppressWarnings("unused")
	private double pageRankOf(String v) {
		Vertex vertex = this.g.getVertex(v);
		return vertex.getRank();
	}
	
	@SuppressWarnings("unused")
	private int numEdges() {
		return this.g.getNumEdges();
	}
	

	private String[] topKPageRank(int k) {
		ArrayList<Vertex> temp = new ArrayList<Vertex>();
		String[] result = new String[k];
		HashMap<Integer, Vertex> gMap = g.getGraph();
		Vertex v;
		for(int key : gMap.keySet()){	
			v = gMap.get(key);
			if(temp.size() == 0) {
				temp.add(v);
			}
			else {
				int pos = 0;
				boolean added = false;
				for(Vertex w : temp) {
					if(v.getRank()>w.getRank()) {
						temp.add(pos, v);
						added = true;
						break;
					}
					pos++;
				}
				if(!added) {
					temp.add(v);
				}
			}
		}
		for(int i = 0; i < k; i++) {
			result[i] = temp.get(i).getName();
		}
		return result;
	}

}
