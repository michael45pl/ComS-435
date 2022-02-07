package test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Graph {
	
	private String graphFile;
	private HashMap<Integer,Vertex> graphMap;
	
	private static final int TOPKINDEGREE = 100;
	private static final int TOPKOUTDEGREE = 101;
	
	public Graph(String graphFile){
		this.graphFile = graphFile;
		this.graphMap = new HashMap<Integer,Vertex>();
		readGraph(this.graphFile,this.graphMap);
	}
	
	public int getGraphSize(){
		return this.graphMap.size();
	}
	
	private void readGraph(String fileName,HashMap<Integer,Vertex> graph){
		
		FileInputStream fstream;
		BufferedReader br;
		try {
			fstream = new FileInputStream(fileName);
			br = new BufferedReader(new InputStreamReader(fstream));

			String strLine = br.readLine();
			System.out.println("There are "+strLine+" vertices in the graph file.");
			//Read File Line By Line
			String vertices[];
			Vertex currentVertex;
			Vertex nextVertex;
			int tempDegree;
			while ((strLine = br.readLine()) != null)   {
	//		  System.out.println (strLine);
				vertices = strLine.split(" ");
				if(vertices.length<1){
					System.err.println("No two vertices in line "+strLine);
				}else{
					currentVertex = getVertex(vertices[0],graph);
					tempDegree = currentVertex.getOutDegree();
					tempDegree++;
					currentVertex.setOutDegree(tempDegree);
					nextVertex = getVertex(vertices[1],graph);
					tempDegree = nextVertex.getInDegree();
					tempDegree++;
					nextVertex.setInDegree(tempDegree);
					
					currentVertex.getNextVertices().add(nextVertex);
				}
			}
			//Close the input stream
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public HashMap<Integer, Vertex> getGraphMap() {
		return graphMap;
	}

	private Vertex getVertex(String key,HashMap<Integer,Vertex> graph){
		Vertex v;
		Integer hashCode = (Integer)key.hashCode();
		
		if(graph.containsKey(hashCode)){
			v = graph.get(hashCode);
		}else{
			v = new Vertex(key);
			graph.put(hashCode, v);
		}
		
		return v;
	}
	
	public Vertex retrieveVertex(String key){
		Vertex v = null;
		Integer hashCode = (Integer)key.hashCode();
		
		if(this.graphMap.containsKey(hashCode)){
			v = this.graphMap.get(hashCode);
		}else{
			System.err.println("Can't find "+ key);
		}
		
		return v;
	}
	
	public int outDegreeOf(String vertex){
		Vertex v = retrieveVertex(vertex);
		return v.getOutDegree();
	}
	public int inDegreeOf(String vertex){
		Vertex v = retrieveVertex(vertex);
		return v.getInDegree();
	}
	
	public int NumEdges(){
		Vertex v;
		int edgeCount = 0;
		for(Integer i:this.graphMap.keySet()){
			v = this.graphMap.get(i);
			edgeCount = edgeCount + v.getNextVertices().size();
		}
		return edgeCount;
	}
	
	public ArrayList<Vertex> topKInDegree(int k){
		ArrayList<Vertex> vertices = topK(k,TOPKINDEGREE);
		ArrayList<Vertex> kIndegree = new ArrayList<Vertex>();
		for(int i=0;i<k;i++){
			System.out.println(vertices.get(i));
			kIndegree.add(vertices.get(i));
		}
		return kIndegree;
	}
	public ArrayList<Vertex> topKOutDegree(int k){
		ArrayList<Vertex> vertices = topK(k,TOPKOUTDEGREE);
		ArrayList<Vertex> kOutdegree = new ArrayList<Vertex>();
		for(int i=0;i<k;i++){
			System.out.println(vertices.get(i));
			kOutdegree.add(vertices.get(i));
		}
		return kOutdegree;
	}
	
	public double[] getVertexRankVector(double[] rankVector){
		Vertex v;
		int count = 0;
		
		for(Integer i:this.graphMap.keySet()){
			v = this.graphMap.get(i);
			rankVector[count] = v.getRank();
			count++;
		}
		return rankVector;
	}
	
	public void setVertexRankVector(double[] rankVector){
		Vertex v;
		int count = 0;
		
		for(Integer i:this.graphMap.keySet()){
			v = this.graphMap.get(i);
			v.setRank(rankVector[count]);
			count++;
		}
	}
	
	private ArrayList<Vertex> topK(int k,int type){
		Vertex v;
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		for(Integer i:this.graphMap.keySet()){
			v = this.graphMap.get(i);
			vertices.add(v);
		}
		switch(type){
		case TOPKINDEGREE:
			Collections.sort(vertices,Vertex.inDegreeComparator);	
			break;
		case TOPKOUTDEGREE:
			Collections.sort(vertices,Vertex.outDegreeComparator);
			break;
			default:
				break;
		}
		return vertices;
	}
	
	public void printGraph(){
		System.out.println(this.toString());
	}
	
	@Override
	public String toString(){
		Vertex v;
		StringBuilder str = new StringBuilder();
		for(Integer i: this.graphMap.keySet()){
			v = this.graphMap.get(i);
			str.append(v.getName()+","+v.getInDegree()+","+v.getOutDegree()+"\n");
		}
		return str.toString();
	}
}

