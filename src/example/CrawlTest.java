package example;

import api.Graph;
import com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput;
import pa1.Crawler;
import pa1.WebGraph;
import api.TaggedVertex;
import pa1.WebGraph.Node;

import java.io.IOException;
import java.util.LinkedList;

import java.util.LinkedList;

public class CrawlTest {
    public static void main(String[] args) {
        // Construct a new crawler
        Crawler c = new Crawler("http://web.cs.iastate.edu/~smkautz/cs311f19/temp/a.html", 100, 100);

        // Graph<String> g = c.crawl();

        // Construct a graph from the BFS

            Graph<String> graph = c.crawl();


            // Formatting
            System.out.println();
            System.out.println();

            // Get the vertex data for every node
            System.out.println(" -------------------- VERTEX DATA -------------------- ");
            System.out.println(graph.vertexData());

            // Formatting
            System.out.println();
            System.out.println();

            // Get the vertex data with incoming counts for every node
            System.out.println(" ---------- VERTEX DATA WITH INCOMING COUNT ---------- ");
            System.out.println(graph.vertexDataWithIncomingCounts());

            // Formatting
            System.out.println();
            System.out.println();

            for (TaggedVertex<String> tv : graph.vertexDataWithIncomingCounts()) {
                System.out.println("Tagged vertex " + tv + " has data with value " + tv.getVertexData() + " and in-degree " + tv.getTagValue());
            }

            // Formatting
            System.out.println();
            System.out.println();

//            // Get the indices of the neighboring (outgoing) nodes
//            for (WebGraph.Node<String> node : graph.getGraphMap().keySet()) {
//                System.out.print("Outgoing neighbors of node " + node.getData() + " (Index " + node.getIndex() +  "): ");
//                System.out.println(graph.getNeighbors(node.getIndex()));
//            }
//
//            // Formatting
//            System.out.println();
//            System.out.println();
//
//            // Get the indices of the neighboring (incoming) nodes
//            for (Object node : graph.getGraphMap().keySet()) {
//                System.out.print("Incoming neighbors of node " + node.getData() + " (Index " + node.getIndex() +  "): ");
//                System.out.println(graph.getIncoming(node.getIndex()));
//            }

            // Formatting
            System.out.println();
            System.out.println();

    }
}
