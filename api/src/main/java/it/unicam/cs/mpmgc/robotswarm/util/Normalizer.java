package it.unicam.cs.mpmgc.robotswarm.util;

import it.unicam.cs.mpmgc.robotswarm.model.Coordinate;

/**
 * This class is used to normalize a value out of [-1, 1]
 */
public class Normalizer {
    /**
     * robot coordinate
     */
    private final Coordinate robotCoordinate;

    /**
     * destination coordinate
     */
    private final Coordinate destination;

    public Normalizer(Coordinate robotCoordinate, Coordinate destination){
        this.robotCoordinate=robotCoordinate;
        this.destination=destination;
    }

    /**
     * returns the correct direction
     * @return the correct direction
     */
    public Coordinate getCorrectDirection(){
        if(!(isXIncorrect())&&!(isYIncorrect())) return shiftByRobotCoordinate();
        if(isXIncorrect()&& !(isYIncorrect())) return getDirectionByX(shiftByRobotCoordinate());
        if(!(isXIncorrect())&& isYIncorrect()) return getDirectionByY(shiftByRobotCoordinate());
        if(Math.abs(shiftByRobotCoordinate().y())>Math.abs(shiftByRobotCoordinate().x()))
            return getDirectionByY(shiftByRobotCoordinate());
        return getDirectionByX(shiftByRobotCoordinate());
    }

    private boolean isXIncorrect(){
        return robotCoordinate.x()-destination.x()<=-1||robotCoordinate.x()-destination.x()>=1;
    }

    private boolean isYIncorrect(){
        return robotCoordinate.y()-destination.y()<=-1||robotCoordinate.y()-destination.y()>=1;
    }

    private Coordinate shiftByRobotCoordinate(){
        return new Coordinate(destination.x()-robotCoordinate.x(), destination.y()- robotCoordinate.y());
    }
    private Coordinate getDirectionByY(Coordinate point){
        if(robotCoordinate.y()<point.y())
            return new Coordinate( point.x()/ point.y(),1 );
        return new Coordinate(point.x()/ -point.y(),-1);
    }
    private Coordinate getDirectionByX(Coordinate point){
        if(robotCoordinate.x()<point.x())
            return new Coordinate(1, point.y()/ point.x());
        return new Coordinate(-1, point.y()/ -point.x());

    }
}
