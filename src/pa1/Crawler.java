package pa1;
import api.TaggedVertex;

import java.io.IOException;
import java.util.*;

import api.Graph;
import api.Util;
import org.jsoup.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pa1.WebGraph.Node;
import sun.awt.image.ImageWatched;

import static com.sun.activation.registries.LogSupport.log;

/**
 * Implementation of a basic web crawler that creates a graph of some
 * portion of the world wide web.
 *
 * @author Stamatios Morellas (morellas@iastate.edu)
 * @author Jack Creighton (jackc1@iastate.edu)
 */
public class Crawler {
    // Class attributes
    private String seedUrl;
    private int maxDepth;
    private int maxPages;

    /**
     * Constructs a Crawler that will start with the given seed url, including
     * only up to maxPages pages at distance up to maxDepth from the seed url.
     * @param seedUrl
     * @param maxDepth
     * @param maxPages
     */
    public Crawler(String seedUrl, int maxDepth, int maxPages) {
        // Set initial values
        this.seedUrl = seedUrl;
        this.maxDepth = maxDepth;
        this.maxPages = maxPages;
    }

    /**
     * Creates a web graph for the portion of the web obtained by a BFS of the
     * web starting with the seed url for this object, subject to the restrictions
     * implied by maxDepth and maxPages.
     * @return
     *   an instance of Graph representing this portion of the web
     */
    public Graph<String> crawl() {
        try {
            WebGraph<String> webgraph = constructGraphBFS(seedUrl, maxDepth, maxPages);
            Graph<String> graph = webgraph;
            return webgraph;
        } catch (IOException e) {
            System.out.print(e.toString());
        }
        return null;
    }

//    public WebGraph<String> constructWebGraph

    /**
     * Get the BFS graph of the entire WebGraph
     * @param seedUrl
     *
     */
    private WebGraph<String> constructGraphBFS(String seedUrl, int maxDepth, int maxPages) throws IOException, org.jsoup.UnsupportedMimeTypeException, org.jsoup.HttpStatusException {
        try {
            // Construct a graph that will be returned
            WebGraph<String> graph = new WebGraph<String>(maxPages);

            int indexCounter = 0; // Index counter to keep track of the creation of nodes

            // 1. Input: seedUrl
            Node<String> root = new Node<String>(seedUrl, indexCounter); // The root node for this crawler

            // 2. Initialize a FIFO queue and a list discovered
            Queue<Node<String>> queue = new LinkedList<Node<String>>();
            List<String> discovered = new ArrayList<String>();

            // 3. Place seedUrl in queue and in discovered
            queue.add(root);
            discovered.add(root.getData());
            graph.getGraphMap().put(root, new LinkedList<Node<String>>()); // Add the node to the web graph

            int requestCounter = 0; // Counter for the number of requests

            // 4. Run BFS to visit all the pages that are reachable from the seedUrl
            do {

                // a. Remove first element in the queue
                Node<String> currentPage = queue.remove();

                // b. Send a request to server at currentPage and download currentPage
                Document doc = Jsoup.connect(currentPage.getData()).get();
                requestCounter++; // Update request counter
                if (requestCounter % 50 == 0) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ignore) {
                        // Do nothing
                    }
                }

                // c. Extract all links from currentPage
                Elements links = doc.select("a[href]");

                // d. For every element that appears in currentPage
                for (Element link : links) {

                    // get the href in the form of an absolute url
                    String v = link.attr("abs:href");
                    System.out.println("Found: " + v);

                    // make sure it's a non-bookmark link with a valid MIME type
                    Document temp = null;
                    if (!Util.ignoreLink(currentPage.getData(), v)) {
                        try {
                            temp = Jsoup.connect(v).get();
                        }
                        catch (UnsupportedMimeTypeException e) {
                            System.out.println("--unsupported document type, do nothing");
                        }
                        catch (HttpStatusException e) {
                            System.out.println("--invalid link, do nothing");
                        }
                    }
                    else {
                        System.out.println("--ignore");
                    }

                    if (!discovered.contains(v)) {
                        indexCounter++;
                        Node<String> u = new Node<String>(v, indexCounter);
                        queue.add(u); // Add u to the end of the queue
                        discovered.add(u.getData()); // Add u to discovered
                        u.setDepth(currentPage.getDepth() + 1); // Update the depth of the node
                        // Check to make sure the size of the discovered list is not out of bounds
                        if (discovered.size() > maxPages || u.getDepth() > maxDepth) {
                            return graph;
                        }
                        graph.addEdge(currentPage, u);
                    } else {
                        Node<String> toLink = new Node<String>("", -1);
                        for (Node<String> n : graph.getGraphMap().keySet()) {
                            if (n.getData().equals(v)) {
                                toLink = n;
                                break;
                            }
                        }
                        graph.addEdge(currentPage, toLink);
                    }
                }

            } while(!queue.isEmpty()); // While queue is not empty

            return graph;

        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return null;
    }
}
