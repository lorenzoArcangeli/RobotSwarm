package it.unicam.cs.mpmgc.robotswarm.model;

import it.unicam.cs.mpmgc.robotswarm.command.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handle the instruction pointer of the robot program
 */

public class InstructionPointerHandler {

    /**
     * index of the program
     */
    private int index;

    /**
     * list of the loop command
     */
    private final List<Integer> toRepeatCommandIndex;

    /**
     * list of the number of iterations of the loop command
     */
    private final List<Integer> toRepeatCommandNumIter;

    /**
     * robot program
     */
    private List<Command<Robot>> commands;

    /**
     * last Done Command index
     */
    private int doneCommandIndex;

    /**
     * handle te Continue command index
     */
    private int continueIndex;

    /**
     * Index of the nested loop
     */
    int nestedCycleIndex=0;

    public InstructionPointerHandler(){
        index=0;
        toRepeatCommandIndex=new ArrayList<>();
        toRepeatCommandNumIter=new ArrayList<>();
        continueIndex=0;
        doneCommandIndex=-1;
    }

    /**
     * set the robot program
     * @param commands list of instructions
     */
    public void setCommands(List<Command<Robot>> commands) {
        this.commands = commands;
    }

    /**
     * increment the current index by value
     * @param value to add to the index
     */
    public void incrementIndex(int value){
        index+=value;
    }

    private void setIndex(int value){
        index=value;
    }

    /**
     * add a loop command when it is discovered
     */
    public void addToRepeatCommandIndex(){
        this.toRepeatCommandIndex.add(index);
    }

    /**
     * set the initial number of iteration
     */
    public void addToRepeatCommandNumIter(){
        this.toRepeatCommandNumIter.add(0);
    }

    /**
     * returns the number of iterations of the current loop
     * @return the number of iterations of the current loop
     */
    public int getCurrentToRepeatCommandNumIter(){
        return this.toRepeatCommandNumIter.get(nestedCycleIndex-1);
    }

    /**
     * update the nested loop index
     * @param value to add to the nested loop index
     */
    public void changeNestedCommandIndex(int value){
        this.nestedCycleIndex+=value;
    }

    /**
     * increment the number of iterations of the current loop
     */
    public void incrementToRepeatCommandNumIter(){
        this.toRepeatCommandNumIter.set(nestedCycleIndex-1, toRepeatCommandNumIter.get(nestedCycleIndex-1)+1);
    }

    /**
     * reset the number of iterations of the current loop
     */
    public void resetLastNumIteration(){
        this.toRepeatCommandNumIter.set(nestedCycleIndex-1,0);
    }

    /**
     * set the index to the start of last loop
     */
     public void goToLastRepeatCommand(){
        setIndex(toRepeatCommandIndex.get(nestedCycleIndex-1));
    }

    /**
     * set index to the first applicable command
     */
    public void goToFirstApplicableCommand(){
        if(!isDoneCommandNotSet())
            setIndex(doneCommandIndex+1);
        else
            setIndex(findFirstApplicableCommand()+1);
    }

    /**
     * search the first applicable command index if the Done command is not already set
     * @return the first applicable command index
     */
    private int findFirstApplicableCommand(){
        int numRepeatCommand=0;
        int numDoneCommand=0;
        for(int i=index;i<commands.size();i++){
            if(isRepeatCommand(i))
                numRepeatCommand++;
            if(isDoneCommand(i))
                numDoneCommand++;
            if(numDoneCommand==numRepeatCommand){
                doneCommandIndex=i;
                return i;
            }
        }
        return -1;
    }

    private boolean isRepeatCommand(int i){
        return commands.get(i).getClass()== RepeatCommand.class||commands.get(i).getClass()== UntilCommand.class
                ||commands.get(i).getClass()== DoForeverCommand.class;
    }

    private boolean isDoneCommand(int i){
        return commands.get(i).getClass()== DoneCommand.class;
    }

    /**
     * returns the continue index
     * @return the continue index
     */
    public int getContinueIndex() {
        return continueIndex;
    }

    /**
     * increment the continue index by 1
     */
    public void incrementContinueIndex() {
        continueIndex++;
    }

    /**
     * return if a Done command was already discovered
     * @return true if a Done command was already discovered
     */
    private boolean isDoneCommandNotSet(){
        return doneCommandIndex==-1;
    }

    /**
     * returns the program index
     * @return the program index
     */
    public int getIndex(){
        return index;
    }

    /**
     * set the Done command index
     * @param val index of the Done command
     */
    public void setDoneIndex(int val){
        doneCommandIndex=val;
    }

    /**
     * return if a loop was not already discovered
     * @return true if a loop was not already discovered
     */
    public boolean needAdd(){
        return this.nestedCycleIndex==this.toRepeatCommandIndex.size() && isDoneCommandNotSet();
    }

    /**
     * return if there are nested loop already discovered
     * @return true if there are nested loop
     */
    public boolean needIncrementNestedIndex(){
        return this.nestedCycleIndex<this.toRepeatCommandIndex.size();
    }
}
