package searchEngineComp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Graph {
 
	private String fileName;
	private int numVertices;
	private int numEdges;
	private HashMap<Integer, Vertex> graph;
	
	public Graph(String fileName) {
		this.fileName = fileName;
		this.graph = new HashMap<Integer, Vertex>();
		createGraph(this.fileName, this.graph);
	}
	
	private void createGraph(String fileName, HashMap<Integer, Vertex> graph) {
		FileInputStream in;
		BufferedReader br;
		try {
			in = new FileInputStream(fileName);
			br = new BufferedReader(new InputStreamReader(in));
			
			String str = br.readLine();
			numVertices = Integer.parseInt(str);
			Vertex curVertex;
			Vertex nextVertex;
			String[] nodes;
			int edges = 0;
			
			while((str = br.readLine()) != null) {
				nodes = str.split(" ");
				if(nodes.length < 2) {
					System.err.println("Need two vertices instead only had "+ str);
				}else {
					curVertex = createVertex(nodes[0]);
					nextVertex = createVertex(nodes[1]);
					curVertex.addEdge(nextVertex);
					edges++;
				}
			}
			numEdges = edges;
			br.close();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public Vertex createVertex(String name) {
		int key = name.hashCode();
		Vertex v;
		if(graph.containsKey(key)) {
			v = graph.get(key);
		}else {
			v = new Vertex(name);
			graph.put(key, v);
		}
		return v;
	}
	
	public Vertex getVertex(String name) {
		int key = name.hashCode();
		if(graph.containsKey(key)) {
			return graph.get(key);
		}
		else {
			System.err.println("No vertex with name " + name);
			return null;
		}
	}
	
	public double[] getVertexRanks(double[] P) {
		Vertex v;
		int n = 0;
		for(int key: graph.keySet()) {
			v = graph.get(key);
			P[n] = v.getRank();
			n++;
		}
		return P;
	}
	
	public void setVertexRanks(double[] P) {
		Vertex v;
		int n = 0;
		for(int key: graph.keySet()) {
			v = graph.get(key);
			v.setRank(P[n]);
			n++;
		}
	}
	
	public HashMap<Integer, Vertex> getGraph(){
		return graph;
	}
	
	public int getNumVertices() {
		return numVertices;
	}

	public int getNumEdges() {
		return numEdges;
	}
	
	
}
