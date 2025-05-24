import java.awt.Point;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy {


    @Override
    public List<java.awt.Point> computePath(java.awt.Point start, java.awt.Point end, Predicate<java.awt.Point> canPassThrough, BiPredicate<java.awt.Point, java.awt.Point> withinReach, Function<java.awt.Point, Stream<Point>> potentialNeighbors) {
        return List.of();
    }
}
