package edu.undav.research.tinygarden;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import edu.undav.research.tinygarden.BasicClasses.EdgeRelabelGraph;
import edu.undav.research.tinygarden.BasicClasses.Graph;
import edu.undav.research.tinygarden.BasicClasses.PartitionUnionFind;
import edu.undav.research.tinygarden.BasicClasses.VecInt;
import edu.undav.research.tinygarden.SpanningTreesMatsuiCollector.Collector;
import edu.undav.research.tinygarden.SpanningTreesMatsuiCollector.IntersectionNumberCollector;
import edu.undav.research.tinygarden.SpanningTreesMatsuiCollector.MaxZerosCollector;
import edu.undav.research.tinygarden.SpanningTreesMatsuiProcessor.Processor;



/*
 * Implementation of Matsui algorithm 
 * Generate spanning trees of a connected graph.
 * Reference:
 * T. Matsui, Algorithms for finding all the spanning trees in undirected graphs, 
 * METR93-08, Depart- ment of Mathematical Engineering and Information Physics, 
 * Faculty of Engineering, University of Tokyo (1993).
 * */
public class SpanningTreesMatsui {
 	private Graph _graph;
 	private TreeMatsui _tree;
 	private EdgeRelabelGraph _relabelGraph;
 	private VecInt _forbiddenEdges;  
 	private List<Collector> _collectors;
 	private List<Processor> _processors;
 	
	public SpanningTreesMatsui(Graph g) {
		_graph = g;
		//_graph.dump();
		_relabelGraph = new	 EdgeRelabelGraph(g);
		//System.out.println("*****************************************");
		//_relabelGraph.dump();
		_tree = new TreeMatsui(_relabelGraph);
		_forbiddenEdges = new VecInt(30); 
	}

	public void setCollector(Collector collector) {
		if (_collectors == null) {
			_collectors = new ArrayList<Collector>();
		}
		_collectors.add(collector);
	}

	public void setProcessor(Processor processor) {
		if (_processors == null) {
			_processors = new ArrayList<Processor>();
		}
		_processors.add(processor);
	}

 	public Graph getGraph() {
 		return _graph;
 	}
 	
 	public TreeMatsui getTree() {
 		return _tree;
 	}

	public VecInt getPivotEdges() throws Exception {
		VecInt pivotEdges = new VecInt(10);
		PartitionUnionFind label = _tree.label();
		for (int i = _tree.getBottom()+1; i < _graph.getNumberOfEdges(); i++) {
			int iE = _relabelGraph.getGraphIdx(i);
			if (label.find(_graph.getVertex0(iE)) != label.find(_graph.getVertex1(iE))) {
				pivotEdges.pushBack(i);
			}
		}
		return pivotEdges;
	}
	
	public VecInt getCycleEdges(int pivotEdge) throws Exception {
		return _tree.getCycleEdges(pivotEdge);
	}

	private void processSpanningTree() {
		if (_collectors != null)
			for (Collector c : _collectors) {
				c.processSpanningTree();
			}
		if (_processors != null)
			for (Processor p : _processors) {
				p.processSpanningTree();
			}
	}
/*
	private void processSpanningTree() {
		if (_treeCnt == 0) {
			SparseMatrixInt labi = _tree.labiMatrix(_fileName + "." + _treeCnt);
			int cantZeros = 0;
			SparseMatrixInt laplacian = labi.transpose().multiply(labi);
			laplacian.fullDump();
			for (int i = _graph.getNumberOfVertices(); i < _graph.getNumberOfEdges(); i++) {
				for (int j = _graph.getNumberOfVertices(); j < _graph.getNumberOfEdges(); j++) {
					if (laplacian.get(i,j) == 0)
						cantZeros++;
				}	
			}
			String line = _fileName + "," + _treeCnt + "," + _graph.getNumberOfVertices() + "," + 
							_graph.getNumberOfEdges() + "," + cantZeros;
			System.out.println(line);
		}
		_treeCnt++;
	}	
*/
	
/*
	private void processSpanningTree() {
		_tree.dumpGraphViz(_fileName + "." + _treeCnt);
		_treeCnt++;
	}	
*/
	
	public void setEdge(int idx, int newEdgeIdx) { 
		_tree.set(idx, newEdgeIdx);
	}

	public VecInt getTreeEdges() {
		return _tree.getTreeEdges();
	}
	
	public void allSpanningTrees() throws Exception {
		processSpanningTree();
		//_tree.getTreeEdges().dump();
		VecInt pivotEdges = getPivotEdges();
		//System.out.println(_call + " pivotEdges.size: " + pivotEdges.size());
		if (pivotEdges.size() > 0) {
			for (int i = 0; i < pivotEdges.size(); i++) {
				int pivotEdge = pivotEdges.get(i);
				VecInt cycleEdges = getCycleEdges(pivotEdge);
				//System.out.println(_call + " ANTES i: " + i + " pivotEdge: " + pivotEdge + " cycleEdges.size: " + cycleEdges.size());
				//pivotEdges.dump();
				//cycleEdges.dump();
				int _forbiddenEdgesCount = 0;
				for (int j = 0; j < cycleEdges.size(); j++) {
					int cycleEdge = cycleEdges.get(j); 
					if (!_forbiddenEdges.contains(cycleEdge)) {
						//System.out.println(_call + " entre!");
						//System.out.println(_call + " pivotEdge: " + pivotEdge + " cycleEdge: " + cycleEdge);
						_tree.set(cycleEdge, pivotEdge);
						allSpanningTrees();
						_tree.set(cycleEdge, cycleEdge);
						_forbiddenEdges.pushBack(cycleEdge); // ACA!!!
						_forbiddenEdgesCount++;
					}
				}
				//ACA debería liberar los forbidden edges de esta iteracion
				_forbiddenEdges.popBackN(_forbiddenEdgesCount);
			}
		}
	}
	
	public int getEdgeIdx(int relabelEdgeIdx) {
		return _relabelGraph.getGraphIdx(relabelEdgeIdx);
	}
	
	public void dump() {
	}

	public static int intersectionNumber(Graph g) throws Exception {
        SpanningTreesMatsui s;
        IntersectionNumberCollector collector;
        s = new SpanningTreesMatsui(g);
        collector = new IntersectionNumberCollector(s, null, null);
        s.allSpanningTrees();
        return collector.getIntersectionNumber();
    }

	public static class LABILogger {
		private String _logFile;
		private String[] _buffer;
		private static int LEN = 50;
		private int _lineNo;
		
		public LABILogger(String logFile) {
			_logFile = logFile;
			reset();
		}
		
		private void reset() {
            _buffer = new String[LEN];
            _lineNo = 0;			
		}
		
		public void flush() {
			BufferedWriter writer = null;
	        try {
	            writer = new BufferedWriter(new FileWriter(new File(_logFile), true));
	            for (int i = 0; i < LEN; i++) {
	            	if (_buffer[i] != null) 
	            		writer.write(_buffer[i] + "\n");
	            }
	            reset();
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                // Close the writer regardless of what happens...
	                writer.close();
	            } catch (Exception e) {
	            }
	        }
		}
		
		public void logLine(String line) {
			if (_lineNo == LEN) {
				flush();
			}			
			_buffer[_lineNo++] = line;
		}
	}
/*
	public static void main(String[] args) throws Exception {
		int nodeCnt = 8;
		SpanningTreesMatsui s;
		String path = "/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos/connected-graphs/stats/deg-" + nodeCnt;
		TreesFeaturesCollector collector;
		LABILogger log = new LABILogger(path + "/deg-" + nodeCnt + "-bestTreesFeatures.csv");
		FileReaderSimple fs = new FileReaderSimple(path + "/deg-" + nodeCnt + "-maxZerosTrees.csv");
		while (fs.hasItem()) {
			Item item = fs.nextItem();
			if (item.getData().size() > 0) {
				String fName = item.fileName();
				s = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
												"/home/manuel/20170817/doctorado/octave/tesis-octave/" + 
														"ejemplos/connected-graphs/deg-"+nodeCnt+"/" + fName));
				collector = new TreesFeaturesCollector(s, fName, log, item.getData());
				s.allSpanningTrees();
				collector.postProcess();
				int treeCnt = collector.getTreeCnt();
				System.out.println(fName + " - treeCnt: " + treeCnt);			
				
			}
			//System.out.println(item.toString());
		}
		log.flush();
	}
*/
/*
	public static void main(String[] args) throws Exception {
		int N = 5;
		int M = 3;
		int nodeCnt = N * M;
		String treesFileName = N + "_" + M + ".maxZerosTrees.csv";
		SpanningTreesMatsui s;
		String path = "/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos/connected-graphs/cuad-meshes";
		TreesFeaturesCollector collector;
		LABILogger log = new LABILogger(path + "/" + N + "_" + M + ".bestTreesFeatures.csv");
		FileReaderSimple fs = new FileReaderSimple(path + "/" + treesFileName);
		while (fs.hasItem()) {
			Item item = fs.nextItem();
			if (item.getData().size() > 0) {
				String fName = item.fileName();
				s = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
												path +"/" + fName));
				collector = new TreesFeaturesCollector(s, fName, log, item.getData());
				s.allSpanningTrees();
				collector.postProcess();
				int treeCnt = collector.getTreeCnt();
				System.out.println(fName + " - treeCnt: " + treeCnt);			
				
			}
			//System.out.println(item.toString());
		}
		log.flush();
	}
*/
/*
	public static void main(String[] args) throws Exception {
		int nodeCnt = 5;
		String fName = "A21.mat";
		SpanningTreesMatsui s = new SpanningTreesMatsui(fName, 
									GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
										"/home/manuel/20170817/doctorado/octave/tesis-octave/" + 
												"ejemplos/connected-graphs/deg-" + nodeCnt + "/" + fName));
		s.allSpanningTrees();
		int treeCnt = s.getTreeCnt();
		System.out.println(fName + " - treeCnt: " + treeCnt);			

	}
*/

/*
 *	5 -> 22 
 *  6 -> 113
 *  7 -> 854
 *  8 -> 11118
 *  9 -> 261080
 */
	public static void main(String[] args) throws Exception {
		int nodeCnt = 5;
		SpanningTreesMatsui s;
		MaxZerosCollector collector;
		LABILogger log = new LABILogger("/Users/admin/20200829/doctorado/tesis-octave/ejemplos/connected-graphs/stats/deg-" + nodeCnt + "/deg-" + nodeCnt + "-maxZeros.csv");
		for (int i = 1; i < 22; i++) {
			String fName = "A" + i + ".mat";
			s = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
											"/Users/admin/20200829/doctorado/tesis-octave/ejemplos/connected-graphs/deg-"+nodeCnt+"/" + fName));
			collector = new MaxZerosCollector(s, fName, log);
			s.allSpanningTrees();
			collector.postProcess();
			int treeCnt = collector.getTreeCnt();
			System.out.println(fName + " - treeCnt: " + treeCnt);			
		}
		log.flush();
	}

/*
	public static void main(String[] args) throws Exception {
		int nodeCnt = 9;
		SpanningTreesMatsui s;
		MaxZerosCollector collector;
		LABILogger log = new LABILogger("/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos/connected-graphs"+
				"/stats/deg-" + nodeCnt + "/deg-" + nodeCnt + "-maxZeros.sample.csv");
		String path = "/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos/connected-graphs/stats/deg-9";
		FileReaderSimple fs = new FileReaderSimple(path + "/deg-9-maxZeros.sample.bkup.csv");
		while (fs.hasItem()) {
			FileReaderSimple.Item item = fs.nextItem();
			String fName = item.fileName();
			s = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
											"/home/manuel/20170817/doctorado/octave/tesis-octave/" + 
													"ejemplos/connected-graphs/deg-"+nodeCnt+"/" + fName));
			collector = new MaxZerosCollector(s, fName, log);
			s.allSpanningTrees();
			collector.postProcess();
			int treeCnt = collector.getTreeCnt();
			System.out.println(fName + " - treeCnt: " + treeCnt);			
		}
		log.flush();
	}
*/
/*
	public static void main(String[] args) throws Exception {
		int N = 6;
		int nodeCnt = N;
		String fName = N + ".mat";
		SpanningTreesMatsui s;
		MaxZerosCollector collector;
		LABILogger log = new LABILogger("/home/manuel/20170817/doctorado/octave/tesis-octave/"+
											"ejemplos/connected-graphs/tri-meshes/"+ nodeCnt+".maxZeros.csv");		
		s = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
										"/home/manuel/20170817/doctorado/octave/tesis-octave/"+"" +
												"ejemplos/connected-graphs/tri-meshes/" + fName));
		collector = new MaxZerosCollector(s, fName, log);
		s.allSpanningTrees();
		collector.postProcess();
		int treeCnt = collector.getTreeCnt();
		System.out.println(fName + " - treeCnt: " + treeCnt);			
		log.flush();
	}
*/
	/*
	public static void main(String[] args) throws Exception {
		int N = 5;
		int M = 5;
		int nodeCnt = N * M;
		String fName = N + "_" + M + ".mat";
		SpanningTreesMatsui s;
		MaxZerosCollector collector;
		LABILogger log = new LABILogger("/home/manuel/20170817/doctorado/octave/tesis-octave/"+
											"ejemplos/connected-graphs/cuad-meshes/maxZeros.csv");		
		s = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
										"/home/manuel/20170817/doctorado/octave/tesis-octave/"+"" +
												"ejemplos/connected-graphs/cuad-meshes/" + fName));
		collector = new MaxZerosCollector(s, fName, log);
		s.allSpanningTrees();
		collector.postProcess();
		int treeCnt = collector.getTreeCnt();
		System.out.println(fName + " - treeCnt: " + treeCnt);			
		log.flush();
	}
*/
	
	/*
	 *	5 -> 22 
	 *  6 -> 113
	 *  7 -> 854
	 *  8 -> 11118
	 *  9 -> 261080
	 */
/*
	public static void main(String[] args) throws Exception {
		int nodeCnt = 8;
		SpanningTreesMatsui s;
		MaxZerosTrees collector;
		LABILogger log = new LABILogger("/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos/connected-graphs"+
											"/stats/deg-" + nodeCnt + "/deg-" + nodeCnt + "-maxZerosTrees.csv");
		for (int i = 1; i < 11118; i++) {
			String fName = "A" + i + ".mat";
			s = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
											"/home/manuel/20170817/doctorado/octave/tesis-octave/" + 
													"ejemplos/connected-graphs/deg-"+nodeCnt+"/" + fName));
			collector = new MaxZerosTrees(s, fName, log);
			s.allSpanningTrees();
			collector.postProcess();
			int treeCnt = collector.getTreeCnt();
			System.out.println(fName + " - treeCnt: " + treeCnt);			
		}
		log.flush();
	}
*/
/*
	public static void main(String[] args) throws Exception {
		int nodeCnt = 9;
		String path = "/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos"+
						"/connected-graphs/spanning_trees/pruebas/deg-" + nodeCnt + "/";
		SpanningTreesMatsui s;
		MaxZerosTrees collector;
		//int[] v = {0,1,0,2,0,3,0,4,0,5,0,6};
		int[] v = {0,1,0,2,0,3,0,4,0,5,0,6};
		Graph g = Graph.buildCompleteGraphExceptEdges(nodeCnt,v);
		s = new SpanningTreesMatsui(g);
		collector = new MaxZerosTrees(s, null, null);
		s.allSpanningTrees();
		
		Item item = new Item("deg" + nodeCnt);
		item.setData(collector.getMaxList());
		
		s = new SpanningTreesMatsui(g);
		TreeListProcessor processor = new TreeListProcessor(s, item, path);
		s.allSpanningTrees();
		
		
		//int treeCnt = collector.getTreeCnt();
		//System.out.println("treeCnt: " + treeCnt);
	}
*/
/*
	public static void main(String[] args) throws Exception {
		int nodeCnt = 9;
		String path = "/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos"+
						"/connected-graphs/spanning_trees/3DM-reduction/";
		SpanningTreesMatsui s;
		MaxZerosTrees collector;
		Graph g = Graph.buildReduction(2);
		s = new SpanningTreesMatsui(g);
		collector = new MaxZerosTrees(s, null, null);
		s.allSpanningTrees();
		
		System.out.println(collector.getMaxList().size());
		Item item = new Item("deg" + nodeCnt);
		item.setData(collector.getMaxList());
		
		s = new SpanningTreesMatsui(g);
		TreeListProcessor processor = new TreeListProcessor(s, item, path);
		s.allSpanningTrees();
		
		
		int treeCnt = collector.getTreeCnt();
		//System.out.println("treeCnt: " + treeCnt);
	}
*/
//	public static void main(String[] args) throws Exception {
//		String path = "/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos"+
//						"/connected-graphs/spanning_trees/simple-cycles/";
//		SpanningTreesMatsui s;
//		MaxZerosTrees collector;
//		Graph g = new Graph(7);
//		g.insertEdge(0,1);
//		g.insertEdge(0,2);
//		g.insertEdge(1,2);
//		g.insertEdge(1,4);
//		g.insertEdge(2,3);
//		g.insertEdge(3,4);
//		g.insertEdge(3,5);
//		g.insertEdge(3,6);
//		g.insertEdge(4,5);
//		g.insertEdge(4,6);
//		s = new SpanningTreesMatsui(g);
//		collector = new MaxZerosTrees(s, null, null);
//		s.allSpanningTrees();
//		
//		System.out.println("#total_trees: " + collector.getTreeCnt() + " #min_trees:" + collector.getMaxList().size());
//		Item item = new Item("prueba");
//		item.setData(collector.getMaxList());
//		
//		s = new SpanningTreesMatsui(g);
//		TreeListProcessor processor = new TreeListProcessor(s, item, path);
//		s.allSpanningTrees();
//		
//		
//		//int treeCnt = collector.getTreeCnt();
//		//System.out.println("treeCnt: " + treeCnt);
//	}
/*
	public static void main(String[] args) throws Exception {
		String path = "/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos"+
						"/connected-graphs/spanning_trees/petersen/";
		SpanningTreesMatsui s;
		MaxZerosTrees collector;
		Graph g = new Graph(10);
		g.insertEdge(0,1);
		g.insertEdge(0,4);
		g.insertEdge(0,5);
		g.insertEdge(1,2);
		g.insertEdge(1,6);
		g.insertEdge(2,3);
		g.insertEdge(2,7);
		g.insertEdge(3,4);
		g.insertEdge(3,8);
		g.insertEdge(4,9);
		g.insertEdge(5,7);
		g.insertEdge(5,8);
		g.insertEdge(6,8);
		g.insertEdge(6,9);
		g.insertEdge(7,9);
		s = new SpanningTreesMatsui(g);
		collector = new MaxZerosTrees(s, null, null);
		s.allSpanningTrees();
		
		System.out.println("#total_trees: " + collector.getTreeCnt() + " #min_trees:" + collector.getMaxList().size());
		Item item = new Item("prueba");
		item.setData(collector.getMaxList());
		
		s = new SpanningTreesMatsui(g);
		TreeListProcessor processor = new TreeListProcessor(s, item, path);
		s.allSpanningTrees();
		
		
		//int treeCnt = collector.getTreeCnt();
		//System.out.println("treeCnt: " + treeCnt);
	}
*/
/*
	public static void main(String[] args) throws Exception {
		int N = 6;
		int nodeCnt = N;
		String fName = N + ".mat";
		SpanningTreesMatsui s;
		LABILogger log = new LABILogger("/home/manuel/20170817/doctorado/octave/tesis-octave/"+
											"ejemplos/connected-graphs/tri-meshes/"+nodeCnt+".maxZerosTrees.csv");		
		s = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
										"/home/manuel/20170817/doctorado/octave/tesis-octave/"+"" +
												"ejemplos/connected-graphs/tri-meshes/" + fName));
		MaxZerosTrees collector = new MaxZerosTrees(s, fName, log);
		s.allSpanningTrees();
		collector.postProcess();
		int treeCnt = collector.getTreeCnt();
		System.out.println(fName + " - treeCnt: " + treeCnt);			
		log.flush();
	}
*/
/*
	public static void main(String[] args) throws Exception {
		int N = 5;
		int M = 3;
		int nodeCnt = N * M;
		String fName = N + "_" + M + ".mat";
		SpanningTreesMatsui s;
		LABILogger log = new LABILogger("/home/manuel/20170817/doctorado/octave/tesis-octave/"+
											"ejemplos/connected-graphs/cuad-meshes/maxZerosTrees.csv");		
		s = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
										"/home/manuel/20170817/doctorado/octave/tesis-octave/"+"" +
												"ejemplos/connected-graphs/cuad-meshes/" + fName));
		MaxZerosTrees collector = new MaxZerosTrees(s, fName, log);
		s.allSpanningTrees();
		collector.postProcess();
		int treeCnt = collector.getTreeCnt();
		System.out.println(fName + " - treeCnt: " + treeCnt);			
		log.flush();
	}
*/
	/*
	 *	5 -> 22 
	 *  6 -> 113
	 *  7 -> 854
	 *  8 -> 11118
	 *  9 -> 261080
	 */
/*
	public static void main(String[] args) throws Exception {
		int nodeCnt = 8;
		SpanningTreesMatsui s;
		DiameterHistogram c1;
		LABILogger l1 = new LABILogger("/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos/connected-graphs"+
				"/stats/deg-" + nodeCnt + "/deg-" + nodeCnt + "-diameterHistogram.csv");
		TotalPathLengthHistogram c2;
		LABILogger l2 = new LABILogger("/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos/connected-graphs"+
											"/stats/deg-" + nodeCnt + "/deg-" + nodeCnt + "-totalPathLenHistogram.csv");
		for (int i = 1; i < 11118; i++) {
			String fName = "A" + i + ".mat";
			s = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
											"/home/manuel/20170817/doctorado/octave/tesis-octave/" + 
													"ejemplos/connected-graphs/deg-"+nodeCnt+"/" + fName));
			c1 = new DiameterHistogram(s, fName, l1);
			c2 = new TotalPathLengthHistogram(s, fName, l2);
			s.allSpanningTrees();
			c1.postProcess();
			c2.postProcess();
			int treeCnt = c2.getTreeCnt();
			System.out.println(fName + " - treeCnt: " + treeCnt);			
		}
		l1.flush();
		l2.flush();
	}
*/
/*
	public static void main(String[] args) throws Exception {
		int N = 4;
		int M = 4;
		int nodeCnt = N * M;
		String fName = N + "_" + M + ".mat";
		SpanningTreesMatsui s;
		LABILogger log = new LABILogger("/home/manuel/20170817/doctorado/octave/tesis-octave/"+
											"ejemplos/connected-graphs/cuad-meshes/totalPathLengthHistogram.csv");		
		s = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
										"/home/manuel/20170817/doctorado/octave/tesis-octave/"+"" +
												"ejemplos/connected-graphs/cuad-meshes/" + fName));
		TotalPathLengthHistogram collector = new TotalPathLengthHistogram(s, fName, log);
		s.allSpanningTrees();
		collector.postProcess();
		int treeCnt = collector.getTreeCnt();
		System.out.println(fName + " - treeCnt: " + treeCnt);			
		log.flush();
	}
*/
/*
	public static void main(String[] args) throws Exception {
		int N = 5;
		int M = 3;
		int nodeCnt = N * M;
		String fName = N + "_" + M + ".mat";
		SpanningTreesMatsui s;
		LABILogger log = new LABILogger("/home/manuel/20170817/doctorado/octave/tesis-octave/"+
											"ejemplos/connected-graphs/cuad-meshes/cycleLenDigestHistogram.csv");		
		s = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
										"/home/manuel/20170817/doctorado/octave/tesis-octave/"+"" +
												"ejemplos/connected-graphs/cuad-meshes/" + fName));
		CycleLenDigestHistogram collector = new CycleLenDigestHistogram(s, fName, log);
		s.allSpanningTrees();
		collector.postProcess();
		int treeCnt = collector.getTreeCnt();
		System.out.println(fName + " - treeCnt: " + treeCnt);			
		log.flush();
	}
*/
/*
	public static void main(String[] args) throws Exception {
		int nodeCnt = 9;
		SpanningTreesMatsui s;
		DiameterHistogram c1;
		LABILogger l1 = new LABILogger("/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos/connected-graphs"+
				"/stats/deg-" + nodeCnt + "/deg-" + nodeCnt + "-diameterHistogram.csv");
		TotalPathLengthHistogram c2;
		LABILogger l2 = new LABILogger("/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos/connected-graphs"+
											"/stats/deg-" + nodeCnt + "/deg-" + nodeCnt + "-totalPathLenHistogram.csv");
		String path = "/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos/connected-graphs/stats/deg-9";
		FileReaderSimple fs = new FileReaderSimple(path + "/deg-9-maxZeros.sample.csv");
		while (fs.hasItem()) {
			FileReaderSimple.Item item = fs.nextItem();
			String fName = item.fileName();
			s = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
											"/home/manuel/20170817/doctorado/octave/tesis-octave/" + 
													"ejemplos/connected-graphs/deg-"+nodeCnt+"/" + fName));
			c1 = new DiameterHistogram(s, fName, l1);
			c2 = new TotalPathLengthHistogram(s, fName, l2);
			s.allSpanningTrees();
			c1.postProcess();
			c2.postProcess();
			int treeCnt = c2.getTreeCnt();
			System.out.println(fName + " - treeCnt: " + treeCnt);			
		}
		l1.flush();
		l2.flush();
	}
*/

/*
	public static void main(String[] args) throws Exception {
		int nodeCnt = 9;
		SpanningTreesMatsui s;
		MaxZerosTrees collector;
		LABILogger log = new LABILogger("/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos/connected-graphs"+
											"/stats/deg-" + nodeCnt + "/deg-" + nodeCnt + "-maxZerosTrees.csv");
		String path = "/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos/connected-graphs/stats/deg-9";
		FileReaderSimple fs = new FileReaderSimple(path + "/deg-9-maxZeros.sample.csv");
		while (fs.hasItem()) {
			FileReaderSimple.Item item = fs.nextItem();
			s = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
											"/home/manuel/20170817/doctorado/octave/tesis-octave/" + 
													"ejemplos/connected-graphs/deg-"+nodeCnt+"/" + item.fileName()));
			collector = new MaxZerosTrees(s, item.fileName(), log);
			s.allSpanningTrees();
			collector.postProcess();
			int treeCnt = collector.getTreeCnt();
			System.out.println(item.fileName() + " - treeCnt: " + treeCnt);
		}
		log.flush();
	}
*/

	//2550 2598
/*	public static void main(String[] args) throws Exception {
		int nodeCnt = 9;
		SpanningTreesMatsui s;
		MaxZerosTrees collector;
		LABILogger log = new LABILogger("/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos/connected-graphs"+
											"/stats/deg-" + nodeCnt + "/deg-" + nodeCnt + "-maxZerosTrees.csv");
		String path = "/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos/connected-graphs/stats/deg-9";
		FileSeeker fs = new FileSeeker(new int[] {2551,2552,2553,2554,2555,2556,2557,2558,2559,2560,2561,2562,
				2563,2564,2565,2566,2567,2568,2569,2570,2571,2572,2573,2574,2575,2576,2577,2578,2579,2580,2581,2582,
				2583,2584,2585,2586,2587,2588,2589,2590,2591,2592,2593,2594,2595,2596,2597,2598}, 
				path + "/deg-9-maxZeros.sample.csv");
		while (fs.hasItem()) {
			FileSeeker.Item item = fs.nextItem();
			s = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
											"/home/manuel/20170817/doctorado/octave/tesis-octave/" + 
													"ejemplos/connected-graphs/deg-"+nodeCnt+"/" + item.fileName()));
			collector = new MaxZerosTrees(s, item.fileName(), log);
			s.allSpanningTrees();
			collector.postProcess();
			int treeCnt = collector.getTreeCnt();
			System.out.println(item.fileName() + " - treeCnt: " + treeCnt);
		}
		log.flush();
	}
*/
/*
	public static void main(String[] args) throws Exception {
		int nodeCnt = 8;
		SpanningTreesMatsui stm;
		TreeListProcessor processor;
		String path = "/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos/connected-graphs";
		System.out.println(path);
		FileSeeker fs = new FileSeeker(new int[] {8464}, 
							path + "/stats/deg-" + nodeCnt + "/deg-" + nodeCnt + "-maxZerosTrees.csv");
		while (fs.hasItem()) {
			Item item = fs.nextItem();
			stm = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
					path + "/deg-"+nodeCnt+"/" + item.fileName()));
			processor = new TreeListProcessor(stm, item, path + "/spanning_trees/");
			stm.allSpanningTrees();
			System.out.println(processor.getTreeCnt() + "/" + item);
		}
	}
*/

/*
	public static void main(String[] args) throws Exception {
		int nodeCnt = 9;
		SpanningTreesMatsui stm;
		TreeListProcessor processor;
		String path = "/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos/connected-graphs/stats/deg-9";
		FileReaderSimple fs = new FileReaderSimple(path + "/deg-9-maxZeros.sample.csv");
		while (fs.hasItem()) {
			Item item = fs.nextItem();
			
			stm = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
					path + "/deg-"+nodeCnt+"/" + item.fileName()));
			processor = new TreeListProcessor(stm, item, path + "/spanning_trees/");
			stm.allSpanningTrees();
			System.out.println(processor.getTreeCnt() + "/" + item);
		}
	}
*/

/*
	public static void main(String[] args) throws Exception {
		int N = 6;
		int nodeCnt = N;
		String fName = N + ".maxZerosTrees.csv";
		SpanningTreesMatsui stm;
		TreeListProcessor processor;
		String path = "/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos/connected-graphs/tri-meshes";
		FileReaderSimple fs = new FileReaderSimple(path + "/" + fName);
		while (fs.hasItem()) {
			Item item = fs.nextItem();			
			stm = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
					path + "/" + item.fileName()));
			processor = new TreeListProcessor(stm, item, path + "/spanning_trees/");
			stm.allSpanningTrees();
			System.out.println(processor.getTreeCnt() + "/" + item);
		}
	}
*/
	
/*
	public static void main(String[] args) throws Exception {
		int N = 5;
		int M = 3;
		int nodeCnt = N * M;
		String fName = N + "_" + M + ".maxZerosTrees.csv";
		SpanningTreesMatsui stm;
		TreeListProcessor processor;
		String path = "/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos/connected-graphs/cuad-meshes";
		FileReaderSimple fs = new FileReaderSimple(path + "/" + fName);
		while (fs.hasItem()) {
			Item item = fs.nextItem();			
			stm = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
					path + "/" + item.fileName()));
			processor = new TreeListProcessor(stm, item, path + "/spanning_trees/");
			stm.allSpanningTrees();
			System.out.println(processor.getTreeCnt() + "/" + item);
		}
	}
*/

	/*
	public static void main(String[] args) throws Exception {
		int N = 4;
		int M = 4;
		int nodeCnt = N * M;
		String fName = N + "_" + M + ".maxZerosTrees.csv";
		SpanningTreesMatsui stm;
		LABIMatrixListProcessor processor;
		String path = "/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos/connected-graphs/cuad-meshes";
		FileReaderSimple fs = new FileReaderSimple(path + "/" + fName);
		while (fs.hasItem()) {
			Item item = fs.nextItem();			
			stm = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
					path + "/" + item.fileName()));
			processor = new LABIMatrixListProcessor(stm, item);
			stm.allSpanningTrees();
			System.out.println(processor.getTreeCnt() + "/" + item);
		}
	}
	*/
/*
	public static void main(String[] args) throws Exception {
		int nodeCnt = 8;
		SpanningTreesMatsui stm;
		LABIMatrixListProcessor processor;
		String path = "/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos/connected-graphs";
		System.out.println(path);
		FileSeeker fs = new FileSeeker(new int[] {8464}, 
							path + "/stats/deg-" + nodeCnt + "/deg-" + nodeCnt + "-maxZerosTrees.csv");
		while (fs.hasItem()) {
			Item item = fs.nextItem();
			stm = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
					path + "/deg-"+nodeCnt+"/" + item.fileName()));
			processor = new LABIMatrixListProcessor(stm, item);
			stm.allSpanningTrees();
			System.out.println(processor.getTreeCnt() + "/" + item);
		}
	}
*/
	
/*
	public static void main(String[] args) throws Exception {
		SpanningTreesMatsui s;
		long startTime = System.currentTimeMillis();		
		int n = 10;
		System.out.println("Complete Graphs");
		for (int i = 2; i < n; i++) {
			s = new SpanningTreesMatsui(GraphTarjanRead.buildCompleteGraph(i));
			s.allSpanningTrees();			
			int treeCnt = s.getTreeCnt();
			System.out.println(i + ": " + ((double)treeCnt == Math.pow((double)i, (double)i-2) ? "OK" : "ERROR") + " (" + treeCnt + ")");
		}
		System.out.println("Cycle Graphs");
		for (int i = 3; i < n; i++) {
			s = new SpanningTreesMatsui(GraphTarjanRead.buildCycleGraph(i));
			s.allSpanningTrees();			
			int treeCnt = s.getTreeCnt();			
			System.out.println(i + ": " + (treeCnt == i ? "OK" : "ERROR") + " (" + treeCnt + ")");
		}
		n = 5;
		int m = 7;
		System.out.println("Complete Bipartite Graphs");
		for (int i = 2; i < n; i++) {
			for (int j = i; j < m; j++) {
				s = new SpanningTreesMatsui(GraphTarjanRead.buildCompleteBipartite(i,j));
				s.allSpanningTrees();
				int treeCnt = s.getTreeCnt();
				System.out.println(i + ": " + j + 
						((double)treeCnt == Math.pow((double)i, (double)j-1) * 
						          Math.pow((double)j, (double)i-1) ? " OK" : " ERROR") + " (" + treeCnt + ")");
			}
		}
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("totalTime: " + totalTime);
	}
*/
/*
	public static void main(String[] args) throws Exception {
		int nodeCnt = 8;
		SpanningTreesMatsui s;
		MaxZerosTrees collector;
		String path = "/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos/connected-graphs/stats/deg-"+nodeCnt;
		LABILogger log = new LABILogger(path + "/star-" + nodeCnt + "-maxZerosTrees.csv");		
		FileReaderSimple fs = new FileReaderSimple(path + "/star_unique");
		while (fs.hasItem()) {
			Item item = fs.nextItem();
			s = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
											"/home/manuel/20170817/doctorado/octave/tesis-octave/" + 
													"ejemplos/connected-graphs/deg-"+nodeCnt+"/" + item.fileName()));
			collector = new MaxZerosTrees(s, item.fileName(), log);
			s.allSpanningTrees();
			collector.postProcess();
			int treeCnt = collector.getTreeCnt();
			System.out.println(item.fileName() + " - treeCnt: " + treeCnt);
		}
		log.flush();
	}
*/
/*
	public static void main(String[] args) throws Exception {
		int nodeCnt = 8;
		SpanningTreesMatsui s;
		String path = "/home/manuel/20170817/doctorado/octave/tesis-octave/ejemplos/connected-graphs/stats/deg-" + nodeCnt;
		StarTreeCheckerCollector collector;
		LABILogger log = new LABILogger(path + "/deg-" + nodeCnt + "-nonstar.txt");
		FileReaderSimple fs = new FileReaderSimple(path + "/star-" + nodeCnt + "-maxZerosTrees.csv");
		while (fs.hasItem()) {
			Item item = fs.nextItem();
			if (item.getData().size() > 0) {
				String fName = item.fileName();
				s = new SpanningTreesMatsui(GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
												"/home/manuel/20170817/doctorado/octave/tesis-octave/" + 
														"ejemplos/connected-graphs/deg-"+nodeCnt+"/" + fName));
				collector = new StarTreeCheckerCollector(s, fName, log, item.getData());
				s.allSpanningTrees();
				collector.postProcess();
				int treeCnt = collector.getTreeCnt();
				System.out.println(fName + " - treeCnt: " + treeCnt);							
			}
			//System.out.println(item.toString());
		}
		log.flush();
	}
*/
}
