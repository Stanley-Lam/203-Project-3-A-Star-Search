package src;

import java.awt.Point;
import java.lang.reflect.*;

import java.util.function.Predicate;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.List;

interface PathingStrategy
{
    // Relax, I know what I'm doing.
    static Field pointXBox[] = {null};
    static Field pointYBox[] = {null};

    static void publicizePoint()
    {
        try {
            // Nice security you've got there, Oracle.
            pointXBox[0] = java.awt.Point.class.getDeclaredField("x");
            pointXBox[0].setAccessible(true);
            pointYBox[0] = java.awt.Point.class.getDeclaredField("y");
            pointYBox[0].setAccessible(true);
        }
        catch (NoSuchFieldException e) {
            // All bets are off if they didn't name their fields "x" and "y". But
            // we were already assuming that anyways.
        }
    }

    static int getX(java.awt.Point p) {
        try {
            return (int)pointXBox[0].get(p);
        }
        catch (IllegalAccessException e) {
            return -1;
        }
    }

    static int getY(java.awt.Point p) {
        try {
            return (int)pointYBox[0].get(p);
        }
        catch (IllegalAccessException e) {
            return -1;
        }
    }

    /*
     * Returns a prefix of a path from the start point to a point within reach
     * of the end point.  This path is only valid ("clear") when returned, but
     * may be invalidated by movement of other entities.
     *
     * The prefix includes neither the start point nor the end point.
     */
    List<java.awt.Point> computePath(java.awt.Point start, java.awt.Point end,
                                     Predicate<java.awt.Point> canPassThrough,
                                     BiPredicate<java.awt.Point, java.awt.Point> withinReach,
                                     Function<java.awt.Point, Stream<java.awt.Point>> potentialNeighbors);

    static final Function<java.awt.Point, Stream<java.awt.Point>> CARDINAL_NEIGHBORS =
            point -> {
                publicizePoint();
                return Stream.<java.awt.Point>builder()
                        .add(new java.awt.Point(getX(point), getY(point) - 1))
                        .add(new java.awt.Point(getX(point), getY(point) + 1))
                        .add(new java.awt.Point(getX(point) - 1, getY(point)))
                        .add(new java.awt.Point(getX(point) + 1, getY(point)))
                        .build();
            };

    static final Function<java.awt.Point, Stream<java.awt.Point>> DIAGONAL_CARDINAL_NEIGHBORS =
            point -> {
                publicizePoint();
                return Stream.<java.awt.Point>builder()
                        .add(new java.awt.Point(getX(point) - 1, getY(point) - 1))
                        .add(new java.awt.Point(getX(point) + 1, getY(point) + 1))
                        .add(new java.awt.Point(getX(point) - 1, getY(point) + 1))
                        .add(new java.awt.Point(getX(point) + 1, getY(point) - 1))
                        .add(new java.awt.Point(getX(point), getY(point) - 1))
                        .add(new java.awt.Point(getX(point), getY(point) + 1))
                        .add(new java.awt.Point(getX(point) - 1, getY(point)))
                        .add(new Point(getX(point) + 1, getY(point)))
                        .build();
            };
}
