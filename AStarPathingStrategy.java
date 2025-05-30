import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy {

    private static class Node {
        Point point;
        Node parent;
        int g; // Cost from start to this node
        int f; // Estimated total cost (g + h)

        public Node(Point point, Node parent, int g, int f) {
            this.point = point;
            this.parent = parent;
            this.g = g;
            this.f = f;
        }
    }

    @Override
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        boolean includeEnd = withinReach.test(end, end); // determines if end should be in final path

        Comparator<Node> comparator = Comparator.comparingInt(n -> n.f);
        PriorityQueue<Node> open = new PriorityQueue<>(comparator);
        Map<Point, Integer> gScores = new HashMap<>();
        Set<Point> closed = new HashSet<>();

        Node startNode = new Node(start, null, 0, heuristic(start, end));
        open.add(startNode);
        gScores.put(start, 0);

        while (!open.isEmpty()) {
            Node current = open.poll();

            if (withinReach.test(current.point, end)) {
                List<Point> path = reconstructPath(current);
                if (includeEnd) {
                    path.add(end);
                }
                return path;
            }

            closed.add(current.point);

            for (Point neighbor : potentialNeighbors.apply(current.point)
                    .filter(canPassThrough)
                    .filter(p -> !p.equals(start))
                    .toList()) {

                if (closed.contains(neighbor)) continue;

                int tentativeG = current.g + 1;
                int fScore = tentativeG + heuristic(neighbor, end);

                if (!gScores.containsKey(neighbor) || tentativeG < gScores.get(neighbor)) {
                    gScores.put(neighbor, tentativeG);
                    open.add(new Node(neighbor, current, tentativeG, fScore));
                }
            }
        }

        return List.of(); // no path found
    }

    private int heuristic(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y); // Manhattan distance
    }

    private List<Point> reconstructPath(Node node) {
        LinkedList<Point> path = new LinkedList<>();
        while (node.parent != null) {
            path.addFirst(node.point);
            node = node.parent;
        }
        return path;
    }
}
