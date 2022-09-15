package edu.undav.research.tinygarden;

import edu.undav.research.tinygarden.BasicClasses.Graph;
import edu.undav.research.tinygarden.BasicClasses.SparseMatrixInt;
import edu.undav.research.tinygarden.BasicClasses.VecInt;
import edu.undav.research.tinygarden.SpanningTreesMatsui.LABILogger;

public class SpanningTreesMatsuiCollector {
	public static interface Collector {
		public void processSpanningTree();
		public void postProcess();
	}

	public static abstract class CollectorBase implements Collector {
		protected SpanningTreesMatsui _stm;
		protected String _fileName;
		protected LABILogger _log;
		protected int _treeCnt;
		
		public CollectorBase(SpanningTreesMatsui stm, String fileName, LABILogger log) {
			_stm = stm;
			_fileName = fileName;
			_log = log;
			_stm.setCollector(this);
		}
		
		public int getTreeCnt() {
			return _treeCnt;
		}

	}

	public static class MinMaxCollector extends CollectorBase {
		
	 	private int maxIntersectionNumber;
	 	private int minIntersectionNumber;
	 	private int maxCount;
	 	private int minCount;

		public MinMaxCollector(SpanningTreesMatsui stm, String fileName, LABILogger log) {
			super(stm, fileName, log);
		}
		
		public void processSpanningTree() {
			int cantZeros = _stm.getTree().intersectionNumber();

			if (_treeCnt == 0) {
				maxIntersectionNumber = cantZeros;
				minIntersectionNumber = cantZeros;
				maxCount = 1;
				minCount = 1;
			} else {
				if (cantZeros > maxIntersectionNumber) {
					maxIntersectionNumber = cantZeros;
					maxCount = 1;
				} else if (cantZeros == maxIntersectionNumber) {
					maxCount++;
				}
				if (cantZeros < minIntersectionNumber) {
					minIntersectionNumber = cantZeros;
					minCount = 1;
				} else if (cantZeros == minIntersectionNumber) {
					minCount++;
				}
			}
			
			_treeCnt++;
		}

		public int getIntersectionNumber() {
			return minIntersectionNumber;
		}

		public void postProcess() {
			Graph g = _stm.getGraph();
			String line = _fileName + "," + 
							_treeCnt + "," + 
							g.getNumberOfEdges() + "," + 
							minIntersectionNumber + "," + 
							minCount + "," +
							maxIntersectionNumber + "," + 
							maxCount;
			if (_log != null) {
				_log.logLine(line);
			} else {
				System.out.println(line);
			}
		}
		
	}

	public static class IntersectionNumberCollector extends CollectorBase {		
		private int intersectionNumber;

	   public IntersectionNumberCollector(SpanningTreesMatsui stm, String fileName, LABILogger log) {
		   super(stm, fileName, log);
	   }
	   
	   public void processSpanningTree() {
		   int cantZeros = _stm.getTree().intersectionNumber();

		   if (_treeCnt == 0) {
			   intersectionNumber = cantZeros;
		   } else {
				if (cantZeros < intersectionNumber) {
				   intersectionNumber = cantZeros;
				}
		   }
		   
		   _treeCnt++;
	   }

	   public int getIntersectionNumber() {
		   return intersectionNumber;
	   }

	   public void postProcess() {
	   }
	   
   }

	/*
	 * summary for each graph:
	  		String line = _fileName,#edges,#spannig tree, max zeros in LABI matrix, 
						#spanning trees with max zeros in LABI matrix
	 */
	
	public static class MaxZerosCollector extends CollectorBase {
	 	private int maxCantZeros;
		 private int maxCantNotZeros;
	 	private int maxTreeCount;
	 	private VecInt maxList;

		public MaxZerosCollector(SpanningTreesMatsui stm, String fileName, LABILogger log) {
			super(stm, fileName, log);
		}
		
		public void processSpanningTree() {
			int cantNotZeros = _stm.getTree().intersectionNumber();
			
			if (_treeCnt == 0 || cantNotZeros < maxCantNotZeros) {
				//maxCantZeros = _stm.getTree().labiCantZeros();
				maxCantNotZeros = cantNotZeros;
				maxTreeCount = 1;
				maxList = new VecInt(2);
				maxList.pushBack(_treeCnt);
			} else if (cantNotZeros == maxCantNotZeros) {
				maxList.pushBack(_treeCnt);
				maxTreeCount++;
			}
			
			_treeCnt++;
		}

		public void postProcess() {
			Graph g = _stm.getGraph();
			float n = g.getNumberOfVertices();
			float m = g.getNumberOfEdges();
			float mu = m - n  + 1;
			float lowerBound = ((mu * mu)/(n-1)-mu) / 2;
			System.out.println((mu * mu));
			String line = "graph: " + _fileName + "," +  //filename
						"#edges: " + g.getNumberOfEdges() + "," + //edges 
						"#spanning trees: " + _treeCnt + "," + //spanning trees
						"#intersection-number: " + maxCantNotZeros + "," + //intersection number
						//"#null cycle intersections: " + maxCantZeros + "," +
						"#spanning trees with min cycle intersections: " + maxTreeCount + "," + //tree with min cycle intersection
						"lower-bound: " + lowerBound; // lower bound //+ "," + maxList.join(",");
			if (_log != null) {
				_log.logLine(line);
			} else {
				System.out.println(line);
			}
		}		
	}

	public static class MaxDegreeCollector extends CollectorBase {
		private int _intersectionNumber;
		private int degrees;

	   public MaxDegreeCollector(SpanningTreesMatsui stm, String fileName, 
	   								LABILogger log, int intersectionNumber) {
		   super(stm, fileName, log);
		   _intersectionNumber = intersectionNumber;
		   degrees = 0;
	   }
	   
	   public void processSpanningTree() {
			if (_stm.getTree().intersectionNumber() == _intersectionNumber) {
				if (_stm.getTree().maxTreeDegree() == _stm.getGraph().maxDegree()) {
					degrees++;
				}
			}
		   
		   _treeCnt++;
	   }

	   public void postProcess() {
		   if (_intersectionNumber > 0 && degrees == 0) {
				String line = "graph: " + _fileName + "," +  //filename
								"intersection number: " + _intersectionNumber + "," +
								"#distinct trees: " + degrees;
				if (_log != null) {
				_log.logLine(line);
				} else {
				System.out.println(line);
				}
			}
	   }		
   }

	//MaxZerosTrees, spanning trees with best score
	public static class MaxZerosTrees extends CollectorBase {
	 	private int maxCantZeros;
	 	private VecInt maxList;

		public MaxZerosTrees(SpanningTreesMatsui stm, String fileName, LABILogger log) {
			super(stm, fileName, log);
		}
		
		public void processSpanningTree() {
			int cantZeros = _stm.getTree().labiCantZeros();
			
			if (_treeCnt == 0 || cantZeros > maxCantZeros) {
				maxCantZeros = cantZeros;
				maxList = new VecInt(2);
				maxList.pushBack(_treeCnt);
			} else if (cantZeros == maxCantZeros) {
				maxList.pushBack(_treeCnt);
			}
			
			_treeCnt++;
		}

		public VecInt getMaxList() {
			return maxList;
		}
		
		public void postProcess() {
			String line = _fileName;
			if (maxCantZeros > 0) 
				line = line + "," + maxList.join(",");
			if (_log != null) {
				_log.logLine(line);
			} else {
				System.out.println(line);
			}
		}
		
	}

	public static class DiameterHistogram extends CollectorBase {
	 	private int[] _histogram;

		public DiameterHistogram(SpanningTreesMatsui stm, String fileName, LABILogger log) {
			super(stm, fileName, log);
			_histogram = new int[stm.getGraph().getNumberOfEdges()+1]; // el "+1" es para [0..N]
		}
		
		public void processSpanningTree() {
			int diameter = _stm.getTree().diameter();
			_histogram[diameter]++; 
			_treeCnt++;
		}

		public void postProcess() {			
			String line = _fileName;
			for (int i = 0; i < _histogram.length; i++) {
				if (_histogram[i] != 0) {
					line = line + "," + i + "," + _histogram[i];
				}				
			}
			if (_log != null) {
				_log.logLine(line);
			} else {
				System.out.println(line);
			}
		}
		
	}

	public static class CycleLenDigestHistogram extends CollectorBase {
	 	private int[] _histogram;

		public CycleLenDigestHistogram(SpanningTreesMatsui stm, String fileName, LABILogger log) {
			super(stm, fileName, log);
			// una cota superior surge de considerar que todos los ciclos inducidos tengan longitud
			// máxima. es decir: (m-n+1) * n (hay (m-n+1) loop-edges que generan esa cantidad de ciclos inducidos
			// y cada ciclo a lo sumo tiene n ejes)
			int upperBound = stm.getGraph().getNumberOfVertices() * 
									(stm.getGraph().getNumberOfEdges()-stm.getGraph().getNumberOfVertices()+1);
			_histogram = new int[upperBound];
		}
		
		public void processSpanningTree() {
			int cycleLenDigest = _stm.getTree().cycleHistogramDigest();
			_histogram[cycleLenDigest]++;
			_treeCnt++;
		}

		public void postProcess() {			
			String line = _fileName;
			for (int i = 0; i < _histogram.length; i++) {
				if (_histogram[i] != 0) {
					line = line + "," + i + "," + _histogram[i];
				}				
			}
			if (_log != null) {
				_log.logLine(line);
			} else {
				System.out.println(line);
			}
		}
		
	}

	// total sum of distances of pair of vertices in connected graph (is bounded from above): 
	// 	- Number of paths: totalPairs = (N,2)
	//	- Distance is at most: |V| - 1
	//	- Then <= totalPairs * (|V| - 1) = |V| * (|V| - 1) / 2 * (|V| - 1) =
	//		= |V| * (|V| - 1)^2 / 2
	public static class TotalPathLengthHistogram extends CollectorBase {
	 	private int[] _histogram;

		public TotalPathLengthHistogram(SpanningTreesMatsui stm, String fileName, LABILogger log) {
			super(stm, fileName, log);			
			int nodeCnt = stm.getGraph().getNumberOfVertices();
			_histogram = new int[nodeCnt * (nodeCnt-1) * (nodeCnt-1) / 2 + 1]; // "+ 1" para [0..cota_superior]
		}
		
		public void processSpanningTree() {
			SparseMatrixInt dist = _stm.getTree().vertexDistances();
			Graph g = _stm.getGraph();
			int totalDist = 0;
			for (int i = 0; i < g.getNumberOfVertices(); i++) {
				for (int j = i+1; j < g.getNumberOfVertices(); j++) {
					totalDist += dist.get(i, j);
				}	
			}
			_histogram[totalDist]++; 
			_treeCnt++;
		}

		public void postProcess() {			
			String line = _fileName;
			for (int i = 0; i < _histogram.length; i++) {
				if (_histogram[i] != 0) {
					line = line + "," + i + "," + _histogram[i];
				}				
			}
			if (_log != null) {
				_log.logLine(line);
			} else {
				System.out.println(line);
			}
		}
		
	}

	//Diameter a distance sum of a list of spanning trees
	public static class TreesFeaturesCollector extends CollectorBase {
	 	private VecInt _trees;
	 	private int _currIdx;
	 	private StringBuffer _result;
	 	
		public TreesFeaturesCollector(SpanningTreesMatsui stm, String fileName, LABILogger log, VecInt trees) {
			super(stm, fileName, log);			
			_trees = trees;
			_currIdx = 0;
			_result = new StringBuffer();
			_result.append(fileName);
		}
		
		public void processSpanningTree() {
			if (_currIdx < _trees.size() && _treeCnt == _trees.get(_currIdx)) {
				_result.append("," + _treeCnt + "#" +
								_stm.getTree().diameter() + "#" + 
								_stm.getTree().totalPathLength() + "#" +
								_stm.getTree().cycleHistogramDigest());
				_currIdx++;
			}
			_treeCnt++;
		}

		public void postProcess() {			
			if (_log != null) {
				_log.logLine(_result.toString());
			} else {
				System.out.println(_result.toString());
			}
		}
		
	}
	
	public static class StarTreeCheckerCollector extends CollectorBase {
	 	private VecInt _trees;
	 	private int _currIdx;
	 	private StringBuffer _result;
	 	private boolean _hasStarSpanningTree;
	 	
		public StarTreeCheckerCollector(SpanningTreesMatsui stm, String fileName, LABILogger log, VecInt trees) {
			super(stm, fileName, log);			
			_trees = trees;
			_currIdx = 0;
			_result = new StringBuffer();
			_result.append(fileName);
			_hasStarSpanningTree = false;
		}
		
		public void processSpanningTree() {
			if (_currIdx < _trees.size() && _treeCnt == _trees.get(_currIdx)) {
				if (_stm.getTree().isStar()) {
					_hasStarSpanningTree = true;
					System.out.println("match! treeCnt: " + _treeCnt + " cantZeros: " + _stm.getTree().labiCantZeros());
				}
				_currIdx++;
			}
			_treeCnt++;
		}

		public void postProcess() {
			if (!_hasStarSpanningTree) {
				if (_log != null) {
					_log.logLine(_result.toString());
				} else {
					System.out.println(_result.toString());
				}
			}
		}
		
	}


}
