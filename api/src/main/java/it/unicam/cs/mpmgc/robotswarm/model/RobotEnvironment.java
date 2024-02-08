package it.unicam.cs.mpmgc.robotswarm.model;

import it.unicam.cs.mpmgc.robotswarm.utilites.ShapeData;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Environment parameterized with Robot
 */
public class RobotEnvironment implements Environment<Robot>{

    /**
     * list of robots in the environment
     */
    private final List<Robot> robots;

    /**
     * list of the environment conditions
     */
    private final List<ShapeData> shapesData;

    /**
     * create a new robot environment with the given robots and conditions
     * @param robots to insert in the environment
     * @param shapesData to insert in the environment
     */
    public RobotEnvironment(List<Robot> robots, List<ShapeData> shapesData){
        this.robots=robots;
        this.shapesData=shapesData;
    }

    @Override
    public Coordinate getAverageCoordinateByLabel(String label, Robot entity, double distance) {
        return averageSwarmCoordinates(getSignalingRobot(label, getNearRobots(entity, distance)));
    }

    /**
     * returns the robots within the distance
     * @param robot represent the centre
     * @param distance within the other robots have to be
     * @return a list of robots within the distance
     */
    private List<Robot> getNearRobots(Robot robot, double distance){
        return this.robots
                .stream()
                .map(r->isNear(robot, r, distance))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    /**
     * verify if a robot is near to another
     * @param r1 first robot
     * @param r2 second robot
     * @param distance between the two robots
     * @return the second Robot only if the second robot is near to the first
     */
    private Optional<Robot> isNear(Robot r1, Robot r2, double distance){
        if(Math.sqrt(Math.pow(r1.getCoordinate().x()-r2.getCoordinate().x(),2)
                +Math.pow(r1.getCoordinate().y()-r2.getCoordinate().y(),2))<distance){
            return Optional.of(r2);
        }
        return Optional.empty();
    }

    /**
     * returns the robots that are signaling a condition
     * @param label of the condition
     * @param robots to verify
     * @return a list of the robots that are signaling a condition
     */
    private List<Robot> getSignalingRobot(String label, List<Robot> robots){
        return robots
                .stream()
                .filter(r-> r.isConditionPresent(label))
                .collect(Collectors.toList());
    }

    private Coordinate averageSwarmCoordinates(List<Robot> robots) {
        if(robots.isEmpty())
            return new Coordinate(0,0);
        Coordinate result=sumCoordinate(robots);
        return new Coordinate(result.x()/robots.size(), result.y()/robots.size());
    }

    private Coordinate sumCoordinate(List<Robot> robots){
        double sumX=0, sumY=0;
        for(Robot r : robots) {
            sumX += r.getCoordinate().x();
            sumY += r.getCoordinate().y();
        }
        return new Coordinate(sumX, sumY);
    }

    @Override
    public boolean verifyShapeData(String label, Robot r) {
        Optional<ShapeData> shapeData=getShapeDataByLabel(label);
        if(shapeData.isEmpty())
            throw new IllegalArgumentException("Label doesn't match with any conditions");
        return shapeData.get().contains(r.getCoordinate());
    }

    @Override
    public RobotEnvironment moveEntity() {
       for(Robot r: robots)
           r.applyCommand();
       return this;
    }

    @Override
    public List<ShapeData> getShapesData() {
        return this.shapesData;
    }

    private Optional<ShapeData> getShapeDataByLabel(String label){
        for (ShapeData s: this.shapesData) {
            if(s.label().equals(label))
                return Optional.of(s);
        }
        return Optional.empty();
    }


    @Override
    public List<Robot> getEntity() {
        return this.robots;
    }

    @Override
    public String toString(){
        return robotsToString()+environmentConditionToString();
    }

    /**
     * create a copy of the environment
     * @return the copy of the environment
     */
    @Override
    public  Environment<Robot> createCopy(){
        return new RobotEnvironment(copyRobots(), this.getShapesData());
    }

    private List<Robot> copyRobots(){
        return this.getEntity()
                .stream()
                .map(r->new Robot(r.getCoordinate(), r.getDirection(), r.getSpeed(), copyConditions(r.getConditions())))
                .collect(Collectors.toList());
    }

    private List<String> copyConditions(List<String> conditions){
        return conditions
                .stream()
                .map(String::new)
                .collect(Collectors.toList());
    }

    private String robotConditionToString(Robot r){
        StringBuilder s= new StringBuilder("Signaled conditions: ");
        for(String condition: r.getConditions()){
            s.append(condition).append(" ");
        }
        s.append("\n");
        return s.toString();
    }

    private String robotsToString(){
        StringBuilder s= new StringBuilder("Robots: \n");
        for(Robot r: robots){
            s.append("Coordinate: ").append(r.getCoordinate().x()).append(" ").append(r.getCoordinate().y())
                    .append(" - Direction: ").append(r.getDirection().x()).append(" ").append(r.getDirection().y())
                    .append(" - Speed: ").append(r.getSpeed()).append("\n");
            if(!r.getConditions().isEmpty())
                s.append(robotConditionToString(r));
        }
        return s.toString();
    }

    private String environmentConditionToString(){
        StringBuilder s= new StringBuilder("Environment conditions: \n");
        for(ShapeData sd: shapesData){
            s.append("Label: ").append(sd.label()).append(" ").append(" - Shape: ")
                    .append(sd.shape()).append(" - Arguments: ");
            for(double arg : sd.args())
                s.append(arg).append(" ");
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }
}
