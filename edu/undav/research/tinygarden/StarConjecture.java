package edu.undav.research.tinygarden;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.undav.research.tinygarden.BasicClasses.Graph;
import edu.undav.research.tinygarden.BasicClasses.SparseMatrixInt;
import edu.undav.research.tinygarden.BasicClasses.VecBool;
import edu.undav.research.tinygarden.BasicClasses.VecInt;


/*
 * Reference:
 * M. Dubinsky, C. Massri, G. Taubin, Minimum Spanning Tree Cycle Intersection problem, 
 * Discrete Applied Mathematics 294 (2021), 152-166 
 * 
With nauty generate all the trees of a given number of nodes:
	Example (7 nodes):
	
		geng 7 6:6 -c | listg -A 

foreach tree T
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

public class StarConjecture {
	Graph _tree;
	int _starRoot;
	VecInt _parent;
	VecInt _nonInterbranchCycleEdges;
	VecInt _starEdges;
	VecInt _bfsLabels;
	VecInt[] _treeEdgesCycles;
	int _testNum;
	
	public StarConjecture(int testNum, Graph tree) {
		_tree = tree;
		_testNum = testNum;
	}
	
	public static Graph buildFromAdjMatrix(int n, String fileName) {
		Graph g = new Graph(n);
	    File file = new File(fileName);
	    try {
	    	//System.out.println(file);
	        Scanner sc = new Scanner(file);
	        int j = 0;
	        while (sc.hasNextInt()) {
	            if (sc.nextInt() == 1) 	            
	            	g.insertEdge(j/n, j%n);
	            j++;
	        }
	        sc.close();
	    } 
	    catch (FileNotFoundException e) {
	        e.printStackTrace();
	    }
	    //g.dump();
	    return g;
	}
	
	private void buildParents(Graph g) {
	//BFS from the root of the star
		int nV = g.getNumberOfVertices();
		int queueIdx = 0;
		int labelIdx = 0;
		VecInt queue = new VecInt(nV);
		queue.pushBack(_starRoot);		
		_parent = new VecInt(nV, 0);
		_parent.set(_starRoot, -1); //set root parent in -1
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
	
	private void addCycleEdges(Graph g) {
		_starEdges = new VecInt(20);
		_nonInterbranchCycleEdges  = new VecInt(20);
		for (int i = 0; i < g.getNumberOfVertices(); i++) {
			if (i != _starRoot) {
				int ancestor = _parent.get(i);
				while (ancestor != -1) {
					int iE = g.insertEdge(i, ancestor);
					if (iE != -1) {
						if (ancestor != _starRoot) {
							_nonInterbranchCycleEdges.pushBack(iE);
						} else {
							_starEdges.pushBack(iE);
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

	private int checkStarFormula(Graph g, long mask) {
		VecInt[] treeEdgesCycles = new VecInt[g.getNumberOfEdges()];
		VecBool validEdges = new VecBool(g.getNumberOfEdges(), true);
		for (int i = 0; i < _nonInterbranchCycleEdges.size(); i++) {
			long edgeMask = (long) Math.pow(2, i);
			if ((edgeMask & mask) == 0) { // if not consider the cycle-edge
				int iE = _nonInterbranchCycleEdges.get(i);
				validEdges.set(iE, false);
			}			
		}
		
		for (int iE = 0; iE < validEdges.size(); iE++) {
			if (validEdges.get(iE)) {
				int iV0 = g.getVertex0(iE);
				int iV1 = g.getVertex1(iE);
				if (iV0 != _starRoot && iV1 != _starRoot) { // if it is a cycle edge in star
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
		}

		//fill cycle instrsection matrix
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

		// count intersections
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
	
	private boolean check(Graph g, long mask) throws Exception {
		_treeEdgesCycles = new VecInt[g.getNumberOfVertices() - 1];
		//non interbranch cycle edges
		int i = 0;
		long edgeMask = (long) Math.pow(2, i);
		//System.out.println("ACA: " + _nonInterbranchCycleEdges.size() + " mask: " + mask);
		while (i < _nonInterbranchCycleEdges.size() && edgeMask <= mask) {
			//System.out.println("entre! i: " + i);
			if ((edgeMask & mask) == edgeMask) {
				int iE = _nonInterbranchCycleEdges.get(i);
				//System.out.println("usando eje: " + iE + " i: " + i);
				setTreeCycleEdges(g, iE);
			}
			i++;
			edgeMask = (long) Math.pow(2, i);
		}
		
		//star cycle edges
		for (i = 0; i < _starEdges.size(); i++) {
			setTreeCycleEdges(g, _starEdges.get(i));
		}
		
		//fill cycle intersection matrix
		SparseMatrixInt cycleIntersect = new SparseMatrixInt(g.getNumberOfEdges());
		for (i = 0; i < _treeEdgesCycles.length; i++) {
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

		// count intersections
		int intersectCount = 0;
		for (i = 0; i < cycleIntersect.getRows(); i++) {
			for (int j = i+1; j < cycleIntersect.getCols(); j++) {
				if (cycleIntersect.get(i,j) > 0) {
					intersectCount++;
				}
			}
		}

		int star = starTreeIntersect(g, mask);
		int starCheck = checkStarFormula(g, mask);
		if (star != starCheck) {
			System.out.println("mask: " + mask + " star: " + star + " starCheck: " + starCheck);
			g.dump();
			throw new Exception("star != starCheck");
		}
		//System.out.println(_testNum + ": " + star + " <=? " + intersectCount);
		return star <= intersectCount;
	}
	
	private int starTreeIntersect(Graph g, long mask) {
		int nV = g.getNumberOfVertices();
		int nE = g.getNumberOfEdges(); // hay que restar los _nonInterbranchCycleEdges que no estan en la mascara
		VecInt degrees = new VecInt(g.getDegrees());

		//Restar ejes de la mascara
		int i = 0;
		long edgeMask = (long) Math.pow(2, i);
		while (i < _nonInterbranchCycleEdges.size()) {
			if ((edgeMask & mask) != edgeMask) {
				nE--;
				int iE = _nonInterbranchCycleEdges.get(i);
				int iV0 = g.getVertex0(iE);
				int iV1 = g.getVertex1(iE);
				degrees.dec(iV0);
				degrees.dec(iV1);
			}
			i++;
			edgeMask = (long) Math.pow(2, i);
		}
		
		return (degrees.squareNorm()-6*nE-(nV-1) * (nV - 6)) / 2;
	}
	
	public void process() throws Exception {
		_starRoot = _tree.insertVertex();
		for (int i = 0; i < _tree.getNumberOfVertices() && i != _starRoot; i++) {
		// Cada nodo del arbol puede ser el unico vecino de la raiz 
			Graph g = _tree.clone();
			g.insertEdge(_starRoot, i);			
		// Armar los parents del arbol T
			buildParents(g);
		// Agregar ejes de cada rama 
			addCycleEdges(g);
			//System.out.println("#test: " + i + " #interbranch: " + _nonInterbranchCycleEdges.size());
			//System.out.println(_nonInterbranchCycleEdges.size());
			if (_nonInterbranchCycleEdges.size() < 64) {
				long nonInterbranchCycleEdges  = (long) Math.pow(2, _nonInterbranchCycleEdges.size());
				for (long mask = 0; mask < nonInterbranchCycleEdges; mask++) {
					if (!check(g, mask)) {
						System.out.println("Contraejemplo mask: " + mask);
						g.dump();
						throw new Exception("Contraejemplo");
					}
				}
			} else {
				System.out.println("_nonInterbranchCycleEdges.size() >= 64");
			}
			//System.out.println("*******");
		}
	}
	
	public static void main(String[] args) {
		int N = 9;
		String dir = "/Users/admin/20200829/doctorado/tesis-octave/ejemplos/connected-graphs/star-hypothesis/deg-"+N;
		try {
/*			StarHypothesis sh = new StarHypothesis(1, StarHypothesis.buildFromAdjMatrix(N, dir + "/A1.tree"));				
			sh.process();
*/
			for(int i = 1; i <= 1; i++) {				
				//System.out.println("A" + i);
				StarConjecture sh = new StarConjecture(i, StarConjecture.buildFromAdjMatrix(N, dir + "/A" + i + ".tree"));
				sh.process();
				//System.out.println("*************************");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
