package it.unicam.cs.mpmgc.robotswarm.model;

import it.unicam.cs.mpmgc.robotswarm.utilites.ShapeData;

import java.util.List;

/**
 * Classes implementing this interface are used to represent an environment with entities and conditions
 *
 * @param <E> represent an entity.
 */

public interface Environment<E extends Entity>{

    /**
     * returns a list of the entities present in the environment
     * @return a list of the entities present in the environment
     */
    List<E> getEntity();

    /**
     * calculate the average coordinate of the robot signaling the given label
     * within the distance
     *
     * @param label    of the condition
     * @param entity   entity
     * @param distance within the entities have to be
     * @return the average coordinate of the robot signaling the given label
     */
    Coordinate getAverageCoordinateByLabel(String label, E entity, double distance);

    /**
     * verify if an entity is within an environment condition
     * @param label of the environment condition
     * @param entity to verify
     * @return true if an entity is within a shape data
     */
    boolean verifyShapeData(String label, E entity);

    /**
     * Apply the next instruction to all the entities in the environment
     * @return the environment after the application of the instruction
     */
    Environment<E> moveEntity();

    /**
     * returns the environment conditions
     * @return the environment conditions
     */
    List<ShapeData> getShapesData();

    /**
     * returns the string representation of the environment
     * @return the string representation of the environment
     */
    String toString();

    /**
     * returns the copy of the environment
     * @return the copy of the environment
     */
    Environment<E> createCopy();
}
