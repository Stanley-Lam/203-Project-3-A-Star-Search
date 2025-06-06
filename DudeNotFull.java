
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DudeNotFull extends Dude {

    private int resourceCount;

    public DudeNotFull(String id, Point position, List<PImage> images, double actionPeriod,
                       double animationPeriod, int resourceLimit, int resourceCount) {
        super(id, position, images, actionPeriod, animationPeriod, resourceLimit);
        this.resourceCount = resourceCount;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        // Check if evilcloud is nearby
        int radius = 1;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                Point nearby = new Point(this.getPosition().x + dx, this.getPosition().y + dy);
                if (world.withinBounds(nearby)) {
                    Optional<Entity> occupant = world.getOccupant(nearby);
                    if (occupant.isPresent() && occupant.get() instanceof EvilCloud) {
                        this.zombietransform(world, scheduler, imageStore);
                        return; // stop executing other activity, fairy is now a bee
                    }
                    if (occupant.isPresent() && occupant.get() instanceof Zombie) {
                        this.zombietransform(world, scheduler, imageStore);
                        return; // stop executing other activity, fairy is now a bee
                    }
                }
            }
        }

        Optional<Entity> target = world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Tree.class, Sapling.class)));

        if (target.isEmpty() || !this.moveTo(world, target.get(), scheduler) || !this.transform(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.getActionPeriod());
        }
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            this.resourceCount += 1;
            ((Plant) target).decreaseHealth();
            return true;
        } else {
            return super.moveTo(world, target, scheduler);
        }
    }

    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.resourceCount >= this.getResourceLimit()) {
            Movable dude = new DudeFull(this.getId(), this.getPosition(), this.getImages(), this.getActionPeriod(),
                    this.getAnimationPeriod(), this.getResourceLimit());

            world.removeEntity(scheduler, this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(dude);
            dude.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public boolean zombietransform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Movable zombie = new Zombie(this.getId(), this.getPosition(), imageStore.getImageList(Zombie.ZOMBIE_KEY),  // get actual Zombie images
                this.getActionPeriod(), this.getAnimationPeriod());

        world.removeEntity(scheduler, this);
        world.addEntity(zombie);
        zombie.scheduleActions(scheduler, world, imageStore);
        return true;
    }

}
