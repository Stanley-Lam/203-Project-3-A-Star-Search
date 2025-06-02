import processing.core.PImage;

import java.util.List;

public class EvilCloud extends Movable {

    public static final String EVILCLOUD_KEY = "EvilCloud";
    public static final int EVILCLOUD_ANIMATION_PERIOD = 0;
    public static final int EVILCLOUD_ACTION_PERIOD = 1;
    public static final int EVILCLOUD_NUM_PROPERTIES = 2;

    public EvilCloud(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod, pathing_fairy);
    }

    @Override
    public boolean moveTo(WorldModel model, Entity target, EventScheduler scheduler) {
        return false;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

    }
}
