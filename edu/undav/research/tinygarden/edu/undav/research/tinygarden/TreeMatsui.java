package edu.undav.research.tinygarden;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import edu.undav.research.tinygarden.BasicClasses.EdgeRelabelGraph;
import edu.undav.research.tinygarden.BasicClasses.Graph;
import edu.undav.research.tinygarden.BasicClasses.PartitionUnionFind;
import edu.undav.research.tinygarden.BasicClasses.SparseMatrixInt;
import edu.undav.research.tinygarden.BasicClasses.VecBool;
import edu.undav.research.tinygarden.BasicClasses.VecInt;


/*
 * Implementacion de Tree específico para Matsui
 * (generar todos los árbles generadores de un grafo conexo
 * Referencia:
 * 		"SpanningTreesMatsui.pdf"
 * */
public class TreeMatsui {
	private EdgeRelabelGraph _relabelGraph;
	private Graph _graph;
	private VecInt _treeEdges;
	private VecBool _availableEdges; //es necesario poder saber si un eje pertenece al árbol
	private int top;
	private int bottom;
	
	public TreeMatsui(EdgeRelabelGraph relabelGraph) {
		_relabelGraph = relabelGraph;
		_graph = relabelGraph.getGraph();
		_treeEdges = new VecInt(_graph.getNumberOfVertices()-1,-1);
		_availableEdges = new VecBool(_graph.getNumberOfEdges(),false);		

		//el árbol inicial son los labels 0..n-2
		for (int i = 0; i < _treeEdges.size(); i++) {
			_treeEdges.set(i,i);
			_availableEdges.set(_relabelGraph.getGraphIdx(i),true);
		}
		top = 0;
		bottom = _treeEdges.size() - 1;
	}
	
	//sustituye el indice de un eje con otro (para obtener un hijo)
	public void set(int idx, int newEdgeIdx) {
		//System.out.println("idx: " + idx + " newEdgeIdx: " + newEdgeIdx);
		int oldEdgeIdx = _treeEdges.get(idx);
		_availableEdges.set(_relabelGraph.getGraphIdx(oldEdgeIdx), false);
		_availableEdges.set(_relabelGraph.getGraphIdx(newEdgeIdx), true);
		_treeEdges.set(idx, newEdgeIdx);
		if (top == -1) { // es el primer elemento
			top = bottom = newEdgeIdx;
		} else {
			if (oldEdgeIdx == top || oldEdgeIdx == bottom) {
			// si el valor anterior es bottom o top hay que buscar los nuevos bottom y top
				top = bottom = -1;
				for (int i = 0; i < _treeEdges.size(); i++) {
					int value = _treeEdges.get(i);
					if (value >= 0) {
						if (value < top || top == -1)
							top = value;
						if (value > bottom || bottom == -1)
							bottom = value;
					}
				}
			}
			if (newEdgeIdx < top) //si el nuevo valor es top
				top = newEdgeIdx;
			if (newEdgeIdx > bottom) //si el nuevo valor es bottom
				bottom = newEdgeIdx;
		}
	}
	
	public VecInt getTreeEdges() {
		return _treeEdges;
	}

	public void setTreeEdges(VecInt treeEdges) {
		_treeEdges = treeEdges;
	}

	public int getTop() {
		return top;
	}

	public int getBottom() {
		return bottom;
	}

	//simplificacion de la funcion "label" que sugiere el paper
	public PartitionUnionFind label() throws Exception {
		PartitionUnionFind label = new PartitionUnionFind(_graph.getNumberOfVertices());
		for (int i = 0; i < _treeEdges.size(); i++) {
			int iE = _treeEdges.get(i);
			if (iE >= _treeEdges.size()) {
			// si es un indice > n-2 hay que agregar el eje
				int graphIdx = _relabelGraph.getGraphIdx(iE);
				label.join(_graph.getVertex0(graphIdx),_graph.getVertex1(graphIdx));
			}
		}
		return label;
	}
	
	private boolean findPath(int fromVertex, int toVertex, VecInt edgeList) {
		//System.out.println("fromVertex: " + fromVertex + " toVertex: " + toVertex);
		if (fromVertex == toVertex)
			return true;
		else {
			boolean found = false;
			VecInt vertexEdges = _graph.getVertexEdges(fromVertex);
			for (int i = 0; i < vertexEdges.size() && !found; i++) {
				int iE = vertexEdges.get(i);
				//System.out.println("fromVertex: " + fromVertex + " toVertex: " + toVertex + " iE: " + iE);
				if (_availableEdges.get(iE)) {
					//System.out.println("fromVertex: " + fromVertex + " toVertex: " + toVertex + " iE: " + iE + " entre");
					_availableEdges.set(iE, false);
					int neighbor = _graph.getNeighbor(fromVertex, iE);
					if (findPath(neighbor, toVertex, edgeList)) {
						//System.out.println("fromVertex: " + fromVertex + " toVertex: " + toVertex + " iE: " + iE + " entre 2");
						found = true;
						if (_relabelGraph.getLabel(iE) < _graph.getNumberOfVertices()-1) {
							//System.out.println("fromVertex: " + fromVertex + " toVertex: " + toVertex + " iE: " + iE + " entre 3");
							edgeList.pushBack(_relabelGraph.getLabel(iE));
						}
					}
					_availableEdges.set(iE, true);
				}
			}
			return found;
		}
	}
	
	//para un eje pivot e, devuelve la lista de los indices de _treeEdges 
	//que pueden sustituirse por e para obtener un hijo
	//si no devuelve una lista vacía
	public VecInt getCycleEdges(int iE) throws Exception {
		VecInt edgeList = new VecInt(2);
		int graphEdgeIdx = _relabelGraph.getGraphIdx(iE);
		findPath(_graph.getVertex0(graphEdgeIdx), _graph.getVertex1(graphEdgeIdx), edgeList);
		return edgeList;
	}
	
	public void dump() {
		System.out.println("top: " + top + " bottom: " + bottom);
		_graph.dump();
		_treeEdges.dump();
		_availableEdges.dump();
	}
	
	public void dumpGraphViz(String fileName) {
		boolean[] redEdges = new boolean[_graph.getNumberOfEdges()];
		for (int i = 0; i < _treeEdges.size(); i++) {
			redEdges[_relabelGraph.getGraphIdx(_treeEdges.get(i))] = true;
		}
		VecInt edges = _graph.getEdges();
		BufferedWriter writer = null;
        try {
            //create a temporary file
            //String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmssSS").format(Calendar.getInstance().getTime());
            File logFile = new File(fileName + ".tree");

            // This will output the full path where the file will be written to...
            //System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write("graph {\n");
            for (int i = 0; i < edges.size(); i+=2) {
            	if (redEdges[i/2]) {
            		writer.write("  " + edges.get(i) + " -- " + edges.get(i+1) + " [color=red]\n");		
            	} else {
            		writer.write("  " + edges.get(i) + " -- " + edges.get(i+1) + " [color=blue]\n");
            	}
            }            
            writer.write("}\n");
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

	public SparseMatrixInt labiMatrix() {
		BFSTreeRelabel bfs = new BFSTreeRelabel(_graph, _treeEdges, _relabelGraph);
		SparseMatrixInt labi = new SparseMatrixInt(_graph.getNumberOfEdges());
		boolean[] treeEdges = new boolean[_graph.getNumberOfEdges()];
		int edgeIdx = 0;
		for (int i = 0; i < bfs._treeEdges.size(); i++) {
			int iE = bfs._treeEdges.get(i);
			treeEdges[iE] = true;
			int iV0 = bfs._vertex2Label.get(_graph.getVertex0(iE));
			int iV1 = bfs._vertex2Label.get(_graph.getVertex1(iE));
			if (iV0 > iV1) {
				int aux = iV0;
				iV0 = iV1;
				iV1 = aux;
			}
			//matriz L
			labi.set(edgeIdx, iV0, -1);
			labi.set(edgeIdx, iV1, 1);
			edgeIdx++;
		}
		for (int i = 0; i < treeEdges.length; i++) {
			if (!treeEdges[i]) { // si es un loop-edge
				int iV0 = bfs._vertex2Label.get(_graph.getVertex0(i));
				int iV1 = bfs._vertex2Label.get(_graph.getVertex1(i));
				if (iV0 > iV1) {
					int aux = iV0;
					iV0 = iV1;
					iV1 = aux;
				}
				//matriz A
				labi.set(edgeIdx, iV0, -1);
				labi.set(edgeIdx, iV1, 1);
				//matriz I
				labi.set(edgeIdx, edgeIdx+1, 1);
				//matriz B
				while (iV0 != iV1) {
					if (iV0 < iV1) {
						labi.set(iV1-1, edgeIdx+1, -1);
						iV1 = bfs._parent.get(iV1);						
					} else {
						labi.set(iV0-1, edgeIdx+1, 1);
						iV0 = bfs._parent.get(iV0);												
					}
				}
				edgeIdx++;
			}
		}
		//labi.fullDump();
		//System.out.println("******************");
		return labi;
	}
	
	public SparseMatrixInt vertexDistances() {
		BFSTreeRelabel bfs = new BFSTreeRelabel(_graph, _treeEdges, _relabelGraph);
		SparseMatrixInt _vertexDistances = new SparseMatrixInt(_graph.getNumberOfVertices());
		for (int i = 0; i < _graph.getNumberOfVertices(); i++) {
			int iV0 = bfs._label2Vertex.get(i);
			VecInt neighbors = bfs._vertexNeighbors[iV0];
			for (int j = 0; j < neighbors.size(); j++) {
				int iV1 = neighbors.get(j);
				if (_vertexDistances.get(iV0, iV1) == 0) { //si es la primera vez (notar que hay dos veces por cada eje)
					for (int k = 0; k < _graph.getNumberOfVertices(); k++) {
						if (_vertexDistances.get(iV0, k) != 0) {
							_vertexDistances.set(k, iV1, _vertexDistances.get(iV0, k) + 1);
							_vertexDistances.set(iV1, k, _vertexDistances.get(iV0, k) + 1);
						}
					}
					_vertexDistances.set(iV0, iV1, 1);
					_vertexDistances.set(iV1, iV0, 1);
				}
			}
		}		
		return _vertexDistances;
	}
	
	public int labiCantZeros() {
		SparseMatrixInt labi = labiMatrix();
		int cantZeros = 0;
		SparseMatrixInt laplacian = labi.transpose().multiply(labi);
		//laplacian.toOctave();
		for (int i = _graph.getNumberOfVertices(); i < _graph.getNumberOfEdges()+1; i++) {
			for (int j = _graph.getNumberOfVertices(); j < _graph.getNumberOfEdges()+1; j++) {
				if (laplacian.get(i,j) == 0)
					cantZeros++;
			}	
		}
		//System.out.println("cantZeros: " + cantZeros);
		return cantZeros;
	}

	public int intersectionNumber() {
		SparseMatrixInt labi = labiMatrix();
		int cantNotZeros = 0;
		SparseMatrixInt laplacian = labi.transpose().multiply(labi);
		//laplacian.toOctave();
		for (int i = _graph.getNumberOfVertices(); i < _graph.getNumberOfEdges()+1; i++) {
			for (int j = i+1; j < _graph.getNumberOfEdges()+1; j++) {
				if (laplacian.get(i,j) != 0)
					cantNotZeros++;
			}	
		}
		//System.out.println("cantZeros: " + cantZeros);
		return cantNotZeros;
	}

	public int diameter() {
		SparseMatrixInt dist = vertexDistances();
		int diameter = 0;
		for (int i = 0; i < _graph.getNumberOfVertices(); i++) {
			for (int j = i+1; j < _graph.getNumberOfVertices(); j++) {
				if (dist.get(i, j) > diameter)
					diameter = dist.get(i, j);
			}
		}
		return diameter;
	}

	public int totalPathLength() {
		SparseMatrixInt dist = vertexDistances();
		int totalLen = 0;
		for (int i = 0; i < _graph.getNumberOfVertices(); i++) {
			for (int j = i+1; j < _graph.getNumberOfVertices(); j++) {
				totalLen += dist.get(i, j);
			}	
		}
		return totalLen;
	}

	//genera una histograma del largo de los ciclos que inducen los loop-edges en el árbol
	public int[] cycleHistogram() {
		int[] _histogram = new int[_graph.getNumberOfEdges()+1];
		SparseMatrixInt dist = vertexDistances();
		for (int i = 0; i < _availableEdges.size(); i++) {
			if (!_availableEdges.get(i)) { // los loop edges son los que están en false
				int iV0 = _graph.getVertex0(i);
				int iV1 = _graph.getVertex1(i);
				_histogram[dist.get(iV0, iV1)+1]++; //el "+1" es porque el ciclo mide 1 mas que el camino
			}
		}
		return _histogram;
	}
	
	public int cycleHistogramDigest() {
		int digest = 0;
		int[] _histogram = cycleHistogram();
		for (int i = 0; i < _histogram.length; i++) {
			digest += i * _histogram[i];
		}
		return digest;
	}

	public boolean isStar() {
		boolean match = false;
		VecInt[] _vertexNeighbors;
		_vertexNeighbors = new VecInt[_graph.getNumberOfVertices()];
		for (int i = 0; i < _treeEdges.size(); i++) {
			int iE = _relabelGraph.getGraphIdx(_treeEdges.get(i));
			int iV0 = _graph.getVertex0(iE);
			int iV1 = _graph.getVertex1(iE);
			if (_vertexNeighbors[iV0] == null) {
				_vertexNeighbors[iV0] = new VecInt(2);
			}
			if (_vertexNeighbors[iV1] == null) {
				_vertexNeighbors[iV1] = new VecInt(2);
			}
			_vertexNeighbors[iV0].pushBack(iV1);
			_vertexNeighbors[iV1].pushBack(iV0);
		}
		for(int i = 0; i < _vertexNeighbors.length && !match; i++) {
			if (_vertexNeighbors[i].size() == (_graph.getNumberOfVertices() - 1))
				match = true;
		}
		return match;
	}
	
	public static class BFSTreeRelabel {
		private Graph _graph;
		private VecInt[] _vertexNeighbors;
		public VecInt _treeEdges;
		public VecInt _vertex2Label;
		public VecInt _label2Vertex;
		public VecInt _parent;

		BFSTreeRelabel(Graph graph, VecInt treeEdges, EdgeRelabelGraph _relaRelabelGraph) {
			_graph = graph;
			_vertexNeighbors = new VecInt[_graph.getNumberOfVertices()];
			for (int i = 0; i < treeEdges.size(); i++) {
				int iE = _relaRelabelGraph.getGraphIdx(treeEdges.get(i));
				int iV0 = _graph.getVertex0(iE);
				int iV1 = _graph.getVertex1(iE);
				if (_vertexNeighbors[iV0] == null) {
					_vertexNeighbors[iV0] = new VecInt(2);
				}
				if (_vertexNeighbors[iV1] == null) {
					_vertexNeighbors[iV1] = new VecInt(2);
				}
				_vertexNeighbors[iV0].pushBack(iV1);
				_vertexNeighbors[iV1].pushBack(iV0);
			}
/*			for (int i = 0; i < _graph.getNumberOfVertices(); i++) {
				System.out.print("i: " + i + " ");
				_vertexEdges[i].dump();
			}
			System.out.println("**********************");
*/			build();
			//_label2Vertex.dump();
		}
		
		private void build() {
			_treeEdges = new VecInt(_graph.getNumberOfVertices() - 1);
			_vertex2Label = new VecInt(_graph.getNumberOfVertices(), -1);
			_vertex2Label.set(0,0);
			_label2Vertex = new VecInt(_graph.getNumberOfVertices(), 0);
			_parent = new VecInt(_graph.getNumberOfVertices(), 0);
			VecInt queue = new VecInt(_graph.getNumberOfVertices());
			queue.pushBack(0);
			int currentNodeIdx = 0;
			int currentLabel = 1;
			while (currentNodeIdx < _graph.getNumberOfVertices()) {
				//queue.dump();
				//System.out.println(currentNodeIdx);
				int iV = queue.get(currentNodeIdx);
				if (_vertexNeighbors[iV] != null) {
					for (int i = 0; i < _vertexNeighbors[iV].size(); i++) {
						int neighbor = _vertexNeighbors[iV].get(i);
						if (_vertex2Label.get(neighbor) == -1) {
							_vertex2Label.set(neighbor, currentLabel);
							_label2Vertex.set(currentLabel, neighbor);
							_parent.set(currentLabel, _vertex2Label.get(iV));
							currentLabel++;
							int iE = _graph.getEdge(iV, neighbor);
							_treeEdges.pushBack(iE);
							queue.pushBack(neighbor);
						}
					}
				}
				currentNodeIdx++;
			}
/*			_vertex2Label.dump();
			_label2Vertex.dump();
			_treeEdges.dump();
			_parent.dump();
			System.out.println("*************");
*/		}
	}

	public static void main(String[] args) throws Exception {
		TreeMatsui t;
		int n = 5;
		System.out.println("Complete Graphs");
		t = new TreeMatsui(new EdgeRelabelGraph(Graph.buildCompleteGraph(n)));
		//t = new TreeMatsui(new EdgeRelabelGraph(Graph.buildCycleGraph(n)));
		//t = new TreeMatsui(new EdgeRelabelGraph(Graph.buildCompleteBipartite(3,3)));
		//t.dump();
		//t.vertexDistances().dump();
		//t.vertexDistances().fullDump();
		//StringBuffer s = new StringBuffer();
		//int[] hist = t.cycleHistogram();
		//System.out.println(hist.length);
//		for (int i = 0; i < hist.length; i++) {
//			if (hist[i] != 0) {
//				s.append((s.length() > 0 ? "," : "") + i + "," + hist[i]);
//			}			
//		}
//		System.out.println(s);
		System.out.println(t.cycleHistogramDigest());
	}

		
/*
	public static void main(String[] args) throws Exception {
		TreeMatsui t;
		int n = 9; //9
		System.out.println("Complete Graphs");
		for (int i = 3; i < n; i++) {
			boolean ok = true;
			t = new TreeMatsui(new EdgeRelabelGraph(Graph.buildCompleteGraph(i)));
			for (int j = i-1; j < i*(i-1)/2 && ok; j++) {
				ok = ok && (t.getCycleEdges(j).size() == 2);
			}
			System.out.println(i + ": " + (ok ? "OK" : "ERROR"));
		}
		System.out.println("Cycle Graphs");
		for (int i = 3; i < n; i++) {
			t = new TreeMatsui(new EdgeRelabelGraph(Graph.buildCycleGraph(i)));
			System.out.println(i + ": " + (t.getCycleEdges(i-1).size() == i-1 ? "OK" : "ERROR"));
		}
		n = 5;
		int m = 7;
		System.out.println("Complete Bipartite Graphs");
		for (int i = 2; i < n; i++) {
			for (int j = i; j < m; j++) {
				boolean ok = true;
				t = new TreeMatsui(new EdgeRelabelGraph(Graph.buildCompleteBipartite(i,j)));
				for (int k = i+j-1; k < i*j; k++) {
					ok = ok && (t.getCycleEdges(k).size() == 3); //los ciclos en un bipartito miden 4
				}
				System.out.println("[" + i + "," + j + "]: " + (ok ? "OK" : "ERROR"));
			}
		}
	}
*/
/*
	public static void main(String[] args) throws Exception {
		Graph g = new Graph(5);
		g.insertEdge(0, 1);//0
		g.insertEdge(0, 2);//1
		g.insertEdge(0, 3);//2
		g.insertEdge(0, 4);//3
		g.insertEdge(1, 2);//4
		g.insertEdge(2, 3);//5
		g.insertEdge(3, 4);//6
		TreeMatsui t = new TreeMatsui(new EdgeRelabelGraph(g));
		t.dump();
		t.label().dump();
		t.getCycleEdges(4).dump();//[0,1]
		t.getCycleEdges(5).dump();//[1,2]
		t.getCycleEdges(6).dump();//[2,3]
		t.set(2,6);
		t.label().dump();
		t.getCycleEdges(2).dump();//[3,6]
	}
*/
}
