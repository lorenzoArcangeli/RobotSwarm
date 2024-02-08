package it.unicam.cs.mpmgc.robotswarm.command;

import it.unicam.cs.mpmgc.robotswarm.model.Coordinate;
import it.unicam.cs.mpmgc.robotswarm.model.InstructionPointerHandler;
import it.unicam.cs.mpmgc.robotswarm.model.Robot;

/**
 * Command implementing the Stop command: stops its movement. The robot no longer moves but continues to report its condition.
 */


public final class StopCommand implements Command<Robot> {

    @Override
    public void apply(Robot entity, InstructionPointerHandler c) {
        entity.setSpeed(0);
        entity.setDirection(new Coordinate(0,0));
        c.incrementIndex(1);
    }
}
