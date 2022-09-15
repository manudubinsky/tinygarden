package edu.undav.research.tinygarden;

import java.util.Random;

import edu.undav.research.tinygarden.BasicClasses.Graph;
import edu.undav.research.tinygarden.SpanningTreesMatsui.LABILogger;
import edu.undav.research.tinygarden.SpanningTreesMatsuiCollector.MinMaxCollector;

public class LowerBoundConjecture {

    public static void main(String[] args)  throws Exception {
        RandomGraphGenerator lbc = new RandomGraphGenerator();
        //lbc.generateRandomTree(10).dump();
        //lbc.generateRandomConnectedGraph(5, 9).dump();
        int n = 10;
        Random generator = new Random();
      
        SpanningTreesMatsui s;
        MinMaxCollector collector;
		LABILogger log = new LABILogger("C:/Users/manud/graph-data/stats/deg-" + n + "-lower_bound_conjecture.csv");        
        for (int i = 0; i < 10; i++) {
            int m = generator.nextInt(2 * (n-1) + 1, 29);
            System.err.println(i + ", " + m);
            Graph g = lbc.generateRandomConnectedGraph(n, m);
            s = new SpanningTreesMatsui(g);
            collector = new MinMaxCollector(s, null, log);
            s.allSpanningTrees();
            collector.postProcess();
        }
        log.flush();
    }
}
