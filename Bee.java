import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        if (this.getPosition().adjacent(target.getPosition())) {
            model.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = this.nextPosition(model, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                model.moveEntity(scheduler, this, nextPos);
            }
        return false;
    }}

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> beeTarget = world.findNearest(this.getPosition(), new ArrayList<>(List.of(Stump.class)));
        if (beeTarget.isPresent()) {
            Point tgtPos = beeTarget.get().getPosition();

            if (this.moveTo(world, beeTarget.get(), scheduler)) {
                Christmas christmas = new Christmas(Christmas.CHRISTMAS_KEY+"_"+beeTarget.get().getId(),
                        tgtPos,imageStore.getImageList(Christmas.CHRISTMAS_KEY));
                world.addEntity(christmas);
            }
        }

        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.getActionPeriod());
    }
}
