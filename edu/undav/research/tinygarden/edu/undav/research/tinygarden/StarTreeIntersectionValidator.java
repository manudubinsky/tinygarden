package edu.undav.research.tinygarden;

import java.util.concurrent.ThreadLocalRandom;

import edu.undav.research.tinygarden.BasicClasses.Graph;
import edu.undav.research.tinygarden.BasicClasses.SparseMatrixInt;
import edu.undav.research.tinygarden.BasicClasses.VecInt;


public class StarTreeIntersectionValidator {
	int _nV;
	int _nVBranch;
	Graph _graph;
	VecInt _parents;
	VecInt _degrees;
	
	public StarTreeIntersectionValidator(int nV) {
		_nV = nV; //la cantidad total de vertices
		_nVBranch = _nV - 2; // la cantidad total de vertices sin considerar la raiz (r) y el segundo nodo (v)		
		build();
	}	
	
	private void insertEdge(int iV0, int iV1) {
		int iE = _graph.insertEdge(iV0,iV1);
		if (iE != -1) {
			_degrees.inc(iV0);
			_degrees.inc(iV1);
		}
	}
	
	private void build() {
		_degrees = new VecInt(_nV,0);
		_graph = new Graph(_nV);
		_parents = new VecInt(_nV, 0); //al inicializar en 0s los parents de r y v quedan bien
		insertEdge(0, 1);
		VecInt branchesIndexes = new VecInt(_nV);
		int i = 2;		
		while (i < _nV) {
			int branchVertices = ThreadLocalRandom.current().nextInt(1, _nV - i + 1);
			insertEdge(1, i); // las ramas cuelgan del segundo nodo (v)
			_parents.set(i, 1);
			for (int j = 0; j < branchVertices-1; j++) {
				insertEdge(i+j, i+j+1);
				_parents.set(i+j+1, i+j);
			}			
			//System.out.println("ACA: " + branchVertices);			
			branchesIndexes.pushBack(i);
			i += branchVertices;
		}
		//agregar los ejes que definen la estrella
		for (int j = 2; j < _nV; j++) {
			insertEdge(0, j);
		}
		//1ra. version: agrego  1 solo outstanding-edge por rama
		branchesIndexes.dump();
/*		for (int j = 0; j < branchesIndexes.size(); j++) {
			int begin = branchesIndexes.get(j);
			int end = j == branchesIndexes.size() - 1 ? _nV : branchesIndexes.get(j+1);
			//System.out.println("begin: " + begin + " end: " + end);
			if (end - begin - 1 > 2) { // hay al menos 3 nodos en la rama
				insertEdge(begin, end - 1);
			}
		}
*/		_graph.dump();
		//_brachesIndexes.dump();
	}

	public void check() {
		//para cada cycle-edge analizamos que tree-edges pertenecen al tree-cycle correspondiente
		//nota: se establece una correspondencia entre el cycle-edge y el correspondiente tree-cycle
		VecInt[] treeEdgesCycles = new VecInt[_graph.getNumberOfVertices() - 1]; // la cantidad de tree-edges
		for (int i = _graph.getNumberOfVertices()-1; i < _graph.getNumberOfEdges(); i++) {
			int iV0 = _graph.getVertex0(i);
			int iV1 = _graph.getVertex1(i);
			System.out.println("ACA: " + iV0 + " " + iV1);
			while (iV0 != iV1) {
				int parent = -1; int iE = -1;
				if (iV0 < iV1) {
					parent = _parents.get(iV1);
					iE = _graph.getEdge(parent, iV1);
					iV1 = parent;
				} else {
					parent = _parents.get(iV0);
					iE = _graph.getEdge(parent, iV0);
					iV0 = parent;
				}
				if (treeEdgesCycles[iE] == null) {
					treeEdgesCycles[iE] = new VecInt(2);
				}
				treeEdgesCycles[iE].pushBack(i);
			}
		}
		treeEdgesCycles[0].dump();
		
		SparseMatrixInt cycleIntersect = new SparseMatrixInt(_graph.getNumberOfEdges());
		for (int i = 0; i < treeEdgesCycles.length; i++) {
			VecInt cycles = treeEdgesCycles[i];
			if (cycles != null) {
				for (int j = 0; j < cycles.size(); j++) {
					for (int k = j+1; k < cycles.size(); k++) {
						cycleIntersect.add(cycles.get(j), cycles.get(k), 1);
						cycleIntersect.add(cycles.get(k), cycles.get(j), 1);
					}				
				}
			} else {
				System.out.println("ACA!!!: " + i);
			}
		}
		//cycleIntersect.dump();
		int intersectCount = 0;
		for (int i = 0; i < cycleIntersect.getRows(); i++) {
			for (int j = i+1; j < cycleIntersect.getCols(); j++) {
				if (cycleIntersect.get(i,j) > 0) {
					intersectCount++;
				}
			}
		}
		System.out.println("intersectCount: " + intersectCount);
	}
	
	public int starFormula() {
		return (_degrees.squareNorm()-6*_graph.getNumberOfEdges()-(_nV-1) * (_nV - 6)) / 2;
	}
	
	public static void main(String[] args) {
		StarTreeIntersectionValidator s = new StarTreeIntersectionValidator(8);
		s.check();
		System.out.println("star #intersect: " + s.starFormula());
	}
}
