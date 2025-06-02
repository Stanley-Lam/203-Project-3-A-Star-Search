import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
// Might have to implement Pathing strategy here
// Maybe as a variable and part of the class
// but that would also change nextposition I think
public class Fairy extends Movable {


    public static final String FAIRY_KEY = "fairy";
    public static final int FAIRY_ANIMATION_PERIOD = 0;
    public static final int FAIRY_ACTION_PERIOD = 1;
    public static final int FAIRY_NUM_PROPERTIES = 2;

    public Fairy(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod, pathing_fairy); //Added fairy pathing
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fairyTarget = world.findNearest(this.getPosition(), new ArrayList<>(List.of(Stump.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (this.moveTo(world, fairyTarget.get(), scheduler)) {

                Sapling sapling = new Sapling(Sapling.SAPLING_KEY + "_" + fairyTarget.get().getId(), tgtPos,
                        imageStore.getImageList(Sapling.SAPLING_KEY), Sapling.SAPLING_ACTION_ANIMATION_PERIOD,
                        Sapling.SAPLING_ACTION_ANIMATION_PERIOD, 0, Sapling.SAPLING_HEALTH_LIMIT);

                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.getActionPeriod());
    }


    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            world.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Movable bee = new Bee(this.getId(), this.getPosition(), this.getImages(),
                this.getActionPeriod(), this.getAnimationPeriod(), Movable.pathing_fairy);
        world.removeEntity(scheduler, this);
        world.addEntity(bee);
        bee.scheduleActions(scheduler, world, imageStore);

        return true;
    }
}
