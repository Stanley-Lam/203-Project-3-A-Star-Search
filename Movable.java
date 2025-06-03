import processing.core.PImage;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class Movable extends ActiveAnimatedEntity {
    // Added the two entity pathing
    protected static final PathingStrategy pathing_dude = new AStarPathingStrategy();
    protected static final PathingStrategy pathing_fairy = new AStarPathingStrategy();
    protected static final PathingStrategy pathing_zombie = new AStarPathingStrategy();
    private PathingStrategy pathstrat;

    public Movable(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, PathingStrategy pathstrat) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.pathstrat = pathstrat;
    }


    public Point nextPosition(WorldModel world, Point destPos) {
        Predicate<Point> canPassThrough = (Point p) -> world.withinBounds(p) && !world.isOccupied(p);
        BiPredicate<Point, Point> withinReach = (Point p1, Point p2) -> p1.adjacent(p2);

        List<Point> path = pathstrat.computePath(getPosition(), destPos, canPassThrough, withinReach,
                PathingStrategy.CARDINAL_NEIGHBORS);

        if (path.size() == 0){
            return this.getPosition();
        }
        else{
            return path.get(0);
        }
    }

    public abstract boolean moveTo(WorldModel model, Entity target, EventScheduler scheduler);

    /**
     * The entity can move to destination if it's not occupied.
     */
    public boolean isInvalidMove(WorldModel world, Point destination) {
        return world.isOccupied(destination);
    }
}
