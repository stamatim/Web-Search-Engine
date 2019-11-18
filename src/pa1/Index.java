package pa1;
import java.io.IOException;
import java.util.*;

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
import sun.rmi.server.InactiveGroupException;

import javax.swing.text.html.HTML;

/**
 * Implementation of an inverted index for a web graph.
 *
 * @author Stamatios Morellas (morellas@iastate.edu)
 * @author Jack Creighton (jackc1@iastate.edu)
 */
public class Index {

    private List<TaggedVertex<String>> urls; // The index of urls
    private Map<String, List<Pair<String, Integer>>> I; // Store each unique word in a HashMap linked to a list of tuples consisting of <url the string was found in, number of appearances in that url>

    /**
     * Custom Comparator class used for sorting in the search methods
     */
    public static class CustomComparator implements Comparator<TaggedVertex<String>> {
        @Override
        public int compare(TaggedVertex<String> v1, TaggedVertex<String> v2) {
            return Integer.compare(v2.getTagValue(), v1.getTagValue());
        }
    }

    /**
     * Constructs an index from the given list of urls.  The
     * tag value for each url is the indegree of the corresponding
     * node in the graph to be indexed.
     * @param urls
     *   information about graph to be indexed
     */
    public Index(List<TaggedVertex<String>> urls) {
        this.urls = urls;
        I = new HashMap<>();
    }

    /**
     * Creates the index.
     */
    public void makeIndex() throws IOException {
        try {
            int requestCounter = 0;

            // 1. Let S be a collection of urls
            List<TaggedVertex<String>> S = urls;

            // Construct W
            for (TaggedVertex<String> url : S) { // for every url in the collection

                // 2. Let W be a list of all the words appearing in the body text of these pages excluding stop words
                Map<String, Integer> W = new HashMap<String, Integer>(); // a list of words in a specific url mapped to the number of times they appear

                String bodyText = Jsoup.connect(url.getVertexData()).get().body().text(); // the body text in string format for each url
                Scanner sc = new Scanner(bodyText); // Create a scanner to read through the body test
                List<String> wordsInUrl = new ArrayList<String>(); // a list of words in the body text from this url

                // Politeness policy check
                requestCounter++;
                if (requestCounter % 50 == 0) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ignore) {
                        // Do nothing
                    }
                }

                do {
                    String current = Util.stripPunctuation(sc.next()); // Get each word from body text
                    if (!Util.isStopWord(current) && !W.containsKey(current)) { // check if word is STOP word and is NOT in the list
                        W.put(current, 1); // Add the word as a new instance to W
                    } else if (!Util.isStopWord(current) && W.containsKey(current)) { // check if word is STOP word and IS in the list
                        W.put(current, W.get(current) + 1);
                    }
                    if (!Util.isStopWord(current) && !wordsInUrl.contains(current)) {
                        wordsInUrl.add(current);
                    }
                } while (sc.hasNext());


                // Update the list of pairs for the HashMap
                for (String u : wordsInUrl) {
                    if (I.containsKey(u)) {
                        List<Pair<String, Integer>> listW = I.get(u);
                        Pair<String, Integer> pair = new Pair<String, Integer>(url.getVertexData(), W.get(u));
                        if (!(listW == null)) {
                            listW.add(pair);
                            I.put(u, listW);
                        }
                    }
                    else {
                        List<Pair<String, Integer>> listW = new ArrayList<Pair<String, Integer>>();
                        Pair<String, Integer> pair = new Pair<String, Integer>(url.getVertexData(), W.get(u));
                        listW.add(pair);
                        I.put(u, listW);
                    }
                }
                sc.close(); // Close the scanner
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Searches the index for pages containing keyword w.  Returns a list
     * of urls ordered by ranking (largest to smallest).  The tag
     * value associated with each url is its ranking.
     * The ranking for a given page is the number of occurrences
     * of the keyword multiplied by the indegree of its url in
     * the associated graph.  No pages with rank zero are included.
     * @param w
     *   keyword to search for
     * @return
     *   ranked list of urls
     */
    public List<TaggedVertex<String>> search(String w) {
        List<TaggedVertex<String>> toReturn = new ArrayList<TaggedVertex<String>>(); // Make a new List to return

        // Make a list of tuples
        List<Pair<String, Integer>> appearances = I.get(w); // The appearances of string w in the HashMap

        // Update the tag value of each vertex to be saved into toReturn
        int listIndex = 0;
        for (TaggedVertex<String> url : urls) {
            if (listIndex >= appearances.size()) {
                break;
            }
            if (!url.getVertexData().equals(appearances.get(listIndex).getKey())) {
                continue;
            }
            TaggedVertex<String> t = new TaggedVertex<String>(url.getVertexData(), url.getTagValue() * appearances.get(listIndex).getValue()); // Create new TaggedVertex
            toReturn.add(t);
            listIndex++;
        }

        // Sort the list with the custom comparator
        toReturn.sort(new CustomComparator());

        return toReturn;
    }


    /**
     * Searches the index for pages containing both of the keywords w1
     * and w2.  Returns a list of qualifying
     * urls ordered by ranking (largest to smallest).  The tag
     * value associated with each url is its ranking.
     * The ranking for a given page is the number of occurrences
     * of w1 plus number of occurrences of w2, all multiplied by the
     * indegree of its url in the associated graph.
     * No pages with rank zero are included.
     * @param w1
     *   first keyword to search for
     * @param w2
     *   second keyword to search for
     * @return
     *   ranked list of urls
     */
    public List<TaggedVertex<String>> searchWithAnd(String w1, String w2) {
        List<TaggedVertex<String>> toReturn = new ArrayList<TaggedVertex<String>>(); // Make a new List to return

        List<TaggedVertex<String>> search1 = search(w1);
        List<TaggedVertex<String>> search2 = search(w2);

        for (TaggedVertex<String> s1 : search1) {
            for (TaggedVertex<String> s2 : search2) {
                if (s1.getVertexData().equals(s2.getVertexData())) {
                    TaggedVertex<String> toAdd = new TaggedVertex<String>(s1.getVertexData(), s1.getTagValue() + s2.getTagValue());
                    toReturn.add(toAdd);
                }
            }
        }

        // Sort the list with the custom comparator
        toReturn.sort(new CustomComparator());

        return toReturn;
    }

    /**
     * Searches the index for pages containing at least one of the keywords w1
     * and w2.  Returns a list of qualifying
     * urls ordered by ranking (largest to smallest).  The tag
     * value associated with each url is its ranking.
     * The ranking for a given page is the number of occurrences
     * of w1 plus number of occurrences of w2, all multiplied by the
     * indegree of its url in the associated graph.
     * No pages with rank zero are included.
     * @param w1
     *   first keyword to search for
     * @param w2
     *   second keyword to search for
     * @return
     *   ranked list of urls
     */
    public List<TaggedVertex<String>> searchWithOr(String w1, String w2) { // FIXME
        List<TaggedVertex<String>> toReturn = new ArrayList<TaggedVertex<String>>(); // Make a new List to return

        List<TaggedVertex<String>> search1 = search(w1);
        int size1 = search1.size();
        List<TaggedVertex<String>> search2 = search(w2);
        int size2 = search2.size();

        List<String> added = new ArrayList<>(); // Keep track of which strings have been added

        for (TaggedVertex<String> s1 : search1) {
            for (TaggedVertex<String> s2 : search2) {
                if (s1.getVertexData().equals(s2.getVertexData())) {
                    TaggedVertex<String> toAdd = new TaggedVertex<String>(s1.getVertexData(), s1.getTagValue() + s2.getTagValue());
                    if (!toReturn.contains(toAdd))
                        toReturn.add(toAdd);
                        added.add(toAdd.getVertexData());
                        break;
                } else {
                    TaggedVertex<String> toAdd1 = new TaggedVertex<String>(s1.getVertexData(), s1.getTagValue());
                    TaggedVertex<String> toAdd2 = new TaggedVertex<String>(s2.getVertexData(), s1.getTagValue());
                    if (!toReturn.contains(toAdd1) && !added.contains(toAdd1.getVertexData()))
                        toReturn.add(toAdd1);
                        added.add(toAdd1.getVertexData());
                    if (!toReturn.contains(toAdd2) && !added.contains(toAdd2.getVertexData()))
                        toReturn.add(toAdd2);
                        added.add(toAdd2.getVertexData());
                        break;
                }
            }
        }

        // Sort the list with the custom comparator
        toReturn.sort(new CustomComparator());

        return toReturn;
    }

    /**
     * Searches the index for pages containing keyword w1
     * but NOT w2.  Returns a list of qualifying urls
     * ordered by ranking (largest to smallest).  The tag
     * value associated with each url is its ranking.
     * The ranking for a given page is the number of occurrences
     * of w1, multiplied by the
     * indegree of its url in the associated graph.
     * No pages with rank zero are included.
     * @param w1
     *   first keyword to search for
     * @param w2
     *   second keyword to search for
     * @return
     *   ranked list of urls
     */
    public List<TaggedVertex<String>> searchAndNot(String w1, String w2) {
        List<TaggedVertex<String>> toReturn = new ArrayList<TaggedVertex<String>>(); // Make a new List to return

        List<TaggedVertex<String>> search1 = search(w1);
        List<TaggedVertex<String>> search2 = search(w2);
        List<String> seen = new ArrayList<>();

        for (TaggedVertex<String> s1 : search1) {
            for (TaggedVertex<String> s2 : search2) {
                seen.add(s2.getVertexData());

                if (!seen.contains(s1.getVertexData())) {
                    TaggedVertex<String> toAdd = new TaggedVertex<String>(s1.getVertexData(), s1.getTagValue());
                    toReturn.add(toAdd);
                    seen.add(s1.getVertexData());
                }
            }
        }

        // Sort the list with the custom comparator
        toReturn.sort(new CustomComparator());

        return toReturn;
    }
}
