package edu.undav.research.tinygarden;

import edu.undav.research.tinygarden.BasicClasses.Graph;
import edu.undav.research.tinygarden.BasicClasses.VecInt;
import edu.undav.research.tinygarden.SpanningTreesMatsuiProcessor.IntersectionNumberProcessor;

public class AddEdgeConjecture {

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
        /* 
        int intersectionNumber =  intersectionNumber(g);
        VecInt data = new VecInt();
        data.pushBack(intersectionNumber);
        Item item = new Item("A10462",data);
        SpanningTreesMatsui s = new SpanningTreesMatsui(g);
        IntersectionNumberProcessor processor = new IntersectionNumberProcessor(s, item,
                                                        "C:/Users/manud/graph-data/graphviz/deg-8/A10462/g/");
        s.allSpanningTrees();
        */
    }    

/* 
    public static void main(String[] args) throws Exception {
        String fName = "A10462.mat";
        int nodeCnt = 8;
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
    public static void main(String[] args) throws Exception {
        String[] files = {"A4567.mat","A4577.mat","A4578.mat","A4581.mat","A4582.mat","A4584.mat",
                            "A8796.mat","A9200.mat","A9220.mat","A9498.mat","A9595.mat","A9603.mat",
                            "A9604.mat","A9607.mat","A9609.mat","A9611.mat","A9612.mat","A9614.mat",
                            "A9622.mat","A10298.mat","A10300.mat","A10302.mat","A10303.mat","A10324.mat",
                            "A10333.mat","A10341.mat","A10363.mat","A10369.mat","A10394.mat",
                            "A10416.mat","A10430.mat","A10432.mat","A10441.mat","A10444.mat",
                            "A10452.mat","A10453.mat","A10454.mat","A10457.mat","A10458.mat",
                            "A10462.mat","A10464.mat","A10465.mat","A10466.mat","A10467.mat",
                            "A10469.mat","A10474.mat","A10476.mat","A10478.mat","A10479.mat",
                            "A10480.mat","A10482.mat","A10483.mat","A10486.mat","A10487.mat",
                            "A10806.mat","A10815.mat","A10843.mat","A10877.mat","A10888.mat",
                            "A10901.mat","A10904.mat","A10910.mat","A10954.mat","A10960.mat",
                            "A10970.mat","A10977.mat","A10978.mat","A10980.mat","A10984.mat",
                            "A10985.mat","A10986.mat","A10990.mat","A10992.mat","A11002.mat",
                            "A11004.mat","A11008.mat","A11015.mat","A11022.mat","A11023.mat",
                            "A11029.mat","A11031.mat","A11033.mat","A11035.mat","A11040.mat",
                            "A11043.mat","A11044.mat","A11045.mat","A11048.mat","A11052.mat",
                            "A11058.mat","A11059.mat","A11060.mat","A11063.mat","A11064.mat",
                            "A11068.mat","A11069.mat","A11071.mat","A11079.mat","A11080.mat",
                            "A11081.mat","A11102.mat","A11104.mat","A11109.mat","A11110.mat",
                            "A11111.mat","A11113.mat"};
        int nodeCnt = 8;
        for (int file = 0; file < files.length; file++) {
            String fName = files[file];
            Graph g = GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
                                            "C:/Users/manud/graph-data/deg-"+nodeCnt+"/" + fName);
            int intersectionNumber =  intersectionNumber(g);
            int nextCnt = 0;
            for (int i = 0; i < g.getNumberOfVertices(); i++) {
                for (int j = i+1; j < g.getNumberOfVertices(); j++) {
                    if (g.getEdge(i, j) == -1) {
                        Graph g2 = g.clone();
                        g2.insertEdge(i, j);
                        int intersectionNumber2 =  intersectionNumber(g2);
                        if (intersectionNumber2 < intersectionNumber) {
                            if (intersectionNumber - intersectionNumber2 > 1) {
                                System.out.println("match " + "fName: " + fName + " diff: " + 
                                                    (intersectionNumber - intersectionNumber2));
                            }
                            nextCnt++;
                        } 
                    }
                }
            }
            System.out.println(fName + ": " + nextCnt);
        }
    }
*/

/* 
    public static void main(String[] args) throws Exception {
        int nodeCnt = 8;
        int total = 11117;
        int graphCount = 0;
        int graphTotal = 0;        
        for (int graphNum = 4500; graphNum <= total; graphNum++) {
            if (graphNum % 50 == 0) {
                System.out.println(graphNum);
            }
			String fName = "A" + graphNum + ".mat";
			Graph g = GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
											"C:/Users/manud/graph-data/deg-"+nodeCnt+"/" + fName);
            if (14 <= g.getNumberOfEdges() && g.getNumberOfEdges() <= 25) {
                graphTotal++;
                int intersectionNumber =  intersectionNumber(g);
                boolean match = false;
                for (int i = 0; i < g.getNumberOfVertices(); i++) {
                    for (int j = i+1; j < g.getNumberOfVertices(); j++) {
                        if (g.getEdge(i, j) == -1) {
                            Graph g2 = g.clone();
                            g2.insertEdge(i, j);
                            int intersectionNumber2 =  intersectionNumber(g2);
                            if (intersectionNumber2 < intersectionNumber) {
                                match = true;
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
       // graph.insertEdge(5, 6);
        SpanningTreesMatsui s = new SpanningTreesMatsui(graph);
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
