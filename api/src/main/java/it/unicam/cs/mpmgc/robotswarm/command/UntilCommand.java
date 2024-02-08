package it.unicam.cs.mpmgc.robotswarm.command;

import it.unicam.cs.mpmgc.robotswarm.model.InstructionPointerHandler;
import it.unicam.cs.mpmgc.robotswarm.model.Robot;
import it.unicam.cs.mpmgc.robotswarm.model.RobotEnvironment;

/**
 * Command implementing the Until command: Repeats instructions until a certain condition is perceived in the environment
 */

public final class UntilCommand implements Command<Robot> {

    /**
     * Environment of the robots
     */
    private final RobotEnvironment environment;

    /**
     * Condition to perceived
     */
    private final String label;

    public UntilCommand(RobotEnvironment environment, String label){
        this.environment=environment;
        this.label=label;
    }

    @Override
    public void apply(Robot entity, InstructionPointerHandler c) {
        if(!environment.verifyShapeData(label, entity)){
            executeCommand(entity, c);
        }else{
            skipCommand(entity, c);
        }
    }

    private void executeCommand(Robot entity, InstructionPointerHandler c){
        if(c.needAdd()){
            c.addToRepeatCommandIndex();
            c.changeNestedCommandIndex(1);
            c.addToRepeatCommandNumIter();
        }
        if(c.needIncrementNestedIndex())
            c.changeNestedCommandIndex(1);
        c.incrementIndex(1);
        entity.applyCommand();
    }

    private void skipCommand(Robot entity, InstructionPointerHandler c){
        c.changeNestedCommandIndex(-1);
        c.goToFirstApplicableCommand();
        entity.applyCommand();
    }
}
