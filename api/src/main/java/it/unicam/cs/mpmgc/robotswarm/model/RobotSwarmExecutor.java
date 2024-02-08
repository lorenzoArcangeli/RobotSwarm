package it.unicam.cs.mpmgc.robotswarm.model;

import it.unicam.cs.mpmgc.robotswarm.utilites.FollowMeParser;
import it.unicam.cs.mpmgc.robotswarm.utilites.RobotFollowMeParserHandler;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class is used to execute a Robot Swarm model.
 *
 * @param <E> type of entities
 */
public class RobotSwarmExecutor<E extends Entity> {

    private Environment<E> currentEnvironment;
    private final ArrayList<Environment<E>> history=new ArrayList<>();

    private int index=0;

    private final int executionTime;

    /**
     * create a new entities swarm executor from the given environment
     * @param environment starting configuration
     * @param executionTime times to execute
     */
    public RobotSwarmExecutor(Environment<E> environment, int executionTime){
        this.currentEnvironment =environment;
        this.executionTime=executionTime;
    }

    /**
     * create a new entities swarm executor from the given environment
     * with infinite time of execution
     * @param environment starting configuration
     */
    public RobotSwarmExecutor(Environment<E> environment){
        this.currentEnvironment =environment;
        this.executionTime=Integer.MAX_VALUE;
    }

    /**
     * compute next instruction of the entities program
     */
    public void computeNextStep(){
        if(index<executionTime){
            history.add(currentEnvironment.createCopy());
            currentEnvironment=currentEnvironment.moveEntity();
            index++;
        }else{
            System.out.println("Execution is finished");
        }

    }

    /**
     * print the environment
     */
    public synchronized void stepForward() {
        if (index<=history.size()) {
            System.out.println(history.get(index-1));
        } else {
            System.out.println("No more environment in the history");
        }
    }

    /**
     * returns the current environment
     * @return the current environment
     */
    public synchronized Environment<E> getCurrentEnvironment(){
        return history.get(index);
    }

    /**
     * returns the environment in the given index
     * @param index of the environment int the history
     * @return the environment in the given index
     */
    public synchronized Environment<E> getEnvironmentByIndex(int index){
        return history.get(index);
    }

    /**
     * goes back in the history
     */
    public synchronized void stepBackward() {
        if (index>0) {
            index--;
            System.out.println(history.get(index));
        }else{
            System.out.println("This is the started environment");
        }
    }


    public static void main(String[] args) {
        try {
            RobotFollowMeParserHandler handler=new RobotFollowMeParserHandler();
            FollowMeParser followMe=new FollowMeParser(handler);
            RobotEnvironment env=new RobotEnvironment(createRobots(inputNumber("Robot number: ")),
                    followMe.parseEnvironment(Paths.get("environmentCondition.txt")));
            handler.setEnvironment(env);
            followMe.parseRobotProgram(Paths.get("robotMovement.txt"));
            int executionNumber=inputNumber("Execution time: ");
            RobotSwarmExecutor<Robot> executor=new RobotSwarmExecutor<>(env,executionNumber);
            int index=0;
            while(index<executionNumber){
                executor.computeNextStep();
                //only to make the execution more viewable
                Thread.sleep(1000);
                executor.stepForward();
                index++;
            }
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static int inputNumber(String message){
        Scanner nRobot;
        do{
            System.out.print(message);
            nRobot= new Scanner(System.in);
        }while(!nRobot.hasNextInt());
        return nRobot.nextInt();
    }
    private static List<Robot> createRobots(int n){
        List<Robot> robots=new ArrayList<>();
        for(int i=0;i<n;i++)
            robots.add(new Robot(new Coordinate(calculateRandomValue(), calculateRandomValue())));
        return robots;
    }

    private static double calculateRandomValue(){
        return (Math.random() * (20));
    }
}
