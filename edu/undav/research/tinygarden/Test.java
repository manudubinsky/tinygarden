package edu.undav.research.tinygarden;

import edu.undav.research.tinygarden.BasicClasses.Graph;
import edu.undav.research.tinygarden.SpanningTreesMatsuiCollector.MaxZerosCollector;

/*
/*
 * Reference:
 * M. Dubinsky, C. Massri, G. Taubin, Minimum Spanning Tree Cycle Intersection problem, 
 * Discrete Applied Mathematics 294 (2021), 152-166 
 * 
 */

public class Test {

	public static void main(String[] args) throws Exception {
		// complete graphs
		System.out.println("Test 1: complete graphs\n");
		for (int i = 4; i < 8; i++) {
			SpanningTreesMatsui s;
			MaxZerosCollector collector;
			String fName = "K" + i;
			System.out.println("Analyzing all spanning trees of " + fName + "...");
			s = new SpanningTreesMatsui(Graph.buildCompleteGraph(i));
			collector = new MaxZerosCollector(s, fName, null);
			s.allSpanningTrees();
			collector.postProcess();
			System.out.println("");
		}
		
		System.out.println("-------------");

		//non-isomorphic trees of 9 vertices
		int N = 7;
		System.out.println("\nTest 2: attempt to find counterexample of star conjecture based on all non-isomorphic trees of " + N + " nodes\n");		
		
		String dir = "/Users/admin/eclipse-workspace/tinygarden/data/deg-7"
		for(int i = 1; i <= 11; i++) {
			String file = dir + "/A" + i + ".tree";
			StarConjecture sh = new StarConjecture(i, StarConjecture.buildFromAdjMatrix(N, file));
			try {
				sh.process();
				System.out.println(file + ": conjecture verified");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(file + ": counterexample found!");
			}
		}

		//random graphs
		System.out.println("\n-------------");
		int K = 500;
		int nV = 100;

		System.out.println("\nTest 3: attempt to find counterexample of star conjecture generating " + K + " reduced graphs of " + nV + " nodes\n");		


		StarConjectureRandom sh = new StarConjectureRandom(nV, K);
		try {
			sh.process();
			System.out.println("conjecture verified");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("counterexample found!");
		}

	}

}
