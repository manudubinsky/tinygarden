package edu.undav.research.tinygarden;

import edu.undav.research.tinygarden.BasicClasses.Graph;
import edu.undav.research.tinygarden.BasicClasses.VecInt;
import edu.undav.research.tinygarden.SpanningTreesMatsuiCollector.MinMaxCollector;
import edu.undav.research.tinygarden.SpanningTreesMatsuiProcessor.IntersectionNumberProcessor;

public class AddEdgeConjecture {

/* 
fName: A849.mat
Graph g = new Graph(7);
g.insertEdge(0, 2);
g.insertEdge(0, 3);
g.insertEdge(0, 4);
g.insertEdge(0, 5);
g.insertEdge(0, 6);
g.insertEdge(1, 2);
*/
    /*
     * Graph graph = new Graph(7);
graph.insertEdge(0, 2);
graph.insertEdge(0, 3);
graph.insertEdge(0, 4);
graph.insertEdge(0, 5);
graph.insertEdge(0, 6);
graph.insertEdge(1, 2);
graph.insertEdge(1, 3);
graph.insertEdge(1, 4);
graph.insertEdge(1, 5);
graph.insertEdge(1, 6);
graph.insertEdge(2, 4);
graph.insertEdge(2, 5);
graph.insertEdge(2, 6);
graph.insertEdge(3, 4);
graph.insertEdge(3, 5);
graph.insertEdge(3, 6);
graph.insertEdge(4, 6);

Graph graph = new Graph(7);
graph.insertEdge(0, 2);
graph.insertEdge(0, 3);
graph.insertEdge(0, 4);
graph.insertEdge(0, 5);
graph.insertEdge(0, 6);
graph.insertEdge(1, 2);
graph.insertEdge(1, 3);
graph.insertEdge(1, 4);
graph.insertEdge(1, 5);
graph.insertEdge(1, 6);
graph.insertEdge(2, 4);
graph.insertEdge(2, 5);
graph.insertEdge(2, 6);
graph.insertEdge(3, 4);
graph.insertEdge(3, 5);
graph.insertEdge(3, 6);
graph.insertEdge(4, 6);
graph.insertEdge(5, 6);
    */

/*
 * Observaciones: 
 * 1) Sea v el nodo de grado máximo en T, 
 *      confirmar si T contiene a todos los ejes incidentes en v
 * 2) En el ejemplo de 7 nodos, el "next" pasa a contener una estrella. Pasa lo mismo 
 *      en los ejemplos de 8 nodos ?
 * 3) Cuántos "next" tiene cada ejemplo ? Qué diferencia hay entre los intersectionNumbers ?
 * 4) cuántos árboles que alcancen el mínimo tienen g y su/s sucesor/es
 * 
 * Conjetura (generaliza teorema estrella):
 * Toda solución T del problema MSTCI tiene la propiedad de que contiene todos los ejes de 
 * un nodo de grado máximo de G
 *
 * Esto último puede ser importante para pensar un algoritmo polinomial para el problema
*/
/* 
    public static void main(String[] args) throws Exception {
        Graph g = new Graph(8);
        g.insertEdge(0, 3);
        g.insertEdge(0, 4);
        g.insertEdge(0, 5);
        g.insertEdge(0, 6);
        g.insertEdge(0, 7);
        g.insertEdge(1, 3);
        g.insertEdge(1, 4);
        g.insertEdge(1, 5);
        g.insertEdge(1, 6);
        g.insertEdge(1, 7);
        g.insertEdge(2, 3);
        g.insertEdge(2, 4);
        g.insertEdge(2, 5);
        g.insertEdge(2, 6);
        g.insertEdge(2, 7);
        g.insertEdge(3, 6);
        g.insertEdge(3, 7);
        g.insertEdge(4, 6);
        g.insertEdge(4, 7);

        g.buildIncidenceMatrix().latexDump();
        int intersectionNumber =  intersectionNumber(g);
        VecInt data = new VecInt();
        data.pushBack(intersectionNumber);
        Item item = new Item("A10462",data);
        SpanningTreesMatsui s = new SpanningTreesMatsui(g);
        IntersectionNumberProcessor processor = new IntersectionNumberProcessor(s, item,
                                                        "C:/Users/manud/graph-data/graphviz/deg-8/A10462/g/");
        s.allSpanningTrees();
    }    
*/
 
/* 
    public static void main(String[] args) throws Exception {
        String fName = "A849.mat";
        int nodeCnt = 7;
        Graph g = GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
                                        "C:/Users/manud/graph-data/deg-"+nodeCnt+"/" + fName);
        g.codeDump();
    }
*/

/* 
    public static void main(String[] args) throws Exception {
        String fName = "A10462.mat";
        int nodeCnt = 8;
        Graph g = GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
                                        "C:/Users/manud/graph-data/deg-"+nodeCnt+"/" + fName);
        int intersectionNumber =  SpanningTreesMatsui.intersectionNumber(g);

        SpanningTreesMatsui s = new SpanningTreesMatsui(g);
        for (int i = 0; i < g.getNumberOfVertices(); i++) {
            for (int j = i+1; j < g.getNumberOfVertices(); j++) {
                if (g.getEdge(i, j) == -1) {
                    Graph g2 = g.clone();
                    g2.insertEdge(i, j);
                    int intersectionNumber2 =  SpanningTreesMatsui.intersectionNumber(g2);
                    if (intersectionNumber2 < intersectionNumber) {
                        VecInt data = new VecInt();
                        data.pushBack(intersectionNumber2);
                        Item item = new Item(i + "-" + j,data);
                        s = new SpanningTreesMatsui(g2);
                        IntersectionNumberProcessor processor = new IntersectionNumberProcessor(s, item,
                                                                        "C:/Users/manud/graph-data/graphviz/deg-8/A10462/" + i + "-" + j + "/");
                        s.allSpanningTrees();

                    } 
                }
            }
        }
    }

*/
 
/* 
 * 
 * 
46
44
44
44
44
45
45
45
45
44
A10462.mat: 9
******
54
53
53
53
52
52
52
52
52
A10478.mat: 8
******
60
59
59
59
59
59
59
A11104.mat: 6
******
69
68
68
66
66
66
A11110.mat: 5
******
69
67
66
66
66
66
A11111.mat: 5
******
82
75
75
75
75
A11113.mat: 4
******
*/
/* 
    public static void main(String[] args) throws Exception {
            String[] files = {
            "A10462.mat",
            "A10478.mat",
            "A11104.mat",
            "A11110.mat",
            "A11111.mat",
            "A11113.mat"};

        int nodeCnt = 8;
        for (int file = 0; file < files.length; file++) {
            String fName = files[file];
            Graph g = GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
                                            "C:/Users/manud/graph-data/deg-"+nodeCnt+"/" + fName);
            int intersectionNumber =  SpanningTreesMatsui.intersectionNumber(g);
            System.out.println(intersectionNumber);
            int nextCnt = 0;
            for (int i = 0; i < g.getNumberOfVertices(); i++) {
                for (int j = i+1; j < g.getNumberOfVertices(); j++) {
                    if (g.getEdge(i, j) == -1) {
                        Graph g2 = g.clone();
                        g2.insertEdge(i, j);
                        int intersectionNumber2 =  SpanningTreesMatsui.intersectionNumber(g2);
                        System.out.println(intersectionNumber2);
                        if (intersectionNumber2 < intersectionNumber) {
                            nextCnt++;
                        } 
                    }
                }
            }
            System.out.println(fName + ": " + nextCnt);
            System.out.println("******");
        }
    }
*/
 
    public static void main(String[] args) throws Exception {
 
        String[] files = {
            "A10462.mat",
            "A10478.mat",
            "A11104.mat",
            "A11110.mat",
            "A11111.mat",
            "A11113.mat"};

        int nodeCnt = 8;
        for (int file = 0; file < files.length; file++) {
            String fName = files[file];
            Graph g = GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
                                            "C:/Users/manud/graph-data/deg-"+nodeCnt+"/" + fName);
            SpanningTreesMatsui s = new SpanningTreesMatsui(g);
            MinMaxCollector collector = new MinMaxCollector(s, null, null);
            s.allSpanningTrees();
            collector.postProcess();
        }
    }

/* 
    public static void main(String[] args) throws Exception {
        String[] files = {
            "A10462",
            "A10478",
            "A11104",
            "A11110",
            "A11111",
            "A11113"};

        int nodeCnt = 8;
        for (int file = 0; file < files.length; file++) {
            String fName = files[file];
            Graph g = GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
                                            "C:/Users/manud/graph-data/deg-"+nodeCnt+"/" + fName + ".mat");

            int intersectionNumber =  SpanningTreesMatsui.intersectionNumber(g);
            VecInt data = new VecInt();
            data.pushBack(intersectionNumber);
            Item item = new Item(fName,data);
            SpanningTreesMatsui s = new SpanningTreesMatsui(g);
            IntersectionNumberProcessor processor = new IntersectionNumberProcessor(s, item,
                                                            "C:/Users/manud/graph-data/graphviz/deg-8/"+fName+"/");
            s.allSpanningTrees();
        }
    }    
*/

/* 
    public static void main(String[] args) throws Exception {
        int nodeCnt = 8;
        int total = 11117;
        int graphCount = 0;
        int graphTotal = 0;        
        for (int graphNum = 1; graphNum <= total; graphNum++) {
            if (graphNum % 50 == 0) {
                System.out.println(graphNum);
            }
			String fName = "A" + graphNum + ".mat";
			Graph g = GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
										
            "C:/Users/manud/graph-data/deg-"+nodeCnt+"/" + fName);
            //if (14 <= g.getNumberOfEdges() && g.getNumberOfEdges() <= 24) {
            if (g.getNumberOfEdges() < 14) {
                graphTotal++;
                int intersectionNumber =  SpanningTreesMatsui.intersectionNumber(g);
                boolean match = true;
                for (int i = 0; i < g.getNumberOfVertices(); i++) {
                    for (int j = i+1; j < g.getNumberOfVertices(); j++) {
                        if (g.getEdge(i, j) == -1) {
                            Graph g2 = g.clone();
                            g2.insertEdge(i, j);
                            int intersectionNumber2 =  SpanningTreesMatsui.intersectionNumber(g2);
                            if (intersectionNumber2 >= intersectionNumber) {
                                match = false;
                            } 
                        }
                    }
                }
                if (match) {
                    graphCount++;
                    System.out.println("fName: " + fName);
                    g.codeDump();
                    System.out.println("*****************");
                }
            }
        }
        System.out.println("graphCount/total: " + graphCount + " / " + graphTotal);
    }

*/
/*
Para cada ejemplo: 
1) cuantos sucesores tienen menor nro de intersección
2) cuántos árboles que alcancen el mínimo tienen g y su/s sucesor/es
3) dumpGraphViz de los árboles que alcancen el mínimo
fName: A4567.mat
fName: A4577.mat
fName: A4578.mat
fName: A4581.mat
fName: A4582.mat
fName: A4584.mat
fName: A8796.mat
fName: A9200.mat
fName: A9220.mat
fName: A9498.mat
fName: A9595.mat
fName: A9603.mat
fName: A9604.mat
fName: A9607.mat
fName: A9609.mat
fName: A9611.mat
fName: A9612.mat
fName: A9614.mat
fName: A9622.mat
fName: A10298.mat
fName: A10300.mat
fName: A10302.mat
fName: A10303.mat
fName: A10324.mat
fName: A10333.mat
fName: A10341.mat
fName: A10363.mat
fName: A10369.mat
fName: A10394.mat
fName: A10416.mat
fName: A10430.mat
fName: A10432.mat
fName: A10441.mat
fName: A10444.mat
fName: A10452.mat
fName: A10453.mat
fName: A10454.mat
fName: A10457.mat
fName: A10458.mat
fName: A10462.mat
fName: A10464.mat
fName: A10465.mat
fName: A10466.mat
fName: A10467.mat
fName: A10469.mat
fName: A10474.mat
fName: A10476.mat
fName: A10478.mat
fName: A10479.mat
fName: A10480.mat
fName: A10482.mat
fName: A10483.mat
fName: A10486.mat
fName: A10487.mat
fName: A10806.mat
fName: A10815.mat
fName: A10843.mat
fName: A10877.mat
fName: A10888.mat
fName: A10901.mat
fName: A10904.mat
fName: A10910.mat
fName: A10954.mat
fName: A10960.mat
fName: A10970.mat
fName: A10977.mat
fName: A10978.mat
fName: A10980.mat
fName: A10984.mat
fName: A10985.mat
fName: A10986.mat
fName: A10990.mat
fName: A10992.mat
fName: A11002.mat
fName: A11004.mat
fName: A11008.mat
fName: A11015.mat
fName: A11022.mat
fName: A11023.mat
fName: A11029.mat
fName: A11031.mat
fName: A11033.mat
fName: A11035.mat
fName: A11040.mat
fName: A11043.mat
fName: A11044.mat
fName: A11045.mat
fName: A11048.mat
fName: A11052.mat
fName: A11058.mat
fName: A11059.mat
fName: A11060.mat
fName: A11063.mat
fName: A11064.mat
fName: A11068.mat
fName: A11069.mat
fName: A11071.mat
fName: A11079.mat
fName: A11080.mat
fName: A11081.mat
fName: A11102.mat
fName: A11104.mat
fName: A11109.mat
fName: A11110.mat
fName: A11111.mat
fName: A11113.mat
*/

    /* 
Contraejemplo g:
Graph graph = new Graph(8);
graph.insertEdge(0, 5);
graph.insertEdge(1, 5);
graph.insertEdge(0, 4);
graph.insertEdge(5, 7);
graph.insertEdge(4, 6);
graph.insertEdge(2, 7);
graph.insertEdge(3, 6);
graph.insertEdge(0, 3);
graph.insertEdge(5, 6);
graph.insertEdge(0, 6);
graph.insertEdge(2, 5);
graph.insertEdge(2, 6);
graph.insertEdge(3, 4);
graph.insertEdge(3, 7);
graph.insertEdge(4, 7);
graph.insertEdge(4, 5);
graph.insertEdge(0, 1);
graph.insertEdge(1, 3);
graph.insertEdge(1, 2);

g2:
Graph graph = new Graph(8);
graph.insertEdge(0, 5);
graph.insertEdge(1, 5);
graph.insertEdge(0, 4);
graph.insertEdge(5, 7);
graph.insertEdge(4, 6);
graph.insertEdge(2, 7);
graph.insertEdge(3, 6);
graph.insertEdge(0, 3);
graph.insertEdge(5, 6);
graph.insertEdge(0, 6);
graph.insertEdge(2, 5);
graph.insertEdge(2, 6);
graph.insertEdge(3, 4);
graph.insertEdge(3, 7);
graph.insertEdge(4, 7);
graph.insertEdge(4, 5);
graph.insertEdge(0, 1);
graph.insertEdge(1, 3);
graph.insertEdge(1, 2);
graph.insertEdge(3, 5);
*/
/* 
    public static void main(String[] args) throws Exception {
        Graph g = new Graph(7);
        g.insertEdge(0, 2);
        g.insertEdge(0, 3);
        g.insertEdge(0, 4);
        g.insertEdge(0, 5);
        g.insertEdge(0, 6);
        g.insertEdge(1, 2);
        SpanningTreesMatsui s = new SpanningTreesMatsui(g);
        MinMaxCollector collector = new MinMaxCollector(s, null, null);
        s.allSpanningTrees();
        collector.postProcess();
    }
*/
/* 
    public static void main(String[] args) throws Exception {
        Graph g = new Graph(7);
        g.insertEdge(0, 2);
        g.insertEdge(0, 3);
        g.insertEdge(0, 4);
        g.insertEdge(0, 5);
        g.insertEdge(0, 6);
        g.insertEdge(1, 2);
        g.insertEdge(1, 3);
        g.insertEdge(1, 4);
        g.insertEdge(1, 5);
        g.insertEdge(1, 6);
        g.insertEdge(2, 4);
        g.insertEdge(2, 5);
        g.insertEdge(2, 6);
        g.insertEdge(3, 4);
        g.insertEdge(3, 5);
        g.insertEdge(3, 6);
        g.insertEdge(4, 6);        
        int intersectionNumber =  intersectionNumber(g);
        for (int i = 0; i < g.getNumberOfVertices(); i++) {
            for (int j = i+1; j < g.getNumberOfVertices(); j++) {
                if (g.getEdge(i, j) == -1) {
                    Graph g2 = g.clone();
                    g2.insertEdge(i, j);
                    int intersectionNumber2 =  intersectionNumber(g2);
                    if (intersectionNumber2 < intersectionNumber) {
                        System.out.println("intersectionNumber: " + intersectionNumber +
                                             " intersectionNumber2: " + intersectionNumber2);
                        g2.codeDump();
                        System.out.println("*****************");
                        //System.exit(0);
                    } 
                    // else {
                    //     System.out.println("intersectionNumber: " + intersectionNumber + 
                    //                         " intersectionNumber2: " + intersectionNumber2);
                    // }

                }
            }
        }                    


    }
*/
        /*
         * Generar grafo al azar (G)
         * Calcular intersection number de G
         * Armar lista de outstanding edges
         * Para cada outstanding edge:
         *  Clonar el grafo original (G')
         *  Agregar el outstanding edge
         *  Calcular el intersection number de G'
         *  Comparar los intersection numbers de G y G'
         *  Si el de G' es menor, hacer codeDump de G y G'
         */

    /* 
    public static void main(String[] args) throws Exception {
        RandomGraphGenerator generator = new RandomGraphGenerator();
        int n = 8;
        

        for (int m = 18; m < 25; m++) {
            for (int h = 0; h < 100; h++) {
                System.out.println("m: " + m + " h: " + h);
                Graph g = generator.generateRandomConnectedGraph(n, m);

                int intersectionNumber =  intersectionNumber(g);
        
                for (int i = 0; i < g.getNumberOfVertices(); i++) {
                    for (int j = i+1; j < g.getNumberOfVertices(); j++) {
                        if (g.getEdge(i, j) == -1) {
                            Graph g2 = g.clone();
                            g2.insertEdge(i, j);
                            int intersectionNumber2 =  intersectionNumber(g2);
                            if (intersectionNumber2 < intersectionNumber) {
                                g.codeDump();
                                System.out.println("*****************");
                                System.exit(0);;
                            } 
                            // else {
                            //     System.out.println("intersectionNumber: " + intersectionNumber + 
                            //                         " intersectionNumber2: " + intersectionNumber2);
                            // }
        
                        }
                    }
                }                    
            }                            
        }
    }    
    */
}
