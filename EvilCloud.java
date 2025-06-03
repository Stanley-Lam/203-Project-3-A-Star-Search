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
        super(id, position, images, actionPeriod, animationPeriod, pathing_zombie);
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
        Optional<Entity> target = world.findNearest(this.getPosition(), List.of(DudeFull.class, DudeNotFull.class));

        if (target.isPresent()) {
            Entity entity = target.get();
            if (this.getPosition().adjacent(entity.getPosition())) {
                Zombie zombie = new Zombie(
                        Zombie.ZOMBIE_KEY + "_" + entity.getId(),
                        entity.getPosition(),
                        imageStore.getImageList(Zombie.ZOMBIE_KEY),
                        Zombie.ZOMBIE_ACTION_PERIOD,
                        Zombie.ZOMBIE_ANIMATION_PERIOD
                );

                world.removeEntity(scheduler, entity);
                scheduler.unscheduleAllEvents(entity);
                world.addEntity(zombie);
                zombie.scheduleActions(scheduler, world, imageStore);
            } else {
                Point nextPos = this.nextPosition(world, entity.getPosition());
                if (!this.getPosition().equals(nextPos)) {
                    world.moveEntity(scheduler, this, nextPos);
                }
            }
        }

        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.getActionPeriod());
    }
}
