package edu.undav.research.tinygarden;

import java.util.Random;

import edu.undav.research.tinygarden.BasicClasses.Graph;
import edu.undav.research.tinygarden.BasicClasses.VecBool;
import edu.undav.research.tinygarden.BasicClasses.VecInt;

public class RandomGraphGenerator {
    Random _generator;

    public RandomGraphGenerator() {
        _generator = new Random();
    }

    /**
     * @param n
     * @return
     */
    public Graph generateRandomTree(int n) {        
		Graph tree = new Graph(n);
		final VecInt treeNodes = new VecInt(n);
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

    public Graph generateRandomConnectedGraph(int n, int m) {        
        Graph g = null;
        if (n > 0 && m >= n-1 && m <= n * (n-1) / 2) {
            g = generateRandomTree(n);
            while (g.getNumberOfEdges() < m) {
                int iV0 = _generator.nextInt(n);
                while (g.degree(iV0) == n-1)
                    iV0 = (iV0 + 1) % n;
                VecBool neighbors = new VecBool(n, false);
                neighbors.set(iV0, true);
                VecInt edges = g.getVertexEdges(iV0);
                for (int iE = 0; iE < edges.size(); iE++) {
                    neighbors.set(g.getNeighbor(iV0, edges.get(iE)), true);
                }
                int iV1 = _generator.nextInt(n);
                while (neighbors.get(iV1))
                    iV1 = (iV1 + 1) % n;                
                g.insertEdge(iV0, iV1);
            }
        }

        return g;
    }

}
