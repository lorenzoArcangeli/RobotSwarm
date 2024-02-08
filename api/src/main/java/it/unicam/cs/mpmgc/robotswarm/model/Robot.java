package it.unicam.cs.mpmgc.robotswarm.model;

import it.unicam.cs.mpmgc.robotswarm.command.Command;
import it.unicam.cs.mpmgc.robotswarm.command.MoveCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a Robot entity
 */

public class Robot implements Entity{

    /**
     * Coordinate of the robot
     */
    private Coordinate coordinate;

    /**
     * current direction of the robot
     */
    private Coordinate direction;

    /**
     * speed of the robot
     */
    private double speed;

    /**
     * conditions signaling by the robot
     */
    private List<String> conditions;

    /**
     * Robot program instructions
     */
    private List<Command<Robot>> commands;

    /**
     * Handler for the program instruction pointer
     */
    private final InstructionPointerHandler commandHandler=new InstructionPointerHandler();

    /**
     * create a new robot with coordinate, direction and speed
     * @param coordinate to set
     * @param direction to set
     * @param speed to set
     */
    public Robot(Coordinate coordinate, Coordinate direction, double speed){
        this.coordinate=coordinate;
        this.direction=direction;
        this.speed=speed;
        this.commands=new ArrayList<>();
        conditions=new ArrayList<>();
    }

    /**
     * create a new robot with coordinate, direction, speed and conditions
     * @param coordinate to set
     * @param direction to set
     * @param speed to set
     * @param conditions to set
     */
    public Robot(Coordinate coordinate, Coordinate direction, double speed, List<String> conditions){
        this(coordinate, direction, speed);
        this.conditions=conditions;
    }

    /**
     * create a new robot in given position with no direction and no speed
     * @param coordinate of the robot
     */
    public Robot(Coordinate coordinate){
        this(coordinate, new Coordinate(0,0), 0);
    }


    /**
     * set the robot program
     * @param commandsList list of instructions
     */
    public void addCommands(List<Command<Robot>> commandsList){
        this.commands=commandsList;
        commandHandler.setCommands(commands);
    }


    @Override
    public void setCoordinate(Coordinate coordinate) {
        this.coordinate=new Coordinate(this.coordinate.x()+coordinate.x(),
                this.coordinate.y()+coordinate.y());
    }


    @Override
    public Coordinate getCoordinate(){
        return this.coordinate;
    }

    @Override
    public void setDirection(Coordinate direction) {
        this.direction=direction;
    }

    @Override
    public Coordinate getDirection(){
        return this.direction;
    }

    @Override
    public void setSpeed(double speed) {
        this.speed=speed;
    }

    public double getSpeed() {
        return this.speed;
    }

    @Override
    public void addCondition(String label){
        if(isConditionPresent(label))
            throw new IllegalArgumentException("The robot is already signaling the condition");
        this.conditions.add(label);
    }

    @Override
    public void removeCondition(String label) {
        if(!isConditionPresent(label))
            throw new IllegalArgumentException("Robot is not signaling the condition at line: "+(commandHandler.getIndex()+1));
        this.conditions.remove(label);
    }

    /**
     * returns true if a condition is already signaling
     * @param label of the condition
     * @return true if a condition is already signaling
     */
    public boolean isConditionPresent(String label){
        return this.conditions.contains(label);
    }

    @Override
    public List<String> getConditions(){
        return this.conditions;
    }

    @Override
    public void applyCommand(){
        if(commandHandler.getIndex()<commands.size()){
            commands.get(commandHandler.getIndex()).apply(this, commandHandler);
        }else{
            new MoveCommand(this.direction, this.speed).apply(this, commandHandler);
        }
    }
}
