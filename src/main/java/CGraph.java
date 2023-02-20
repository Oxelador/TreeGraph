import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;

public class CGraph {
    private ACell adjList;
    private ArrayList<String> input;

    public CGraph(ArrayList<String> input) throws IOException {
        this.input = input;
        adjList = null;
        for(String s : input) {
            NCell nlist, ptr = null;
            StringTokenizer toks = new StringTokenizer(s);
            String first = toks.nextToken();
            if (toks.hasMoreTokens()) {
                String node = toks.nextToken();
                int cost = Integer.parseInt(toks.nextToken());
                nlist = new NCell(node, cost);
                ptr = nlist;
            } else {
                nlist = null;
            }
            while (toks.hasMoreTokens()) {
                String node = toks.nextToken();
                int cost = Integer.parseInt(toks.nextToken());
                ptr.next = new NCell(node, cost);
                ptr = ptr.next;
            }
            adjList = new ACell(first, nlist, adjList);
        }
    }

    public Enumeration getLinks(String from) {
        return new NextNodes(from);
    }

    private static class NCell {
        String first;
        int cost;
        NCell next;

        NCell(String f, int c) {
            first = f;
            cost = c;
            next = null;
        }
    }

    private static class ACell {
        String from;
        NCell to;
        ACell next;

        ACell(String f, NCell t, ACell n) {
            from = f;
            to = t;
            next = n;
        }
    }

    private class NextNodes implements Enumeration {
        NCell nlist;
        String from;

        NextNodes(String f) {
            from = f;
            ACell ptr = adjList;
            for (; ptr != null && !ptr.from.equals(from); ptr = ptr.next) {
                if (ptr == null) {
                    nlist = null;
                } else {
                    nlist = ptr.to;
                }
            }
        }

        @Override
        public boolean hasMoreElements() {
            return nlist != null;
        }

        @Override
        public Object nextElement() {
            String to = nlist.first;
            int c = nlist.cost;
            nlist = nlist.next;
            return new City(from, to, c);
        }
    }
}
