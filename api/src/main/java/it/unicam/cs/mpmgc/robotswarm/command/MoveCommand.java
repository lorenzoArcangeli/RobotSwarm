package it.unicam.cs.mpmgc.robotswarm.command;

import it.unicam.cs.mpmgc.robotswarm.model.Coordinate;
import it.unicam.cs.mpmgc.robotswarm.model.InstructionPointerHandler;
import it.unicam.cs.mpmgc.robotswarm.model.Robot;

/**
 * Command implementing the Move command: moves in the direction (x,y), where x and y are values between -1 and 1
 * at the given speed s expressed in m/s, it is assumed that at most one between x and y is different from 0.
 */

public final class MoveCommand implements Command<Robot> {

    /**
     * Direction to set
     */
    private final Coordinate direction;

    /**
     * Speed of movement
     */
    private final double speed;

    public MoveCommand(Coordinate direction, double speed){
        this.direction=direction;
        this.speed=speed;
    }

    @Override
    public void apply(Robot entity, InstructionPointerHandler c) {
        if((direction.x()!=0||direction.y()!=0)){
            verifyArgs(direction);
            entity.setSpeed(speed);
            entity.setCoordinate(new Coordinate(direction.x()*entity.getSpeed(), direction.y()*entity.getSpeed()));
            entity.setDirection(direction);
            c.incrementIndex(1);
        }
    }

    private void verifyArgs(Coordinate arg) throws IllegalArgumentException {
        if(arg.x()<-1||arg.x()>1|| arg.y()<-1||arg.y()>1){
            throw new IllegalArgumentException("Incorrect argument command Move");
        }
    }
}
