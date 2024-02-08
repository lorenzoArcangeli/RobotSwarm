import it.unicam.cs.mpmgc.robotswarm.command.*;
import it.unicam.cs.mpmgc.robotswarm.model.*;
import it.unicam.cs.mpmgc.robotswarm.util.Normalizer;
import it.unicam.cs.mpmgc.robotswarm.utilites.RobotFollowMeParserHandler;
import it.unicam.cs.mpmgc.robotswarm.utilites.ShapeData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RobotSwarmTest {

    @Test
    public void moveCommandTest(){
        MoveCommand move=new MoveCommand(new Coordinate(1,1), 1);
        Robot r=new Robot(new Coordinate(0,0), new Coordinate(0,0),0);
        move.apply(r, new InstructionPointerHandler());
        Assertions.assertEquals(r.getCoordinate(), new Coordinate(1,1));
        Assertions.assertEquals(r.getDirection(), new Coordinate(1,1));
        Assertions.assertEquals(r.getSpeed(), 1);
    }

    @Test
    public void moveCommandThrowExceptionTest(){
        MoveCommand move=new MoveCommand(new Coordinate(1,2), 1);
        Robot r=new Robot(new Coordinate(0,0), new Coordinate(0,0),0);
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                move.apply(r, new InstructionPointerHandler()));
    }


    @Test
    public void signalTest(){
        List<Robot> robots=createNRobot(5);
        SignalCommand signalCommand=new SignalCommand("A");
        for(Robot r: robots) {
            signalCommand.apply(r, new InstructionPointerHandler());
            Assertions.assertTrue(r.isConditionPresent("A"));
        }
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                signalCommand.apply(robots.get(0), new InstructionPointerHandler()));
        SignalCommand invalidSignalCommand=new SignalCommand("€");
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                invalidSignalCommand.apply(robots.get(0), new InstructionPointerHandler()));
    }

    @Test
    public void unsignalTest(){
        List<Robot> robots=createNRobot(5);
        SignalCommand signalCommand=new SignalCommand("A");
        for(Robot r: robots) {
            signalCommand.apply(r, new InstructionPointerHandler());
        }
        UnsignalCommand unsignalCommand=new UnsignalCommand("A");
        unsignalCommand.apply(robots.get(0), new InstructionPointerHandler());
        Assertions.assertFalse(robots.get(0).isConditionPresent("A"));
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                unsignalCommand.apply(robots.get(0), new InstructionPointerHandler()));
    }

    @Test
    public void FollowCommandTest(){
        List<Robot> robots=createNRobot(5);
        List<ShapeData> shapeData=new ArrayList<>();
        shapeData.add(new ShapeData("A","RECTANGLE", new double[]{5,5,6,6}));
        RobotEnvironment environment=new RobotEnvironment(robots, shapeData);
        FollowCommand followCommand=new FollowCommand(environment, "A", 2,1);
        for(Robot r: environment.getEntity())
            new SignalCommand("A").apply(r, new InstructionPointerHandler());
        for(Robot r: environment.getEntity()){
            followCommand.apply(r, new InstructionPointerHandler());
        }
        assertEquals(robots.get(0).getCoordinate(), new Coordinate(0.5,0.5));
        assertEquals(robots.get(1).getCoordinate(), new Coordinate(3.5/3,3.5/3));
    }

    @Test
    public void normalizerTest(){
        Normalizer n=new Normalizer(new Coordinate(2,3), new Coordinate(8,6));
        assertEquals(n.getCorrectDirection(), new Coordinate(1,0.5));
        n=new Normalizer(new Coordinate(2,3), new Coordinate(0,-5));
        assertEquals(n.getCorrectDirection(), new Coordinate(-0.25,-1));
    }

    @Test
    public void stopCommandTest(){
        Robot r=new Robot(new Coordinate(0,0), new Coordinate(1,1),2);
        List<Command<Robot>> commands=new ArrayList<>();
        commands.add(new MoveCommand(new Coordinate(1,1),5));
        commands.add(new StopCommand());
        r.addCommands(commands);
        r.applyCommand();
        assertEquals(r.getCoordinate(), new Coordinate(5,5));
        r.applyCommand();
        assertEquals(r.getCoordinate(), new Coordinate(5,5));
        assertEquals(r.getDirection(), new Coordinate(0,0));
        assertEquals(r.getSpeed(), 0);
    }

    @Test
    public void continueCommandTest(){
        Robot r=new Robot(new Coordinate(0,0), new Coordinate(1,1),2);
        List<Command<Robot>> commands=new ArrayList<>();
        commands.add(new ContinueCommand(2));
        commands.add(new StopCommand());
        r.addCommands(commands);
        r.applyCommand();
        assertEquals(r.getCoordinate(), new Coordinate(2,2));
        r.applyCommand();
        assertEquals(r.getCoordinate(), new Coordinate(4,4));
        r.applyCommand();
        assertEquals(r.getCoordinate(), new Coordinate(4,4));
    }

    @Test
    public void repeatCommandTest(){
        Robot r=new Robot(new Coordinate(0,0), new Coordinate(0,0),1);
        List<Command<Robot>> commands=new ArrayList<>();
        commands.add(new RepeatCommand(2));
        commands.add(new MoveCommand(new Coordinate(1,1),1));
        commands.add(new RepeatCommand(2));
        commands.add(new MoveCommand(new Coordinate(1,1),2));
        commands.add(new DoneCommand());
        commands.add(new DoneCommand());
        r.addCommands(commands);
        for(int i=0;i<4;i++){
            r.applyCommand();
        }
        assertEquals(r.getCoordinate(), new Coordinate(6,6));
        assertEquals(r.getDirection(), new Coordinate(1,1));
        assertEquals(r.getSpeed(), 1);
    }

    @Test
    public void untilCommandTest(){
        List<Robot> robots=createNRobot(1);
        List<ShapeData> shapeData=new ArrayList<>();
        shapeData.add(new ShapeData("A", "RECTANGLE", new double[]{4,4,2,2}));
        RobotEnvironment environment=new RobotEnvironment(robots, shapeData);
        List<Command<Robot>> commands=new ArrayList<>();
        commands.add(new UntilCommand(environment, "A"));
        commands.add(new MoveCommand(new Coordinate(1,1), 1));
        commands.add(new DoneCommand());
        commands.add(new StopCommand());
        robots.get(0).addCommands(commands);
        for(int i=0;i<10;i++){
            robots.get(0).applyCommand();
        }
        assertEquals(new Coordinate(3.0,3.0), robots.get(0).getCoordinate());
        assertEquals(new Coordinate(0,0), robots.get(0).getDirection());
        assertEquals(0, robots.get(0).getSpeed());
    }

    @Test
    public void doForeverCommandTest(){
        List<Robot> robots=createNRobot(1);
        List<Command<Robot>> commands=new ArrayList<>();
        commands.add(new DoForeverCommand());
        commands.add(new MoveCommand(new Coordinate(1,1), 1));
        commands.add(new DoneCommand());
        robots.get(0).addCommands(commands);
        for(int i=0;i<10;i++){
            robots.get(0).applyCommand();
        }
        assertEquals(new Coordinate(10.0,10.0), robots.get(0).getCoordinate());
        assertEquals(new Coordinate(1,1), robots.get(0).getDirection());
        assertEquals(1, robots.get(0).getSpeed());
    }


    @Test
    public void robotSwarmExecutorTest(){
        RobotFollowMeParserHandler handler=new RobotFollowMeParserHandler();
        List<Robot> robots=createNRobot(5);
        RobotEnvironment env=new RobotEnvironment(robots, new ArrayList<>());
        handler.setEnvironment(env);
        List<Command<Robot>> commands=Collections.singletonList(new MoveCommand(new Coordinate(1,1),1));
        for(Robot r: robots)
            r.addCommands(commands);
        RobotSwarmExecutor<Robot> executor=new RobotSwarmExecutor<>(env);
        executor.computeNextStep();
        executor.stepBackward();
        assertEquals(executor.getEnvironmentByIndex(0), executor.getCurrentEnvironment());
    }

    private List<Robot> createNRobot(int n) {
        List<Robot> robots = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            robots.add(new Robot(new Coordinate(i, i), new Coordinate(0,0),0));
        }
        return robots;
    }


}
