package it.unicam.cs.mpmgc.robotswarm.command;

import it.unicam.cs.mpmgc.robotswarm.model.Coordinate;
import it.unicam.cs.mpmgc.robotswarm.model.InstructionPointerHandler;
import it.unicam.cs.mpmgc.robotswarm.model.Robot;

/**
 * Command implementing the Continue command: continues to move for s seconds with the same direction and speed.
 */
public final class ContinueCommand implements Command<Robot> {

    /**
     * times to repeat the instruction
     */
    private final int continueTime;

    public ContinueCommand(int continueTime){
        this.continueTime=continueTime;
    }

    @Override
    public void apply(Robot entity, InstructionPointerHandler c) {
        entity.setCoordinate(new Coordinate(entity.getDirection().x()*entity.getSpeed(),
                entity.getDirection().y()*entity.getSpeed()));
        c.incrementContinueIndex();
        c.incrementIndex(1);
        if(c.getContinueIndex()<continueTime){
            c.incrementIndex(-1);
        }
    }

}
