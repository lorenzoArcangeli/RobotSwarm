package it.unicam.cs.mpmgc.robotswarm.command;

import it.unicam.cs.mpmgc.robotswarm.model.Entity;
import it.unicam.cs.mpmgc.robotswarm.model.InstructionPointerHandler;

/**
 * This functional interface is used to compute the next state of an entity based on the instruction pointer value
 *
 * @param <E> an Entity
 */

@FunctionalInterface
public interface Command<E extends Entity> {

    /**
     * change the state of the robot applying the command
     * @param entity who wants to apply a command
     * @param c instruction pointer handler of the program
     */
    void apply(E entity, InstructionPointerHandler c);
}
