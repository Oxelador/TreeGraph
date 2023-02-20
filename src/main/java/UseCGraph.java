import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class UseCGraph {
    // Costed path search, depth-first, search all paths for shortest
// Uses branch-and-bound
    public static int cityCount;
    public static List<String> input = new ArrayList<>();
    public static List<String> requests = new ArrayList<>();
    public static ArrayList<String> cityNamesFromInputFile = new ArrayList<>();
    public static HashMap<Integer, String> cities = new HashMap<>();
    public static ArrayList<String> inputForGraph = new ArrayList<>();
    public static ArrayList<String> result = new ArrayList<>();
    private static int count;
    private static Path best;

    public static void main(String[] args) throws IOException {
        String path = "./src/main/java/input.txt";
        File inputFile = new File(path);
        Scanner scanner = new Scanner(inputFile);
        while (scanner.hasNextLine()) {
            input.add(scanner.nextLine());
        }

        cityCount = Integer.parseInt(input.get(0));
        input.remove(0);

        int mapKeyCount = 1;
        for (String s : input) {
            if (s.matches("^[a-zA-Z]*$")) {
                cityNamesFromInputFile.add(s);
                cities.put(mapKeyCount++, s);
            }
        }

        scanner.close();

        int request = 0;
        boolean requestFlag = false;
        for (int i = input.size() - 1; i > 0; i--) {
            if (!requestFlag) {
                if (input.get(i).length() == 1) {
                    request = Integer.parseInt(input.get(i));
                    input.remove(i);
                    requestFlag = true;
                }
            }
        }
        for (int j = input.size() - 1; j > 0; j--) {
            if (request != 0) {
                requests.add(input.get(j));
                request--;
                input.remove(j);
            }
        }

        Collections.reverse(requests);

        StringBuilder builder = new StringBuilder();
        int childrenCount = 0;
        for (int k = 0; k < input.size(); k++) {
            if (input.get(k).matches("^[a-zA-Z]*$")) {
                builder.append(input.get(k)).append(" ");
            }
            if (input.get(k).length() == 1) {
                childrenCount = Integer.parseInt(input.get(k));
            }
            if (input.get(k).length() == 3) {
                int[] tempArr = Arrays.stream(input.get(k).split(" ")).mapToInt(Integer::parseInt).toArray();
                builder.append(cities.get(tempArr[0])).append(" ").append(tempArr[1]).append(" ");
                childrenCount--;
                if (childrenCount == 0) {
                    inputForGraph.add(builder.toString());
                    builder.setLength(0);
                }
            }
        }
        CGraph graph = new CGraph(inputForGraph);

        String outPutPath = "./src/main/java/output.txt";
        File outPutFile = new File(outPutPath);
        FileWriter writer = new FileWriter(outPutFile);
        count = 0;
        int requestCount = requests.size();
        while (requestCount != 0) {
            for (int i = 0; i < requests.size(); i++) {
                String[] tempArr = requests.get(i).split(" ");
                writer.write(path(tempArr[0], tempArr[1], graph) + "\n");
                writer.flush();
                requestCount--;
            }
        }
    }

    public static String path(String start, String finish, CGraph graph) {
        best = null;
        findPath(finish, graph, new Path(new Cell(start, null), 0));
        if (best == null)
            return "No path possible";
        else {
            Cell p = best.getRoute();
            String str = p.first;
            p = p.next;
            while (p != null) {
                str = p.first + ", " + str;
                p = p.next;
            }
            return "Cost: "+best.getCost()+"  Route: "+str;
        }
    }

    private static void findPath(String finish, CGraph graph, Path done) {
        count++;
        if (done.getRoute().first.equals(finish)) {
            if (best == null || done.getCost() < best.getCost())
                best = done;
        } else {
            Enumeration enumE = graph.getLinks(done.getRoute().first);
            int i = 0;
            while (enumE.hasMoreElements()) {
                City next = (City) enumE.nextElement();
                next.setName(cityNamesFromInputFile.get(i++));
                if (!member(next.getTo(), done.getRoute())) {
                    Path branch = addCity(next, done);
                    if (best == null || branch.getCost() < best.getCost())
                        findPath(finish, graph, addCity(next, done));
                }
            }
        }
    }

    private static Path addCity(City arc, Path p) {
        Cell r = new Cell(arc.getTo(), p.getRoute());
        int c = arc.getCost() + p.getCost();
        return new Path(r, c);
    }

    private static boolean member(String node, Cell list) {
        Cell ptr;
        for (ptr = list; ptr != null && !ptr.first.equals(node); ptr = ptr.next) ;
        return ptr != null;
    }

    private static class Cell {
        String first;
        Cell next;

        Cell(String f, Cell n) {
            first = f;
            next = n;
        }
    }

    private static class Path {
        Cell route;
        int cost;

        Path(Cell r, int c) {
            route = r;
            cost = c;
        }

        int getCost() {
            return cost;
        }

        Cell getRoute() {
            return route;
        }
    }
}
