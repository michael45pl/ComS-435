package searchEngineComp;

import java.util.ArrayList;

public class Vertex {

	private String name;
	private double rank;
	private ArrayList<Vertex> nextVertices;
	
	public Vertex(String name) {
		this.setName(name);
		setEdges(new ArrayList<Vertex>());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getRank() {
		return rank;
	}

	public void setRank(double rank) {
		this.rank = rank;
	}

	public ArrayList<Vertex> getEdges() {
		return nextVertices;
	}

	public void setEdges(ArrayList<Vertex> nextVertices) {
		this.nextVertices = nextVertices;
	}
	
	public void addEdge(Vertex v) {
		nextVertices.add(v);
	}
	
	public int getNumEdges() {
		return nextVertices.size();
	}
	
	public int compareTo(Vertex v) {
		if(this.getRank() == v.getRank()) {
			return 0;
		}
		else {
			return this.getRank() < v.getRank() ? 1 : -1;
		}
		
	}
	
	
}
