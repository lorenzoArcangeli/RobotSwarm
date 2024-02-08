package it.unicam.cs.mpmgc.robotswarm.utilites;
import it.unicam.cs.mpmgc.robotswarm.command.*;
import it.unicam.cs.mpmgc.robotswarm.model.Coordinate;
import it.unicam.cs.mpmgc.robotswarm.model.Robot;
import it.unicam.cs.mpmgc.robotswarm.model.RobotEnvironment;

import java.util.ArrayList;
import java.util.List;


public class RobotFollowMeParserHandler implements FollowMeParserHandler{

    /**
     * environment used by the commands
     */
    private RobotEnvironment environment;

    /**
     * list of the commands
     */
    private List<Command<Robot>> commands;


    public void setEnvironment(RobotEnvironment env){
        this.environment=env;
    }

    @Override
    public void parsingStarted() {
        commands=new ArrayList<>();
    }

    @Override
    public void parsingDone() {
        for(Robot r: this.environment.getEntity())
            r.addCommands(this.commands);
        System.out.println("d");
    }

    @Override
    public void moveCommand(double[] args) {
        commands.add(new MoveCommand(new Coordinate(args[0], args[1]), args[2]));
    }

    @Override
    public void moveRandomCommand(double[] args) {
        commands.add(new MoveRandomCommand(new Coordinate(args[0], args[1]),
                new Coordinate(args[2], args[3]), args[4]));
    }

    @Override
    public void signalCommand(String label) {
        commands.add(new SignalCommand(label));
    }

    @Override
    public void unsignalCommand(String label) {
        commands.add(new UnsignalCommand(label));
    }

    @Override
    public void followCommand(String label, double[] args) {
        commands.add(new FollowCommand(this.environment, label, args[0], args[1]));
    }

    @Override
    public void stopCommand() {
        commands.add(new StopCommand());
    }

    @Override
    public void continueCommand(int s) {
        commands.add(new ContinueCommand(s));
    }

    //In questo non metto il controllo dato che non eseguono in prima persona un comando
    @Override
    public void repeatCommandStart(int n) {
        commands.add(new RepeatCommand(n));
    }

    @Override
    public void untilCommandStart(String label) {
        commands.add(new UntilCommand(this.environment, label));
    }

    @Override
    public void doForeverStart() {
        commands.add(new DoForeverCommand());
    }

    @Override
    public void doneCommand() {
        commands.add(new DoneCommand());
    }

}
