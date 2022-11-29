package edu.undav.research.tinygarden;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

import edu.undav.research.tinygarden.BasicClasses.Graph;
import edu.undav.research.tinygarden.BasicClasses.VecInt;
import edu.undav.research.tinygarden.SpanningTreesMatsui.LABILogger;
import edu.undav.research.tinygarden.SpanningTreesMatsuiProcessor.IntersectionNumberProcessor;

public class MinIntersectionNumberConjecture {
/*
	file    trees	min_intersection	min_trees	max_intersection	max_trees
edges						
7	A10.mat     1       0	                1	       0	1
8	A1097.mat	3	    0	                3	0	3
9	A1087.mat	8	    0	                8	0	8
10	A100.mat	16	    0	                9	0	12
11	A1007.mat	40	    1	                1	1	16
12	A1010.mat	75	    3	                1	3	18
13	A1000.mat	125	    5	                1	6	8
14	A1006.mat	300	    7	                1	9	8
15	A10497.mat	540	    11	                1	13	6
16	A10000.mat	864	    15	                1	17	6
17	A10005.mat	1296	19	                1	34	6
18	A10012.mat	3024	24	                1	40	6
19	A10052.mat	5292	30	                1	58	6
20	A10067.mat	8232	36	                1	73	6
21	A10154.mat	12005	42	                1	82	6
22	A10243.mat	16807	50	                1	90	4
23	A10335.mat	38416	58	                1	105	6
24	A10461.mat	65856	66	                1	126	12
25	A10490.mat	100352	75	                2	140	18
26	A11099.mat	143360	85	                4	155	180
27	A11116.mat	196608	95	                6	165	15480
28	A11117.mat	262144	105	                8	175	201600
*/ 
    public static void dumpGraphViz(Graph g, String fileName) {
        VecInt edges = g.getEdges();
        BufferedWriter writer = null;
        try {
            //create a temporary file
            //String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmssSS").format(Calendar.getInstance().getTime());
            File logFile = new File(fileName);

            // This will output the full path where the file will be written to...
            //System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write("graph {\n");
            writer.write("node [shape = circle]\n");
            writer.write("4 [label = \"4\", pos = \"2.0, 0.0!\"];\n");
            writer.write("3 [label = \"3\", pos = \"1.4142135623730951, 1.414213562373095!\"];\n");
            writer.write("2 [label = \"2\", pos = \"1.2246467991473532e-16, 2.0!\"];\n");
            writer.write("1 [label = \"1\", pos = \"-1.414213562373095, 1.4142135623730951!\"];\n");
            writer.write("8 [label = \"8\", pos = \"-2.0, 2.4492935982947064e-16!\"];\n");
            writer.write("7 [label = \"7\", pos = \"-1.4142135623730954, -1.414213562373095!\"];\n");
            writer.write("6 [label = \"6\", pos = \"-3.6739403974420594e-16, -2.0!\"];\n");
            writer.write("5 [label = \"5\", pos = \"1.4142135623730947, -1.4142135623730954!\"];\n");
            
            for (int i = 0; i < edges.size(); i+=2) {
                writer.write("  " + (edges.get(i) + 1) + " -- " + (edges.get(i+1) + 1) + " [color=blue]\n");
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
    public static void main(String[] args) throws Exception {
        RandomGraphGenerator generator = new RandomGraphGenerator();
        int n = 9;
        Random _generator = new Random();
        LABILogger log = new LABILogger("C:/Users/manud/graph-data/stats/deg-" + n + "-sample_1000.csv");

        for (int i = 0; i < 1000; i++) {            
            int m = _generator.nextInt(8, 32);
            Graph g = generator.generateRandomConnectedGraph(n, m);
            int intersectionNumber =  SpanningTreesMatsui.intersectionNumber(g);
            int mu = m - n + 1;
            int q = (2 * mu) / (n - 1);
            int r = (2 * mu) % (n-1);       
            int muEquidistributed = (n-1) * (q*(q-1) / 2) + r  * q;
            System.out.println(i + "," + m + "," + muEquidistributed + "," + intersectionNumber);
            log.logLine(i + "," + m + "," + muEquidistributed + "," + intersectionNumber);
            if (i % 20 == 19) {
                log.flush();
            }
            if (intersectionNumber < muEquidistributed) {
                g.codeDump();
            }
        }
        log.flush();         
    }    

/* 
 * 
for dir in `ls ./`; do 
    cd $dir; 
    for file in `ls ./`; do 
        neato -Tsvg "$file" >  $file.svg
    done 
    cd ..;
done

     
*/

/* 
    public static void main(String[] args) throws Exception {
        String[] files = {
            "A2.mat", 
            "A4.mat", 
            "A5.mat", 
            "A9.mat", 
           "A21.mat", 
           "A24.mat", 
           "A32.mat", 
           "A33.mat", 
          "A216.mat", 
          "A220.mat", 
            "A3.mat", 
            "A6.mat", 
            "A7.mat", 
           "A10.mat", 
           "A11.mat", 
           "A22.mat", 
           "A25.mat", 
           "A26.mat", 
           "A27.mat", 
           "A28.mat", 
           "A34.mat", 
           "A35.mat", 
           "A36.mat", 
           "A37.mat", 
           "A42.mat", 
           "A45.mat", 
           "A57.mat", 
           "A58.mat", 
           "A91.mat", 
           "A92.mat", 
           "A93.mat", 
          "A104.mat", 
          "A105.mat", 
          "A106.mat", 
          "A107.mat", 
          "A217.mat", 
          "A221.mat", 
          "A222.mat", 
          "A223.mat", 
          "A224.mat", 
          "A225.mat", 
          "A226.mat", 
          "A292.mat", 
           "A23.mat", 
           "A38.mat", 
           "A40.mat", 
           "A60.mat", 
           "A62.mat", 
          "A108.mat", 
          "A110.mat", 
          "A112.mat", 
          "A218.mat", 
          "A228.mat", 
          "A229.mat", 
          "A230.mat", 
          "A232.mat", 
          "A238.mat", 
          "A266.mat", 
          "A378.mat", 
          "A479.mat", 
          "A219.mat", 
          "A272.mat"};
        int nodeCnt = 7;
        //int edges = 12;
        for (int file = 0; file < files.length; file++) {
            String fName = files[file];
            Graph g = GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
                                          "C:/Users/manud/graph-data/deg-"+nodeCnt+"/" + fName);
            dumpGraphViz(g, "C:\\Users\\manud\\graph-data\\graphviz\\minimum\\" + nodeCnt + "\\" + g.getNumberOfEdges() + "\\" + fName);
        }   
    }
*/
/* 
    public static void main(String[] args) throws Exception {
        String[] files = {
            "A570.mat", 
                "A718.mat", 
                "A1302.mat", 
                "A4592.mat", 
                "A4641.mat", 
                "A4670.mat", 
                "A5193.mat"};
        int nodeCnt = 8;
        int edges = 11;
        for (int file = 0; file < files.length; file++) {
            String fName = files[file];
            Graph g = GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
                                            "C:/Users/manud/graph-data/deg-"+nodeCnt+"/" + fName);
            VecInt data = new VecInt(1);
            data.pushBack(SpanningTreesMatsui.intersectionNumber(g));
            Item item = new Item(fName,data);
            SpanningTreesMatsui s = new SpanningTreesMatsui(g);
            IntersectionNumberProcessor processor = new IntersectionNumberProcessor(s, item,
                            "C:\\Users\\manud\\graph-data\\graphviz\\minimum\\" + edges + "\\");
            s.allSpanningTrees();
    
        }   
    }
*/
}
