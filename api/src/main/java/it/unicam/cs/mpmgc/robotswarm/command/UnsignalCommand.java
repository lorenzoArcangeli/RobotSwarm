package it.unicam.cs.mpmgc.robotswarm.command;

import it.unicam.cs.mpmgc.robotswarm.model.Coordinate;
import it.unicam.cs.mpmgc.robotswarm.model.InstructionPointerHandler;
import it.unicam.cs.mpmgc.robotswarm.model.Robot;

/**
 * Command implementing the Unsignal command: nds the signaling of a condition
 */


public final class UnsignalCommand implements Command<Robot>{

    /**
     * condition to unsignal
     */
    private final String label;

    public UnsignalCommand(String label){
        this.label=label;
    }

    @Override
    public void apply(Robot entity, InstructionPointerHandler c) {
        entity.removeCondition(label);
        entity.setCoordinate(new Coordinate(entity.getDirection().x()*entity.getSpeed(),
                entity.getDirection().y()*entity.getSpeed()));
        c.incrementIndex(1);
    }
}
