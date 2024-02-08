package it.unicam.cs.mpmgc.robotswarm.command;

import it.unicam.cs.mpmgc.robotswarm.model.*;
import it.unicam.cs.mpmgc.robotswarm.util.Normalizer;

/**
 * Command implementing the Follow command: moves at the speed s expressed in m/s a direction that is the average
 * of the positions of the robots that signal the label condition and that are located at a distance less than or
 * equal to dist. If there are no robots  If no robots exist, a random direction is chosen in the range
 * [-dist, dist]x[-dist, dist].
 */

public final class FollowCommand implements Command<Robot> {

    /**
     * Environment where the robot is executing
     */
    private final RobotEnvironment environment;

    /**
     * Condition to follow
     */
    private final String label;

    /**
     * Distance of the visible robot
     */
    private final double dist;

    /**
     * Speed of the movement
     */
    private final double speed;

    public FollowCommand(RobotEnvironment environment, String label, double dist, double speed){
        this.environment=environment;
        this.label=label;
        this.dist=dist;
        this.speed=speed;
    }

    @Override
    public void apply(Robot entity, InstructionPointerHandler c) {
        Coordinate direction = this.environment.getAverageCoordinateByLabel(label, entity, dist);
        if(direction.isZero()){
            Coordinate coordinate=new Coordinate(-dist, dist);
            new MoveRandomCommand(coordinate, coordinate, speed).apply(entity, c);
        }else{
            Normalizer n=new Normalizer(entity.getCoordinate(), direction);
            new MoveCommand(n.getCorrectDirection(), speed).apply(entity, c);
        }
    }
}
