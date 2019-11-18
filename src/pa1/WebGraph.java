package pa1;


import api.Graph;
import api.TaggedVertex;
import sun.awt.image.ImageWatched;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the Graph interface
 *
 * @author Stamatios Morellas (morellas@iastate.edu)
 * @author Jack Creighton (jackc1@iastate.edu)
 */
public class WebGraph<E> implements api.Graph {

    private int numNodes; // total number of vertices in the graph
    private HashMap<Node<E>, LinkedList<Node<E>>> graphMap; // each node maps to a list of all its neighbors

    // Constructor
    public WebGraph(int numNodes) {
        this.numNodes = numNodes;
        graphMap = new HashMap<>();
    }

    /**
     * Class for Node object with data of type E
     * @param <E>
     */
    public static class Node<E> {
        E data;
        int index; // The index of the specific node
        int depth; // The depth of the node

        // Constructor
        public Node(E data, int index) {
            this.data = data;
            this.index = index;
            depth = 0;
        }

        public E getData(){
            return data;
        }
        public int getIndex() {
            return index;
        }
        public int getDepth(){
            return depth;
        }
        public int setDepth(int newDepth) {
            depth = newDepth;
            return depth;
        }
    }

    // USER METHODS

    /**
     * Return a node from the HashMap with the given index
     * @param index
     * @return
     */
    public Node<E> findNode(int index) {
        Node<E> desired = null;
        for (Node<E> n : graphMap.keySet()) {
            if (n.getIndex() == index) {
                desired = n;
                break;
            }
        }
        return desired;
    }

    /**
     * Helper method for addEdge()
     * @param src
     * @param dest
     */
    public void addEdgeHelper(Node<E> src, Node<E> dest) {
        LinkedList<Node<E>> tmp = graphMap.get(src);

        if (tmp != null)
            tmp.remove(dest);
        else
            tmp = new LinkedList<>();
            tmp.add(dest);
            graphMap.put(src, tmp);
    }

    /**
     * Add an edge from source node to destination node and store it in the hashmap
     * @param source
     * @param destination
     */
    public void addEdge(Node<E> source, Node<E> destination) {
        if (!graphMap.containsKey(source)) {
            graphMap.put(source, new LinkedList<Node<E>>());
        }
        else if (!graphMap.containsKey(destination)) {
            graphMap.put(destination, new LinkedList<Node<E>>());
            addEdgeHelper(source, destination);
        }
        else {
            addEdgeHelper(source, destination);
        }
    }

    /**
     * Returns true if this graph has an edge from Node src to Node dest
     * @param src
     * @param dest
     * @return
     *          true if an edge exists
     *          false if this edge is not in the graph
     */
    public boolean hasEdge(Node<E> src, Node<E> dest) {
        return graphMap.containsKey(src) && graphMap.get(src).contains(dest);
    }

    /**
     * Returns the graphMap for this WebGraph
     * @return
     */
    public HashMap<Node<E>, LinkedList<Node<E>>> getGraphMap() {
        return graphMap;
    }

    /**
     * Returns the number of nodes that are supposed to be in this graph
     * @return
     */
    public int getNumNodes() {
        return numNodes;
    }

    // INTERFACE METHODS

    /**
     * Returns an ArrayList of the actual objects constituting the vertices
     * of this graph.
     * @return
     *   ArrayList of objects in the graph
     */
    public ArrayList<E> vertexData() {
        ArrayList<E> vd = new ArrayList<E>();
        for (Node<E> node : graphMap.keySet()) { // for each node in the graph
            vd.add(node.getData()); // add the data to our vdata array
        }
        return vd;
    }

    /**
     * Returns an ArrayList that is identical to that returned by vertexData(), except
     * that each vertex is associated with its incoming edge count.
     * @return
     *   ArrayList of objects in the graph, each associated with its incoming edge count
     */
    public ArrayList<TaggedVertex<E>> vertexDataWithIncomingCounts() {
        ArrayList<TaggedVertex<E>> vdic = new ArrayList<TaggedVertex<E>>();
        for (Node<E> node : graphMap.keySet()) { // for each node in the graph
            vdic.add(new TaggedVertex<E>(node.getData(), getIncoming(node.getIndex()).size()));
        }
        return vdic;
    }

    /**
     * Returns a list of outgoing edges, that is, a list of indices for neighbors
     * of the vertex with given index.
     * This method may throw ArrayIndexOutOfBoundsException if the index
     * is invalid.
     * @param index
     *   index of the given vertex according to vertexData()
     * @return
     *   list of outgoing edges
     */
    public List<Integer> getNeighbors(int index) throws ArrayIndexOutOfBoundsException {
        ArrayList<Integer> outgoing = new ArrayList<Integer>(); // The list to return
        // Get the node at the desired index
        Node<E> desired = null;
        for (Node<E> node : graphMap.keySet()) {
            if (node.getIndex() == index) {
                desired = node;
            }
        }
        // Add indices of neighboring nodes to a return list
        if (desired != null) {
            LinkedList<Node<E>> neighbors = graphMap.get(desired); // The list of outgoing edges from the desired node
            for (Node<E> n : neighbors) {
                outgoing.add(n.getIndex());
            }
        }
        return outgoing;
    }

    /**
     * Returns a list of incoming edges, that is, a list of indices for vertices
     * having the given vertex as a neighbor.
     * This method may throw ArrayIndexOutOfBoundsException if the index
     * is invalid.
     * @param index
     *   index of the given vertex according to vertexData()
     * @return
     *   list of incoming edges
     */
    public List<Integer> getIncoming(int index) {
        ArrayList<Integer> incoming = new ArrayList<Integer>(); // The list to return
        // Get node at the desired index
        Node<E> desired = null;
        for (Node<E> node : graphMap.keySet()) {
            if (node.getIndex() == index) {
                desired = node;
            }
        }
        // Find how many times the index of this node appears in outgoing lists of other nodes
        if (desired != null) {
            for (Node<E> currentNode : graphMap.keySet()) { // for each node in the graph
                LinkedList<Node<E>> outgoing = graphMap.get(currentNode); // get the list of outgoing nodes from current node
                for (Node<E> current : outgoing) {
                    if (current.getIndex() == desired.getIndex()) {
                        incoming.add(currentNode.getIndex()); // Add the index of the current node to the return list
                    }
                }
            }
        }
        return incoming;
    }
}


















