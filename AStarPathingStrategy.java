import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy {
    public static class Node{
        Point point;
        int distance;
        int f;
        public Node(Point point,int g,int h){
            this.point=point;
            this.distance=g;
            this.f=g+h;
        }
    }
    @Override
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {
        PriorityQueue<Node>open=new PriorityQueue<>(Comparator.comparingInt(node -> node.f));
        Node star=new Node(start,0,heu(start,end));
        Map<Point,Integer>g=new HashMap<>();
        Map<Point,Point>keeptrack=new HashMap<>();
        open.add(star);
        g.put(star.point,0);
        List<Point> closed= new ArrayList<>();
        while(!open.isEmpty()){
            Node curr=open.poll();
            closed.add(curr.point);
            potentialNeighbors.apply(curr.point).filter(canPassThrough).filter(
                    p->!closed.contains(p)).
                    forEach(
                    nei->{
                        int newG=g.get(curr.point)+1;
                        if (newG<g.getOrDefault(nei,Integer.MAX_VALUE)){
                            keeptrack.put(nei,curr.point);
                            g.put(nei,newG);
                            open.add(new Node(nei,newG,heu(nei,end)));
                        }
                    }
            );
            if(withinReach.test(curr.point,end)){
                List<Point> path=new ArrayList<>();
                path.add(curr.point);
                Point new_curr=keeptrack.get(curr.point);
                while(new_curr !=null){
                        path.addFirst(new_curr);
                        new_curr=keeptrack.get(new_curr);
                }
                path.removeFirst();
                //path.add(end);
                return path;
            }
        }
        return List.of();
    }
    public int heu(Point p1, Point p2){
        return (Math.abs(p1.x-p2.x)+Math.abs(p1.y-p2.y));
    }
}
