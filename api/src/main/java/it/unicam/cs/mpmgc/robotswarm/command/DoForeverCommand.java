package it.unicam.cs.mpmgc.robotswarm.command;

import it.unicam.cs.mpmgc.robotswarm.model.InstructionPointerHandler;
import it.unicam.cs.mpmgc.robotswarm.model.Robot;

/**
 * Command implementing the DoForever command: repeats instruction forever.
 */

public final class DoForeverCommand implements Command<Robot> {

    @Override
    public void apply(Robot entity, InstructionPointerHandler c) {
        if(c.needAdd()){
            c.addToRepeatCommandIndex();
            c.addToRepeatCommandNumIter();
            c.changeNestedCommandIndex(1);
        }
        if(c.needIncrementNestedIndex())
            c.changeNestedCommandIndex(1);
        c.incrementIndex(1);
        entity.applyCommand();
    }
}
