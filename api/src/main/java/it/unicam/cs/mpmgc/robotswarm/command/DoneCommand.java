package it.unicam.cs.mpmgc.robotswarm.command;

import it.unicam.cs.mpmgc.robotswarm.model.InstructionPointerHandler;
import it.unicam.cs.mpmgc.robotswarm.model.Robot;

/**
 * Command implementing the Done command: set the end of a loop.
 */

public final class DoneCommand implements Command<Robot> {

    @Override
    public void apply(Robot entity, InstructionPointerHandler c) {
        c.setDoneIndex(c.getIndex());
        c.goToLastRepeatCommand();
        entity.applyCommand();
    }
}
