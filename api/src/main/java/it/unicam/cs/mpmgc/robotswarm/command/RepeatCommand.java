package it.unicam.cs.mpmgc.robotswarm.command;

import it.unicam.cs.mpmgc.robotswarm.model.InstructionPointerHandler;
import it.unicam.cs.mpmgc.robotswarm.model.Robot;

/**
 * Command implementing the Repeat command: Repeats n times a sequence of instructions
 */

public final class RepeatCommand implements Command<Robot> {

    /**
     * times to repeat a loop
     */
    private final int numReps;

    public RepeatCommand(int numReps){
        this.numReps=numReps;
    }


    @Override
    public void apply(Robot entity, InstructionPointerHandler c) {
        if(c.needAdd()){
            c.addToRepeatCommandIndex();
            c.addToRepeatCommandNumIter();
            c.changeNestedCommandIndex(1);
        }
        if(c.getCurrentToRepeatCommandNumIter()<numReps){
            executeCommand(entity, c);
        }else{
            skipCommand(entity, c);
        }
    }

    private void executeCommand(Robot entity, InstructionPointerHandler c){
        c.incrementToRepeatCommandNumIter();
        if(c.needIncrementNestedIndex())
            c.changeNestedCommandIndex(1);
        c.incrementIndex(1);
        entity.applyCommand();
    }

    private void skipCommand(Robot entity, InstructionPointerHandler c){
        c.resetLastNumIteration();
        c.changeNestedCommandIndex(-1);
        c.goToFirstApplicableCommand();
        entity.applyCommand();
    }

}
