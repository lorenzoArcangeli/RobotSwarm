package it.unicam.cs.mpmgc.robotswarm.command;

import it.unicam.cs.mpmgc.robotswarm.model.Coordinate;
import it.unicam.cs.mpmgc.robotswarm.model.InstructionPointerHandler;
import it.unicam.cs.mpmgc.robotswarm.model.Robot;

/**
 * Command implementing the Signal command: indicates a particular condition, label is an identifier consisting
 * of a sequence of alphanumeric characters and the symbol '_'
 */

public final class SignalCommand implements Command<Robot> {

    /**
     * condition to signal
     */
    private final String label;

    public SignalCommand(String label){
        this.label=label;
    }

    @Override
    public void apply(Robot entity, InstructionPointerHandler c) {
        if(isAlphaNumericOr_(label)){
            entity.addCondition(label);
            entity.setCoordinate(new Coordinate(entity.getDirection().x()*entity.getSpeed(),
                    entity.getDirection().y()*entity.getSpeed()));
            c.incrementIndex(+1);
        }else{
            throw new IllegalArgumentException("Incorrect argument command Signal");
        }
    }

    private boolean isAlphaNumericOr_(String s) {
        return s.matches("[a-zA-Z0-9_]+");
    }
}
