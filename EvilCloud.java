import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        if (this.getPosition().adjacent(target.getPosition())) {
            model.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = this.nextPosition(model, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                model.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> EvilCloudTarget = world.findNearest(this.getPosition(), new ArrayList<>(List.of(Dude.class)));
        if (EvilCloudTarget.isPresent()) {
            Point tgtPos = EvilCloudTarget.get().getPosition();

            if (this.moveTo(world, EvilCloudTarget.get(), scheduler)) {
                Zombie zombie = new Zombie (Zombie.ZOMBIE_KEY + "_" + EvilCloudTarget.get().getId(),
                        tgtPos,imageStore.getImageList(Zombie.ZOMBIE_KEY), Zombie.ZOMBIE_ANIMATION_PERIOD,
                        Zombie.ZOMBIE_ACTION_PERIOD, Movable.pathing_dude);
                world.addEntity(zombie);
            }
        }

        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.getActionPeriod());
    }
}
