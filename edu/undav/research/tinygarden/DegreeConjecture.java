import edu.undav.research.tinygarden.GraphTarjanRead;
import edu.undav.research.tinygarden.Item;
import edu.undav.research.tinygarden.SpanningTreesMatsui;
import edu.undav.research.tinygarden.BasicClasses.Graph;
import edu.undav.research.tinygarden.BasicClasses.VecInt;
import edu.undav.research.tinygarden.SpanningTreesMatsui.LABILogger;
import edu.undav.research.tinygarden.SpanningTreesMatsuiCollector.MaxDegreeCollector;
import edu.undav.research.tinygarden.SpanningTreesMatsuiProcessor.IntersectionNumberProcessor;


public class DegreeConjecture {
 /*    
 
    public static void main(String[] args) throws Exception {
        int nodeCnt = 7;
        int total = 853;
        MaxDegreeCollector collector;
		LABILogger log = new LABILogger("C:/Users/manud/graph-data/stats/deg-" + nodeCnt + "-degree_conjecture.csv");

        for (int graphNum = 1; graphNum <= total; graphNum++) {
			String fName = "A" + graphNum + ".mat";
			Graph g = GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
											"C:/Users/manud/graph-data/deg-"+nodeCnt+"/" + fName);
            int intersectionNumber =  SpanningTreesMatsui.intersectionNumber(g);
            SpanningTreesMatsui s = new SpanningTreesMatsui(g);
            collector = new MaxDegreeCollector(s, fName, log, intersectionNumber);
            s.allSpanningTrees();
            collector.postProcess();
        }
        log.flush();
    }

*/
 
    public static void main(String[] args) throws Exception {
        int nodeCnt = 6;
        int graphNum = 84;
        String fName = "A" + graphNum + ".mat";
        Graph g = GraphTarjanRead.buildFromAdjMatrix(nodeCnt, 
                                        "C:/Users/manud/graph-data/deg-"+nodeCnt+"/" + fName);
        VecInt data = new VecInt();
        data.pushBack(SpanningTreesMatsui.intersectionNumber(g));
        Item item = new Item(fName,data);
        SpanningTreesMatsui s = new SpanningTreesMatsui(g);
        IntersectionNumberProcessor processor = new IntersectionNumberProcessor(s, item,
                                                        "C:/Users/manud/graph-data/graphviz/deg-5/" + graphNum + "/");
        s.allSpanningTrees();
    }

}
