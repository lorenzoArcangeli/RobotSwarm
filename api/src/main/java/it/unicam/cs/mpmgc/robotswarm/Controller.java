package it.unicam.cs.mpmgc.robotswarm;

import it.unicam.cs.mpmgc.robotswarm.model.*;
import it.unicam.cs.mpmgc.robotswarm.utilites.FollowMeParser;
import it.unicam.cs.mpmgc.robotswarm.utilites.FollowMeParserException;
import it.unicam.cs.mpmgc.robotswarm.utilites.RobotFollowMeParserHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is used to control the activities around a Robot Swarm execution.
 */
public class Controller<E extends Entity> {

    private Environment<E> currentEnvironment;

    private final ArrayList<Environment<E>> history=new ArrayList<>();

    private int index=0;

    /**
     * parse of environment conditions, robot program and create the robot
     * @param nRobot number of robot in te environment
     * @param robotFile file of robots program
     * @param environmentFile file of environment conditions
     * @throws FollowMeParserException if there are errors during the parse of the files
     * @throws IOException if the there are problems with given file
     */
    public static Controller<Robot> startRobotController(int nRobot, File robotFile, File environmentFile) throws FollowMeParserException, IOException {
        RobotFollowMeParserHandler handler = new RobotFollowMeParserHandler();
        FollowMeParser followMe = new FollowMeParser(handler);
        RobotEnvironment env = new RobotEnvironment(createRobots(nRobot), followMe.parseEnvironment(robotFile));
        handler.setEnvironment(env);
        followMe.parseRobotProgram(environmentFile);
        return new Controller<>(env);
    }

    /**
     * create a new Controller from the given environment
     * @param environment starting configuration
     */
    public Controller(Environment<E> environment){
        currentEnvironment=environment;
        this.history.add(environment.createCopy());
    }


    private static List<Robot> createRobots(int n){
        List<Robot> robots=new ArrayList<>();
        for(int i=0;i<n;i++)
            robots.add(new Robot(new Coordinate(calculateRandomValue(), calculateRandomValue())));
        return robots;
    }

    private static double calculateRandomValue(){
        return (Math.random() * (40));
    }

    /**
     * compute next step of the robot program
     */
    private void computeNextStep(){
        currentEnvironment=currentEnvironment.moveEntity();
        history.add(currentEnvironment.createCopy());
    }

    /**
     * make a step forward if necessary
     */
    public synchronized void stepForward() {
        if(needExecution())
            computeNextStep();
        index++;
    }

    /**
     * returns the current environment in the history
     * @return the current environment in the history
     */
    public synchronized Environment<E> getEnvironment(){
        return history.get(index);
    }

    /**
     * step back in the history if is not the start environment
     */
    public synchronized void stepBackward() {
        if(backWardAvailable())
            index--;
    }

    /**
     * returns if an execution is necessary
     * @return true if an execution is necessary
     */
    private boolean needExecution(){
        return (index+1)==history.size();
    }

    /**
     * returns if there is previous environments in the history
     * @return true if there is previous environments in the history
     */
    public boolean backWardAvailable(){
        return index>0;
    }
}
