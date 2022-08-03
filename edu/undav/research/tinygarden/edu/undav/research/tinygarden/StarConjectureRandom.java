package edu.undav.research.tinygarden;

import java.util.Random;

import edu.undav.research.tinygarden.BasicClasses.Graph;
import edu.undav.research.tinygarden.BasicClasses.SparseMatrixInt;
import edu.undav.research.tinygarden.BasicClasses.VecBool;
import edu.undav.research.tinygarden.BasicClasses.VecInt;


/*
/*
 * Reference:
 * M. Dubinsky, C. Massri, G. Taubin, Minimum Spanning Tree Cycle Intersection problem, 
 * Discrete Applied Mathematics 294 (2021), 152-166 
 * 
generate random tree of a certain number of nodes
	foreach node w
		create a graph as follows:
			 insert node v (which will be the universal vertex of the star spanning tree)
			 	* connect v-w
			 	* traverse BFS from v to identify the branches
			 	* create a set C of forbidden edgess
			 	* foreach branch
			 	* 	insert all the cycle edges corresponding to the branch and 
			 	* 		add the cycle edges to C
			 	*   insert the cycle edges that connect each node to v
			 	*   foreach combination of forbidden edges 
			 	*   	evalate the intersection number 
		
*/

public class StarConjectureRandom {
	Graph _tree;
	int _starRoot;
	VecInt _parent;
	VecInt _nonInterbranchCycleEdges;
	VecInt _starEdges;
	VecInt _bfsLabels;
	VecInt[] _treeEdgesCycles;
	int _nV;
	int _K; //cantidad de instancias
	Random _generator;
	
	public StarConjectureRandom(int nV, int K) {
		_nV = nV;
		_K = K;
	}

	private void buildParents(Graph g) {
	//BFS desde la raiz de la estrella
		int nV = g.getNumberOfVertices();
		int queueIdx = 0;
		int labelIdx = 0;
		VecInt queue = new VecInt(nV);
		queue.pushBack(_starRoot);		
		_parent = new VecInt(nV, 0);
		_parent.set(_starRoot, -1); //setear el parent de la raiz del arbol en -1
		_bfsLabels = new VecInt(nV, 0);
		_bfsLabels.set(_starRoot, labelIdx++);
		VecBool visited = new VecBool(nV, false);
		visited.set(_starRoot, true);
		//System.out.println("ACA <0> " + _label);
		while (queue.size() < nV) {
			int parent = queue.get(queueIdx++);
			VecInt vertexEdges = g.getVertexEdges(parent);
			for (int j = 0; j < vertexEdges.size(); j++) {
				//System.out.println("ACA <1." + j + ">");
				int edge = vertexEdges.get(j);
				int child = g.getNeighbor(parent, edge);
				if (!visited.get(child)) {
					_parent.set(child, parent);
					_bfsLabels.set(child, labelIdx++);
					queue.pushBack(child);
					visited.set(child, true);
				}
			}
		}
	}
	
	private void addCycleEdges(double lambda, Graph g) {
		_starEdges = new VecInt(20);
		_nonInterbranchCycleEdges  = new VecInt(20);
		
		// los ejes de la estrella
		for (int iV = 0; iV < g.getNumberOfVertices(); iV++) {
			if (iV != _starRoot) { 
				int iE = g.insertEdge(_starRoot, iV);
				if (iE != -1) {
					_starEdges.pushBack(iE);
				}
			}
		}

		// non interbranch cycle edges
		for (int i = 0; i < g.getNumberOfVertices(); i++) {
			if (i != _starRoot) {
				int ancestor = _parent.get(i);
				while (ancestor != _starRoot) {
					if (_generator.nextDouble() < lambda) {
						int iE = g.insertEdge(i, ancestor);
						if (iE != -1) {
							_nonInterbranchCycleEdges.pushBack(iE);
						}
					}
					ancestor = _parent.get(ancestor);
				}
			}
		}
	}

	private void setTreeCycleEdges(Graph g, int cycleEdge) {
		int iV0 = g.getVertex0(cycleEdge); int iV0Label = _bfsLabels.get(iV0);
		int iV1 = g.getVertex1(cycleEdge); int iV1Label = _bfsLabels.get(iV1);
		while (iV0Label != iV1Label) {
			int parent = -1; int iE = -1;
			if (iV0Label < iV1Label) {
				parent = _parent.get(iV1);
				iE = g.getEdge(parent, iV1);
				iV1 = parent;
				iV1Label = _bfsLabels.get(iV1);
			} else {
				parent = _parent.get(iV0);
				iE = g.getEdge(parent, iV0);
				iV0 = parent;
				iV0Label = _bfsLabels.get(iV0);
			}
			if (_treeEdgesCycles[iE] == null) {
				_treeEdgesCycles[iE] = new VecInt(2);
			}
			_treeEdgesCycles[iE].pushBack(cycleEdge);
		}
	}

	private int checkStarFormula(Graph g) {
		VecInt[] treeEdgesCycles = new VecInt[g.getNumberOfEdges()];
		
		for (int iE = 0; iE < g.getNumberOfEdges(); iE++) {
			int iV0 = g.getVertex0(iE);
			int iV1 = g.getVertex1(iE);
			if (iV0 != _starRoot && iV1 != _starRoot) { // si es un cycle edge en la estrella
				int treeEdge0 = g.getEdge(iV0, _starRoot);
				int treeEdge1 = g.getEdge(iV1, _starRoot);
				if (treeEdgesCycles[treeEdge0] == null) {
					treeEdgesCycles[treeEdge0] = new VecInt(2);
				}
				treeEdgesCycles[treeEdge0].pushBack(iE);
				if (treeEdgesCycles[treeEdge1] == null) {
					treeEdgesCycles[treeEdge1] = new VecInt(2);
				}
				treeEdgesCycles[treeEdge1].pushBack(iE);
			}				
		}

		//completar matriz de interseccion de ciclos
		SparseMatrixInt cycleIntersect = new SparseMatrixInt(g.getNumberOfEdges());
		for (int i = 0; i < treeEdgesCycles.length; i++) {
			VecInt cycles = treeEdgesCycles[i];
			if (cycles != null) {
				//System.out.println("ACA i: " + i);
				//cycles.dump();

				for (int j = 0; j < cycles.size(); j++) {
					for (int k = j+1; k < cycles.size(); k++) {
						cycleIntersect.add(cycles.get(j), cycles.get(k), 1);
						cycleIntersect.add(cycles.get(k), cycles.get(j), 1);
					}
				}
			}
		}

		// contar intersecciones
		int intersectCount = 0;
		for (int i = 0; i < cycleIntersect.getRows(); i++) {
			for (int j = i+1; j < cycleIntersect.getCols(); j++) {
				if (cycleIntersect.get(i,j) > 0) {
					intersectCount++;
				}
			}
		}

		return intersectCount;
	}
	
	private boolean check(Graph g) throws Exception {
		_treeEdgesCycles = new VecInt[g.getNumberOfVertices() - 1];

		//non interbranch cycle edges
		for (int i = 0; i < _nonInterbranchCycleEdges.size(); i++) {
			int iE = _nonInterbranchCycleEdges.get(i);
			setTreeCycleEdges(g, iE);			
		}
		
		//star cycle edges
		for (int i = 0; i < _starEdges.size(); i++) {
			setTreeCycleEdges(g, _starEdges.get(i));
		}
		
		//completar matriz de interseccion de ciclos
		SparseMatrixInt cycleIntersect = new SparseMatrixInt(g.getNumberOfEdges());
		for (int i = 0; i < _treeEdgesCycles.length; i++) {
			VecInt cycles = _treeEdgesCycles[i];
			if (cycles != null) {
				for (int j = 0; j < cycles.size(); j++) {
					for (int k = j+1; k < cycles.size(); k++) {
						cycleIntersect.add(cycles.get(j), cycles.get(k), 1);
						cycleIntersect.add(cycles.get(k), cycles.get(j), 1);
					}				
				}
			}
		}

		// contar intersecciones
		int intersectCount = 0;
		for (int i = 0; i < cycleIntersect.getRows(); i++) {
			for (int j = i+1; j < cycleIntersect.getCols(); j++) {
				if (cycleIntersect.get(i,j) > 0) {
					intersectCount++;
				}
			}
		}

		int star = starTreeIntersect(g);
		int starCheck = checkStarFormula(g);
		if (star != starCheck) {
			System.out.println(" star: " + star + " starCheck: " + starCheck);
			g.dump();
			throw new Exception("star != starCheck");
		}
		//System.out.println(star + " <=? " + intersectCount);
		return star <= intersectCount;
	}
	
	private int starTreeIntersect(Graph g) {
		int nV = g.getNumberOfVertices();
		int nE = g.getNumberOfEdges();
		VecInt degrees = new VecInt(g.getDegrees());		
		return (degrees.squareNorm()-6*nE-(nV-1) * (nV - 6)) / 2;
	}

	private Graph generateRandomTree(int n) {		
		Graph tree = new Graph(n);
		VecInt treeNodes = new VecInt(n);
		treeNodes.pushBack(0);
		VecBool connectedNodes = new VecBool(n, false);
		connectedNodes.set(0, true);
		while (treeNodes.size() < n) {
			int nextNode = _generator.nextInt(n);
			while (connectedNodes.get(nextNode))
				nextNode = (nextNode+1) % n;
			int neighbor = treeNodes.get(_generator.nextInt(treeNodes.size()));
			tree.insertEdge(nextNode, neighbor);
			treeNodes.pushBack(nextNode);
			connectedNodes.set(nextNode, true);
		}		
		return tree;
	}

	public void process() throws Exception {
		_generator = new Random();
		for (int i = 0; i < _K; i++) {
//			if (i % 10 == 0) {
//				System.out.println(i);
//			}			
		// Generate random tree
			Graph g = generateRandomTree(_nV-1);
			//g.dump();
		// Add the universal vertex and conect it to the tree
			int _starRoot = g.insertVertex();			
			int rootNeighbor = _generator.nextInt(_nV-1);
			g.insertEdge(_starRoot, rootNeighbor);
		// Set parents of the tree T
			buildParents(g);
			double[] sparsity = new double[]{0.1,0.5,0.9};
		// Add edges for each branch
			for (int j = 0; j < sparsity.length; j++) {
				double lambda = sparsity[j];
				Graph copy = g.clone();
				addCycleEdges(lambda, copy);
				//System.out.println("lambda: " + lambda + " #edges: " + copy.getNumberOfEdges());
				if (!check(copy)) {
					System.out.println("Contraejemplo");
					g.dump();
					throw new Exception("Contraejemplo");
				}				
			}
			//System.out.println("*********");
		}
	}
	
	// test graphs of 50, 100 y 200 nodos
	// 400 100 * 3
	// 200 5000 * 3
	// 100 10000 * 3
	// 50  100000 * 3
	// 25  1000000 * 3
	public static void main(String[] args) {
		int K = 100;
		int nV = 400;
		try {
			StarConjectureRandom sh = new StarConjectureRandom(nV, K);
			sh.process();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
