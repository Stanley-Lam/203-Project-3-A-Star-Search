import src.Point;

import javax.sql.rowset.Predicate;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class SingleStepPathingStrategy implements PathingStrategy {
    public List<Point> computePath(Point start, Point end,
                                   Predicate canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        /* Does not check withinReach.  Since only a single step is taken
         * on each call, the caller will need to check if the destination
         * has been reached.
         */
        return potentialNeighbors.apply(start)
                .filter((java.util.function.Predicate<? super Point>) canPassThrough)
                .filter(pt ->
                        !pt.equals(start)
                                && !pt.equals(end)
                                && Math.abs(end.x - pt.x) <= Math.abs(end.x - start.x)
                                && Math.abs(end.y - pt.y) <= Math.abs(end.y - start.y))
                .limit(1)
                .collect(Collectors.toList());
    }

    @Override
    public List<java.awt.Point> computePath(java.awt.Point start,
                                            java.awt.Point end,
                                            java.util.function.Predicate<java.awt.Point> canPassThrough,
                                            BiPredicate<java.awt.Point, java.awt.Point> withinReach,
                                            Function<java.awt.Point, Stream<java.awt.Point>> potentialNeighbors) {
        return List.of();
    }
}
