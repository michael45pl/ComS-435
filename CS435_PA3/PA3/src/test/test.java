package test;

public class test {

	public static void main(String args[]){

		String graphFileName = "wikiSportsGraph.txt";
		Graph graph = new Graph(graphFileName);
//		graph.printGraph();

		
		PageRank pr = new PageRank(graph);
		pr.pageRank((float)0.5, (float).001, graph);
		pr.topKPageRank(10);
	}
	
	
}
