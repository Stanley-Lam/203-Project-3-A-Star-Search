import processing.core.PImage;

import java.util.List;

public class Bee extends Movable {

    public static final String BEE_KEY = "bee";
    public static final int BEE_ANIMATION_PERIOD = 0;
    public static final int BEE_ACTION_PERIOD = 1;
    public static final int BEE_NUM_PROPERTIES = 2;

    public Bee(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, PathingStrategy pathstrat) {
        super(id, position, images, actionPeriod, animationPeriod, pathstrat);
    }

    @Override
    public boolean moveTo(WorldModel model, Entity target, EventScheduler scheduler) {
        return false;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

    }
}
