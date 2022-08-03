package edu.undav.research.tinygarden;

import edu.undav.research.tinygarden.SpanningTreesMatsui.LABILogger;
import edu.undav.research.tinygarden.SpanningTreesMatsuiCollector.MinMaxCollector;

public class LowerBoundAnalysis {
 
	public static void main(String[] args) throws Exception {
		int nodeCnt = 8;
		SpanningTreesMatsui s;
		MinMaxCollector collector;
		LABILogger log = new LABILogger("C:/Users/manud/graph-data/stats/deg-" + nodeCnt + "-lower_bound.csv");
		for (int i = 1; i < 11118; i++) {
			String fName = "A" + i + ".mat";
			s = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
											"C:/Users/manud/graph-data/deg-"+nodeCnt+"/" + fName));			
			int n= s.getGraph().getNumberOfVertices();
			int m = s.getGraph().getNumberOfEdges();
			if (m > 2 * (n-1)) {
				collector = new MinMaxCollector(s, fName, log);
				s.allSpanningTrees();
				collector.postProcess();
			}
			
		}
		log.flush();
	}

/* 
	public static void main(String[] args) throws Exception {
		SpanningTreesMatsui s;
		MaxZerosCollector collector;
		s = new SpanningTreesMatsui(GraphTarjanRead.buildCompleteGraph(9));
		collector = new MaxZerosCollector(s, null, null);
		s.allSpanningTrees();
		collector.postProcess();
	}
*/

}