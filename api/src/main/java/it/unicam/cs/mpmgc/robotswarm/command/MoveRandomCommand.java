package it.unicam.cs.mpmgc.robotswarm.command;

import it.unicam.cs.mpmgc.robotswarm.model.Coordinate;
import it.unicam.cs.mpmgc.robotswarm.model.InstructionPointerHandler;
import it.unicam.cs.mpmgc.robotswarm.model.Robot;
import it.unicam.cs.mpmgc.robotswarm.util.Normalizer;

import java.util.Random;

/**
 * Command implementing the Move Random command: it moves at the speed s expressed in m/s towards a randomly
 * selected (x,y) position in the range [x1, x2] and [y1, y2];
 */

public final class MoveRandomCommand implements Command<Robot> {

    /**
     * First bound for the random movement
     */
    private final Coordinate firstCoordinate;

    /**
     * Second bound of the random movement
     */
    private final Coordinate secondCoordinate;

    /**
     * Speed of the movement
     */
    private final double speed;

    public MoveRandomCommand(Coordinate firstCoordinate, Coordinate secondCoordinate, double speed){
        this.firstCoordinate=firstCoordinate;
        this.secondCoordinate=secondCoordinate;
        this.speed=speed;
    }

    @Override
    public void apply(Robot entity, InstructionPointerHandler c) {
        Coordinate randomDirection = calculateRandomDirection(firstCoordinate, secondCoordinate);
        Coordinate correctDirection=new Normalizer(entity.getCoordinate(), randomDirection).getCorrectDirection();
        new MoveCommand(new Coordinate(correctDirection.x(), correctDirection.y()), speed).apply(entity, c);
    }

    private Coordinate calculateRandomDirection(Coordinate firstCoordinate, Coordinate secondCoordinate){
        double x= calculateRandomDirectionValue(firstCoordinate.x(), firstCoordinate.y());
        double y= calculateRandomDirectionValue(secondCoordinate.x(), secondCoordinate.y());
        return new Coordinate(x,y);
    }

    private double calculateRandomDirectionValue(double x, double y){
        Random random = new Random();
        if(x>y)
            return random.nextDouble() * (x - y + 1) + x;
        return random.nextDouble() * (y - x + 1) + x;
    }
}

