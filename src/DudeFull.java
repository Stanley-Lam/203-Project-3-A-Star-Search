package src;

import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

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

    @Override
    public List<java.awt.Point> computePath(java.awt.Point start, java.awt.Point end, Predicate<java.awt.Point> canPassThrough, BiPredicate<java.awt.Point, java.awt.Point> withinReach, Function<java.awt.Point, Stream<java.awt.Point>> potentialNeighbors) {
        return List.of();
    }
}
