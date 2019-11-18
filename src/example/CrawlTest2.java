package example;

import api.Graph;
import pa1.Crawler;
import pa1.WebGraph;

import java.util.LinkedList;

public class CrawlTest2 {
    public static void main(String[] args) {
        Crawler c = new Crawler("http://web.cs.iastate.edu/~smkautz/cs311f19/temp/a.html", 100, 100);
        Graph<String> graph = c.crawl();
        // WebGraph<String> wgraph = c.constructGraphBFS("http://web.cs.iastate.edu/~smkautz/cs311f19/temp/a.html", 100, 100);
        WebGraph.Node<String> node = new WebGraph.Node<String>("http://web.cs.iastate.edu/~smkautz/cs311f19/temp/a.html", 0);
    }
}
