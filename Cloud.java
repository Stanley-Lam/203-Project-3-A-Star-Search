import processing.core.PImage;

import java.util.List;

public class Cloud extends Entity implements Animatable{

    public static final String CLOUD_KEY = "cloud";
    public static final int CLOUD_ANIMATION_PERIOD = 0;
    public static final int CLOUD_NUM_PROPERTIES = 1;
    public double animationPeriod;

    public Cloud(String id, Point position, List<PImage> images, double animationPeriod) {
        super(id, position, images);
        this.animationPeriod = animationPeriod;
    }

    @Override
    public double getAnimationPeriod() {
        return this.animationPeriod;
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(
                this,
                new Animation(this, world, imageStore, 0),
                this.animationPeriod
        );
    }
}
