package example;
import java.io.IOException;
import java.util.*;

import api.Graph;
import api.TaggedVertex;
import api.Util;
import com.sun.org.apache.xerces.internal.xs.StringList;
import javafx.util.Pair;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pa1.Crawler;
import pa1.Index;
import sun.rmi.server.InactiveGroupException;

import javax.swing.text.html.HTML;

public class IndexTest {
    public static void main(String[] args) {

        Crawler c = new Crawler("http://web.cs.iastate.edu/~smkautz/cs311f19/temp/a.html", 100, 100); // Make Crawler
        Graph<String> g = c.crawl(); // Obtain Graph
        List<TaggedVertex<String>> urls = g.vertexDataWithIncomingCounts(); // Get urls as tagged vertices
        Index index = new Index(urls);

        // Test making the index
        try {
            index.makeIndex();
        } catch (IOException e) {
            System.out.print(e.toString());
        }

        System.out.println();
        System.out.println();

        // Test the search methods
        System.out.println("Search Test:");
        List<TaggedVertex<String>> test1 = index.search("chicken");
        for (TaggedVertex<String> t1 : test1) {
            System.out.println("Tagged vertex " + t1 + " has data with value " + t1.getVertexData() + " and tag value " + t1.getTagValue());
        }

        System.out.println();
        System.out.println();

        System.out.println("Search Test AND:");
        List<TaggedVertex<String>> test2 = index.searchWithAnd("chicken", "long");
        for (TaggedVertex<String> t2 : test2) {
            System.out.println("Tagged vertex " + t2 + " has data with value " + t2.getVertexData() + " and tag value " + t2.getTagValue());
        }

        System.out.println();
        System.out.println();

        System.out.println("Search Test OR:");
        List<TaggedVertex<String>> test3 = index.searchWithOr("chicken", "vanilla");
        for (TaggedVertex<String> t3 : test3) {
            System.out.println("Tagged vertex " + t3 + " has data with value " + t3.getVertexData() + " and tag value " + t3.getTagValue());
        }

        System.out.println();
        System.out.println();

        System.out.println("Search Test AND NOT:");
        List<TaggedVertex<String>> test4 = index.searchAndNot("chicken", "vanilla");
        for (TaggedVertex<String> t4 : test4) {
            System.out.println("Tagged vertex " + t4 + " has data with value " + t4.getVertexData() + " and tag value " + t4.getTagValue());
        }
    }
}












