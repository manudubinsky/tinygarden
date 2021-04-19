package edu.undav.research.tinygarden;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.undav.research.tinygarden.BasicClasses.EdgeRelabelGraph;
import edu.undav.research.tinygarden.BasicClasses.Graph;
import edu.undav.research.tinygarden.BasicClasses.PartitionUnionFind;
import edu.undav.research.tinygarden.BasicClasses.VecBool;
import edu.undav.research.tinygarden.BasicClasses.VecInt;


public class RandomSpanningTrees {
	Graph _graph;
	
	public RandomSpanningTrees(Graph g) {
		_graph = g;
	}
	
	public List<Integer> getEdgeEnumeration() {
		List<Integer> edgeEnumeration;
		edgeEnumeration = new LinkedList<Integer>();
		for (int i = 0; i < _graph.getNumberOfEdges(); i++) {
			edgeEnumeration.add(i);
		}
		return edgeEnumeration;
	}
	
	public TreeMatsui generateRandomTree() throws Exception {
		//System.out.println(list);
		Graph tree = new Graph(_graph.getNumberOfVertices());
		List<Integer> edges = getEdgeEnumeration();
		java.util.Collections.shuffle(edges);
		VecBool addedVertexes = new VecBool(_graph.getNumberOfVertices(), false);
		Map<Integer, Boolean> loopEdges = new HashMap<Integer, Boolean>();
		PartitionUnionFind components = new PartitionUnionFind(_graph.getNumberOfVertices());
		for (int i = 0; i < edges.size(); i++) {
			int iE = edges.get(i);
			int iV0 = _graph.getVertex0(iE);
			int iV1 = _graph.getVertex1(iE);
			if (!addedVertexes.get(iV0) || !addedVertexes.get(iV1)) {
				tree.insertEdge(iV0, iV1);
				addedVertexes.set(iV0, true);
				addedVertexes.set(iV1, true);
				components.join(iV0, iV1);
			} else {
				loopEdges.put(iE, true);
			}
		}
		for (Integer iE : loopEdges.keySet()) {
			int iV0 = _graph.getVertex0(iE);
			int iV1 = _graph.getVertex1(iE);
			if (components.checkHasToJoin(iV0, iV1)) {
				tree.insertEdge(iV0, iV1);
				components.join(iV0, iV1);
				loopEdges.put(iE, false);
			}
		}
		for (Integer iE : loopEdges.keySet()) {
			int iV0 = _graph.getVertex0(iE);
			int iV1 = _graph.getVertex1(iE);
			if (loopEdges.get(iE)) {
				tree.insertEdge(iV0, iV1);
			}
		}
		VecInt identityMapping = new VecInt(tree.getNumberOfEdges(), 0);
		for (int i = 0; i < identityMapping.size(); i++) {
			identityMapping.set(i, i);
		}
		return new TreeMatsui(new CustomEdgeRelabelGraph(tree, identityMapping, identityMapping));
	}
	
	public static class CustomEdgeRelabelGraph extends EdgeRelabelGraph {

		public CustomEdgeRelabelGraph(Graph g, VecInt edgeLabels, VecInt reverseLabels) {
			super(g);
			setMapping(edgeLabels, reverseLabels);
		}
		
		protected void relabelEdges() {
			
		}
		
		
	}
	
	
	public static void main(String[] args) throws Exception {
		Graph g = Graph.buildCompleteGraph(20);
		RandomSpanningTrees generator = new RandomSpanningTrees(g);
		for (int i = 0; i < 10; i++) {
			TreeMatsui tree = generator.generateRandomTree();
			System.out.println(tree.labiCantZeros());
		}
	}
}
