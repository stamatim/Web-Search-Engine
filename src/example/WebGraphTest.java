package example;

import com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput;
import pa1.WebGraph;
import api.TaggedVertex;

import java.util.LinkedList;

public class WebGraphTest {

    /**
     * Note: This test is based on the information found in this Piazza post
     *
     * https://piazza.com/class/jzpxqej11zo633?cid=434
     *
     */

    public static void main(String[] args) {

        // Construct a new web graph
        WebGraph<String> graph = new WebGraph<String>(10);

        // Add nodes to the graph
        graph.getGraphMap().put(new WebGraph.Node<String>("http://web.cs.iastate.edu/~smkautz/cs311f19/temp/a.html", 0), new LinkedList<>());
        graph.getGraphMap().put(new WebGraph.Node<String>("http://web.cs.iastate.edu/~smkautz/cs311f19/temp/b.html", 1), new LinkedList<>());
        graph.getGraphMap().put(new WebGraph.Node<String>("http://web.cs.iastate.edu/~smkautz/cs311f19/temp/c.html", 2), new LinkedList<>());
        graph.getGraphMap().put(new WebGraph.Node<String>("http://web.cs.iastate.edu/~smkautz/cs311f19/temp/d.html", 3), new LinkedList<>());
        graph.getGraphMap().put(new WebGraph.Node<String>("http://web.cs.iastate.edu/~smkautz/cs311f19/temp/i.html", 4), new LinkedList<>());
        graph.getGraphMap().put(new WebGraph.Node<String>("http://web.cs.iastate.edu/~smkautz/cs311f19/temp/j.html", 5), new LinkedList<>());
        graph.getGraphMap().put(new WebGraph.Node<String>("http://web.cs.iastate.edu/~smkautz/cs311f19/temp/e.html", 6), new LinkedList<>());
        graph.getGraphMap().put(new WebGraph.Node<String>("http://web.cs.iastate.edu/~smkautz/cs311f19/temp/f.html", 7), new LinkedList<>());
        graph.getGraphMap().put(new WebGraph.Node<String>("http://web.cs.iastate.edu/~smkautz/cs311f19/temp/g.html", 8), new LinkedList<>());
        graph.getGraphMap().put(new WebGraph.Node<String>("http://web.cs.iastate.edu/~smkautz/cs311f19/temp/h.html", 9), new LinkedList<>());

        // Link the nodes to each other
        graph.addEdge(graph.findNode(0), graph.findNode(1));
        graph.addEdge(graph.findNode(0), graph.findNode(2));
        graph.addEdge(graph.findNode(0), graph.findNode(3));
        graph.addEdge(graph.findNode(1), graph.findNode(2));
        graph.addEdge(graph.findNode(1), graph.findNode(4));
        graph.addEdge(graph.findNode(1), graph.findNode(5));
        graph.addEdge(graph.findNode(2), graph.findNode(1));
        graph.addEdge(graph.findNode(2), graph.findNode(3));
        graph.addEdge(graph.findNode(2), graph.findNode(6));
        graph.addEdge(graph.findNode(2), graph.findNode(7));
        graph.addEdge(graph.findNode(3), graph.findNode(0));
        graph.addEdge(graph.findNode(3), graph.findNode(8));
        graph.addEdge(graph.findNode(3), graph.findNode(9));
        graph.addEdge(graph.findNode(6), graph.findNode(0));

        // Get the vertex data for every node
        System.out.println(" ------------- VERTEX DATA ------------- ");
        System.out.println(graph.vertexData());

        // Formatting
        System.out.println();
        System.out.println();

        // Get the vertex data with incoming counts for every node
        System.out.println(" --- VERTEX DATA WITH INCOMING COUNT --- ");
        System.out.println(graph.vertexDataWithIncomingCounts());
        for (TaggedVertex<String> tv : graph.vertexDataWithIncomingCounts()) {
            System.out.println("Tagged vertex " + tv + " has data with value " + tv.getVertexData() + " and in-degree " + tv.getTagValue());
        }

        // Formatting
        System.out.println();
        System.out.println();

        // Get the indices of the neighboring (outgoing) nodes
        for (WebGraph.Node<String> node : graph.getGraphMap().keySet()) {
            System.out.print("Outgoing neighbors of node " + node.getData() + " (Index " + node.getIndex() +  "): ");
            System.out.println(graph.getNeighbors(node.getIndex()));
        }

        // Formatting
        System.out.println();
        System.out.println();

        // Get the indices of the neighboring (incoming) nodes
        for (WebGraph.Node<String> node : graph.getGraphMap().keySet()) {
            System.out.print("Incoming neighbors of node " + node.getData() + " (Index " + node.getIndex() +  "): ");
            System.out.println(graph.getIncoming(node.getIndex()));
        }

        // Formatting
        System.out.println();
        System.out.println();
    }
}








