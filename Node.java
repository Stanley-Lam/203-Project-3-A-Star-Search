public class Node {
    Point point;
    double fScore;

    Node(Point point, double fScore) {
        this.point = point;
        this.fScore = fScore;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Node && ((Node) obj).point.equals(point);
    }

    @Override
    public int hashCode() {
        return point.hashCode();
    }
}
