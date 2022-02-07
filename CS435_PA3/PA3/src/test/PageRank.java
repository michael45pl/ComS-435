package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;


public class PageRank {
	
	private Graph graph;
	
	public PageRank(Graph graph){
		this.graph = graph;
	}
	
	public double pageRankOf(String vertex){
		Vertex v = this.graph.retrieveVertex(vertex);
		return v.getRank();
	}
	
	public ArrayList<Vertex> topKPageRank(int k){
		TreeSet<Vertex> tsVertices = new TreeSet<Vertex>();
		for(Integer i:this.graph.getGraphMap().keySet()){
			tsVertices.add(this.graph.getGraphMap().get(i));
		}
		Iterator<Vertex> itr=tsVertices.iterator();
		int count=0;
		Vertex v;
		ArrayList<Vertex> topKPageRank = new ArrayList<Vertex>();
		while(count<k){
			v = itr.next();
			System.out.println(v.getName()+":"+v.getRank());
			topKPageRank.add(v);
			count++;
		}
		return topKPageRank;
	}
	
	public void pageRank(double beta,double sigma,Graph graph){
		boolean converge = false;
		Vertex v;
		double initRank = (double) (1.0/graph.getGraphSize());
		for(Integer i:graph.getGraphMap().keySet()){
			v = graph.getGraphMap().get(i);
			v.setRank(initRank);
		}
		double currentRank[] = new double[graph.getGraphSize()];
		double nextRank[] = new double[graph.getGraphSize()];
		int count = 0;
		while(!converge){
			currentRank = graph.getVertexRankVector(currentRank);
			singleRound(graph,beta);
			nextRank = graph.getVertexRankVector(nextRank);
			double sum = 0;
			
			for(int i=0;i<nextRank.length;i++){
				sum = sum + nextRank[i];
			}
		
//				System.err.println(sum);
			
			
			if(diffVertexVectors(currentRank,nextRank,sigma)){
				converge = true;
			}
			count++;
			
		}
		System.out.println("\nIt takes "+ count+" rounds to converge!!\n");
		
	}
	
	private void singleRound(Graph graph,double beta){
		
		Vertex v;
		Vertex w;
		double tempRank;
		int numOfVertice = graph.getGraphSize();
		double baseRank = (1-beta)/numOfVertice;
		double tempRanks[] = new double[numOfVertice];
		Arrays.fill(tempRanks, baseRank);
		
		double oldRanks[] = new double[numOfVertice];
		graph.getVertexRankVector(oldRanks);
		
		graph.setVertexRankVector(tempRanks);
		double vertexOldRank;
		int count = 0;
		for(Integer i:graph.getGraphMap().keySet()){
			v = graph.getGraphMap().get(i);
			vertexOldRank = oldRanks[count];
			if(v.getOutDegree()==0){
				tempRank = beta*vertexOldRank/numOfVertice;
				for(Integer j:graph.getGraphMap().keySet()){	
					w = graph.getGraphMap().get(j);
					w.setRank(w.getRank()+tempRank);
				}
			}else{
				tempRank = beta*vertexOldRank/v.getNextVertices().size();
				for(Vertex x:v.getNextVertices()){	
					x.setRank(x.getRank()+tempRank);
				}
			}
			count++;
		}
	} 
	
	private boolean diffVertexVectors(double[] v1,double[] v2,double sigma){
		boolean result = true;
		if(v1.length!=v2.length){
			System.err.println("Vectors have different length!!!");
			result = false;
		}else{
			for(int i=0;i<v1.length;i++){
				if(Math.abs(v1[i]-v2[i])>sigma){
					result = false;
					break;
				}
			}
		}
		return result;
	}

}