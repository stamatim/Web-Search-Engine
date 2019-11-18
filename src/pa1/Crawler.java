package pa1;
import api.Graph;

/**
 * Implementation of a basic web crawler that creates a graph of some
 * portion of the world wide web.
 *
 * @author Stamatios Morellas (morellas@iastate.edu)
 * @author Jack Creighton (jackc1@iastate.edu)
 */
public class Crawler {
    // Class attributes
    public String seedUrl;
    public int maxDepth;
    public int maxPages;

    /**
     * Constructs a Crawler that will start with the given seed url, including
     * only up to maxPages pages at distance up to maxDepth from the seed url.
     * @param seedUrl
     * @param maxDepth
     * @param maxPages
     */
    public Crawler(String seedUrl, int maxDepth, int maxPages) {
        // Set initial values
        seedUrl = this.seedUrl;
        maxDepth = this.maxDepth;
        maxPages = this.maxPages;
    }

    /**
     * Creates a web graph for the portion of the web obtained by a BFS of the
     * web starting with the seed url for this object, subject to the restrictions
     * implied by maxDepth and maxPages.
     * @return
     *   an instance of Graph representing this portion of the web
     */
    public Graph<String> crawl() {
        // TODO
        return null;
    }
}
