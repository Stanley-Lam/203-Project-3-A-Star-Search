import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class AStarPathingAdditionalTests {

    private static final PathingStrategy strategy = new AStarPathingStrategy();

    private static GridValues[][] grid;
    private static final int ROWS = 9;
    private static final int COLS = 9;

    private enum GridValues {BACKGROUND, OBSTACLE}

    @BeforeAll
    public static void before() {
        grid = new GridValues[ROWS][COLS];
        PathingStrategy.publicizePoint();
    }

    private static int getX(Point p) {
        return PathingStrategy.getX(p);
    }

    private static int getY(Point p) {
        return PathingStrategy.getY(p);
    }

    private static void initialize_grid() {
        for (GridValues[] gridValues : grid) {
            Arrays.fill(gridValues, GridValues.BACKGROUND);
        }
        grid[1][3] = GridValues.OBSTACLE;
        for (int row = 1; row < 6; row++) {
            grid[row][4] = GridValues.OBSTACLE;
        }
        grid[5][2] = GridValues.OBSTACLE;
        grid[5][3] = GridValues.OBSTACLE;
    }

    private static boolean withinBounds(Point p, GridValues[][] grid) {
        return getY(p) >= 0 && getY(p) < grid.length &&
                getX(p) >= 0 && getX(p) < grid[0].length;
    }

    private static boolean neighbors(Point p1, Point p2) {
        return getX(p1) + 1 == getX(p2) && getY(p1) == getY(p2) ||
                getX(p1) - 1 == getX(p2) && getY(p1) == getY(p2) ||
                getX(p1) == getX(p2) && getY(p1) + 1 == getY(p2) ||
                getX(p1) == getX(p2) && getY(p1) - 1 == getY(p2);
    }

    private static boolean two_doors_down(Point p1, Point p2) {
        return getX(p1) + 2 == getX(p2) && getY(p1) == getY(p2) ||
                getX(p1) - 2 == getX(p2) && getY(p1) == getY(p2) ||
                getX(p1) == getX(p2) && getY(p1) + 2 == getY(p2) ||
                getX(p1) == getX(p2) && getY(p1) - 2 == getY(p2) ||
                getX(p1) + 1 == getX(p2) && getY(p1) + 1 == getY(p2) ||
                getX(p1) + 1 == getX(p2) && getY(p1) - 1 == getY(p2) ||
                getX(p1) - 1 == getX(p2) && getY(p1) + 1 == getY(p2) ||
                getX(p1) - 1 == getX(p2) && getY(p1) - 1 == getY(p2);
    }

    @Test
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    public void test_1() {
        for (GridValues[] row : grid) {
            Arrays.fill(row, GridValues.BACKGROUND);
        }

        List<Point> path = strategy.computePath(
                new Point(0, 0),
                new Point(3, 0),
                p -> withinBounds(p, grid) && grid[getY(p)][getX(p)] != GridValues.OBSTACLE,
                AStarPathingAdditionalTests::neighbors,
                PathingStrategy.CARDINAL_NEIGHBORS
        );

        List<Point> expected = List.of(new Point(1, 0), new Point(2, 0));
        assertEquals(expected, path);
    }

    @Test
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    public void test_2() {
        for (GridValues[] row : grid) {
            Arrays.fill(row, GridValues.BACKGROUND);
        }

        grid[0][1] = GridValues.OBSTACLE;

        List<Point> path = strategy.computePath(
                new Point(0, 0),
                new Point(2, 0),
                p -> withinBounds(p, grid) && grid[getY(p)][getX(p)] != GridValues.OBSTACLE,
                AStarPathingAdditionalTests::neighbors,
                PathingStrategy.CARDINAL_NEIGHBORS
        );

        assertFalse(path.isEmpty(), "Expected no path due to complete blockage.");
    }

    @Test
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    public void test_3() {
        for (GridValues[] row : grid) {
            Arrays.fill(row, GridValues.BACKGROUND);
        }

        grid[1][1] = GridValues.OBSTACLE;

        List<Point> path = strategy.computePath(
                new Point(0, 0),
                new Point(2, 2),
                p -> withinBounds(p, grid) && grid[getY(p)][getX(p)] != GridValues.OBSTACLE,
                AStarPathingAdditionalTests::neighbors,
                PathingStrategy.CARDINAL_NEIGHBORS
        );

        List<Point> expected = List.of(new Point(0, 1), new Point(0, 2), new Point(1, 2));
        assertEquals(expected, path);
    }

    @Test
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    public void test_4() {
        for (GridValues[] row : grid) {
            Arrays.fill(row, GridValues.BACKGROUND);
        }

        grid[0][1] = GridValues.OBSTACLE;
        grid[1][0] = GridValues.OBSTACLE;
        grid[1][2] = GridValues.OBSTACLE;
        grid[2][1] = GridValues.OBSTACLE;

        List<Point> path = strategy.computePath(
                new Point(0, 0),
                new Point(1, 1),
                p -> withinBounds(p, grid) && grid[getY(p)][getX(p)] != GridValues.OBSTACLE,
                AStarPathingAdditionalTests::neighbors,
                PathingStrategy.CARDINAL_NEIGHBORS
        );

        assertTrue(path.isEmpty(), "Expected no path to surrounded goal.");
    }

    @Test
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    public void test_5() {
        for (GridValues[] row : grid) {
            Arrays.fill(row, GridValues.BACKGROUND);
        }

        grid[1][4] = GridValues.OBSTACLE;
        grid[2][4] = GridValues.OBSTACLE;
        grid[3][4] = GridValues.OBSTACLE;
        grid[5][4] = GridValues.OBSTACLE;
        grid[6][4] = GridValues.OBSTACLE;
        grid[7][4] = GridValues.OBSTACLE;
        grid[8][4] = GridValues.OBSTACLE;
        grid[6][2] = GridValues.OBSTACLE;
        grid[6][3] = GridValues.OBSTACLE;
        grid[6][5] = GridValues.OBSTACLE;
        grid[6][7] = GridValues.OBSTACLE;

        List<Point> path = strategy.computePath(
                new Point(2, 2),
                new Point(7, 7),
                p -> withinBounds(p, grid) && grid[getY(p)][getX(p)] != GridValues.OBSTACLE,
                AStarPathingAdditionalTests::neighbors,
                PathingStrategy.CARDINAL_NEIGHBORS
        );

        List<Point> expected = List.of(
                new Point(2, 3), new Point(2, 4), new Point(3, 4), new Point(4, 4), new Point(5, 4),
                new Point(5, 5), new Point(6, 5), new Point(6, 6), new Point(6, 7)
        );
        assertEquals(expected, path);
    }

    @Test
    @Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
    public void test_6() {
        for (GridValues[] row : grid) {
            Arrays.fill(row, GridValues.BACKGROUND);
        }

        grid[1][1] = GridValues.OBSTACLE;
        grid[1][2] = GridValues.OBSTACLE;
        grid[1][3] = GridValues.OBSTACLE;
        grid[1][4] = GridValues.OBSTACLE;
        grid[1][5] = GridValues.OBSTACLE;
        grid[1][6] = GridValues.OBSTACLE;
        grid[1][7] = GridValues.OBSTACLE;
        grid[2][7] = GridValues.OBSTACLE;
        grid[3][7] = GridValues.OBSTACLE;
        grid[4][7] = GridValues.OBSTACLE;
        grid[5][7] = GridValues.OBSTACLE;
        grid[6][7] = GridValues.OBSTACLE;
        grid[6][1] = GridValues.OBSTACLE;
        grid[6][2] = GridValues.OBSTACLE;
        grid[6][3] = GridValues.OBSTACLE;
        grid[6][4] = GridValues.OBSTACLE;
        grid[6][5] = GridValues.OBSTACLE;
        grid[6][6] = GridValues.OBSTACLE;
        grid[3][3] = GridValues.OBSTACLE;
        grid[4][3] = GridValues.OBSTACLE;
        grid[5][3] = GridValues.OBSTACLE;

        List<Point> path = strategy.computePath(
                new Point(0, 0),
                new Point(8, 8),
                p -> withinBounds(p, grid) && grid[getY(p)][getX(p)] != GridValues.OBSTACLE,
                AStarPathingAdditionalTests::neighbors,
                PathingStrategy.CARDINAL_NEIGHBORS
        );

        List<Point> expected = List.of(
                new Point(1, 0), new Point(2, 0), new Point(3, 0), new Point(4, 0), new Point(5, 0),
                new Point(6, 0), new Point(7, 0), new Point(8, 0), new Point(8, 1), new Point(8, 2),
                new Point(8, 3), new Point(8, 4), new Point(8, 5), new Point(8, 6), new Point(8, 7)
        );
        assertEquals(expected, path);
    }
}
