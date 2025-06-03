
import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Zombie extends Movable {

    public static final String ZOMBIE_KEY = "zombie";
    public static final int ZOMBIE_ANIMATION_PERIOD = 0;
    public static final int ZOMBIE_ACTION_PERIOD = 1;
    public static final int ZOMBIE_NUM_PROPERTIES = 2;

    public Zombie(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod, pathing_zombie);
    }

    @Override
    public boolean moveTo(WorldModel model, Entity target, EventScheduler scheduler) {
        return false;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        List<Point> neighbors = (List<Point>) PathingStrategy.CARDINAL_NEIGHBORS.apply(this.getPosition());

        for (Point p : neighbors) {
            if (world.withinBounds(p) && !world.isOccupied(p)) {
                world.moveEntity(scheduler, this, p);
                break;  // only move once per tick
            }
        }

        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.getActionPeriod());
    }

}
