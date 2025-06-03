
import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DudeFull extends Dude {

    public DudeFull(String id, Point position, List<PImage> images, double actionPeriod,
                    double animationPeriod, int resourceLimit) {
        super(id, position, images, actionPeriod, animationPeriod, resourceLimit);
    }
    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            return true;
        } else {
            return super.moveTo(world, target, scheduler);
        }
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

        Optional<Entity> fullTarget = world.findNearest(this.getPosition(), new ArrayList<>(List.of(House.class)));

        if (fullTarget.isPresent() && this.moveTo(world, fullTarget.get(), scheduler)) {
            this.transform(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.getActionPeriod());
        }
    }

    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Movable dude = new DudeNotFull(this.getId(), this.getPosition(), this.getImages(),
                this.getActionPeriod(), this.getAnimationPeriod(), this.getResourceLimit(), 0);
        world.removeEntity(scheduler, this);

        world.addEntity(dude);
        dude.scheduleActions(scheduler, world, imageStore);

        return true;
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
