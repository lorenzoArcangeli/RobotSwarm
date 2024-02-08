package it.unicam.cs.mpmgc.robotswarm.model;

import java.util.List;

/**
 * Classes implementing this interface are used to represent an entity
 */


public interface Entity {

    /**
     * returns the coordinate of the entity
     * @return the coordinate of the entity
     */
    Coordinate getCoordinate();

    /**
     * Set new Coordinate
     * @param coordinate to set
     */
    void setCoordinate(Coordinate coordinate);

    /**
     * returns the direction of the entity
     * @return the direction of the entity
     */
    Coordinate getDirection();

    /**
     * Set new direction
     * @param direction to set
     */
    void setDirection(Coordinate direction);

    /**
     * returns the speed of the entity
     * @return the speed of the entity
     */
    double getSpeed();

    /**
     * Set new speed
     * @param speed to set
     */
    void setSpeed(double speed);

    /**
     * Apply the next command in the set program
     */
    void applyCommand();

    /**
     * add a signaling condition if it is not already signaling
     * @param label of the condition
     */
    void addCondition(String label);

    /**
     * remove a signaling condition if it is already signaling
     * @param label of the condition
     */
    void removeCondition(String label);

    /**
     * returns the signaling conditions
     * @return the signaling conditions
     */
    List<String> getConditions();

}
