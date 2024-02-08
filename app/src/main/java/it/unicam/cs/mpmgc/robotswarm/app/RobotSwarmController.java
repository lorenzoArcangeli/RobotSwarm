package it.unicam.cs.mpmgc.robotswarm.app;

import it.unicam.cs.mpmgc.robotswarm.Controller;
import it.unicam.cs.mpmgc.robotswarm.model.Robot;
import it.unicam.cs.mpmgc.robotswarm.utilites.ShapeData;
import javafx.animation.TranslateTransition;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RobotSwarmController {

    public static final int WIDTH = 635;
    public static final int HEIGHT = 600;
    public Button timeExecutingButton;
    @FXML
    private AnchorPane fieldArea;

    @FXML
    private Button zoomInButton;

    @FXML
    private Button zoomOutButton;

    @FXML
    private Button scrollLeftButton;

    @FXML
    private Button scrollRightButton;

    @FXML
    private Button scrollUpButton;

    @FXML
    private Button scrollDownButton;

    @FXML
    private Button stepForwardButton;

    @FXML
    private Button stepBackwardButton;

    @FXML
    private Button startButton;

    @FXML
    private TextField numRobotTextField;

    @FXML
    private TextField speedTextField;

    @FXML
    private TextArea commandsTextArea;

    @FXML
    private TextField timeExecutingTextField;
    private final List<ImageView> robotsView=new ArrayList<>();

    private final List<Color> conditionsColor=new ArrayList<>();

    private static Duration COMMAND_DURATION = Duration.seconds(1.0);

    private int TOTAL_X_SCROLL=0;

    private int TOTAL_Y_SCROLL=0;

    private static final int SCROLL =20;

    private double ZOOM=20;

    private boolean timeExecuteIsSet=false;
    private int timeToExecute=-1;
    private Controller<Robot> controller;

    /**
     * verify is the text of the given textfield is a number
     * @param textField to verify
     * @return true if the text of the given textfield is a number
     */
    private boolean verifyIntTextField(TextField textField){
        try{
            if(Integer.parseInt(textField.getText())>0)
                return true;
            else
                new Alert(Alert.AlertType.WARNING, "Has to be > 0" , ButtonType.OK).show();
        }catch (Exception e) {
            textField.setText("Number");
            new Alert(Alert.AlertType.WARNING, "Insert number!" , ButtonType.OK).show();
        }
        return false;
    }

    public void initialize() {
        changeButtonProperty(true);
        numRobotTextField.setText("Robot:");
        speedTextField.setText("Speed:");
        timeExecutingTextField.setText("N. instruction: ");
    }

    private void setRandomConditionsColor(){
        for(int i=0;i<controller.getEnvironment().getShapesData().size();i++)
            conditionsColor.add(randomColor());
    }

    /**
     * Set button to able/disable
     * @param value of the button
     */
    private void changeButtonProperty(boolean value){
        zoomInButton.setDisable(value);
        zoomOutButton.setDisable(value);
        scrollDownButton.setDisable(value);
        scrollUpButton.setDisable(value);
        scrollLeftButton.setDisable(value);
        scrollRightButton.setDisable(value);
        stepBackwardButton.setDisable(value);
        stepForwardButton.setDisable(value);
    }

    public void onZoomInCommand() {
        zoomIn();
    }
    public void onZoomOutCommand() {
        zoomOut();
    }

    /**
     * perform a zoom in
     */
    private void zoomIn(){
        if(ZOOM<50)
            ZOOM+=ZOOM/5;
        showEnvironment(new Duration(1));
    }

    /**
     * perform a zoom out
     */
    private void zoomOut(){
        if(ZOOM>15)
            ZOOM-=ZOOM/5;
        showEnvironment(new Duration(1));
    }

    public void onScrollLeftCommand() {
        rightLeftScroll(SCROLL);
    }

    /**
     * update the total X scroll and show the environment
     * @param scroll to right or left
     */
    private void rightLeftScroll(int scroll){
        TOTAL_X_SCROLL+=scroll;
        showEnvironment(new Duration(1));
    }

    /**
     * update the total Y scroll and show the environment
     * @param scroll to up or down
     */
    private void upDownScroll(int scroll) {
        TOTAL_Y_SCROLL+=scroll;
        showEnvironment(new Duration(1));
    }

    public void onScrollRightCommand() {
        rightLeftScroll(-SCROLL);
    }

    public void onScrollUpCommand() {
        upDownScroll(-SCROLL);
    }

    public void onScrollDownCommand() {
        upDownScroll(SCROLL);
    }

    /**
     * perform a step back word and change the time to execute if is necessary
     */
    public void onStepBackwardCommand() {
        controller.stepBackward();
        showEnvironment(COMMAND_DURATION);
        if(timeExecuteIsSet)
            incrementTimeExecute();
    }

    /**
     * perform a step back word and change the time to execute if is necessary
     */
    public void onStepForwardCommand() {
        if(timeExecuteIsSet&&timeToExecute>0){
            timeToExecute--;
            stepForward();
        }else if(!timeExecuteIsSet){
            stepForward();
        }

    }

    private void stepForward(){
        controller.stepForward();
        showEnvironment(COMMAND_DURATION);
    }

    private void incrementTimeExecute(){
        if(controller.backWardAvailable())
            timeToExecute++;
    }


    private void showProgram(File program) throws IOException {
        showProgramLines(Files.readAllLines(Path.of(program.getPath())));
    }

    private void showProgramLines(List<String> lines) {
        for (String line: lines) {
            commandsTextArea.appendText(line+"\n");
        }
    }

    public void onChangeSpeed(){
        if(verifyIntTextField(speedTextField))
            COMMAND_DURATION=new Duration(1000/Double.parseDouble(speedTextField.getText()));
    }


    /**
     * save the first environment robots position
     */
    private void saveRobotFirstPosition(){
        for(Robot r:controller.getEnvironment().getEntity()){
            ImageView imageView=new ImageView(new Image(Objects.requireNonNull(RobotSwarmController.class.getResource("/images/robot.png")).toString()));
            imageView.setFitHeight(ZOOM*2); imageView.setFitWidth(ZOOM*2);
            imageView.setX(r.getCoordinate().x()*ZOOM); imageView.setY(r.getCoordinate().y()*ZOOM);
            robotsView.add(imageView);
            fieldArea.getChildren().add(imageView);
        }
    }


    /**
     * show the environment
     * @param duration of the transition
     */
    private void showEnvironment(Duration duration){
        int index=0;
        removePreviousConditionsRobot();
        for(Robot r: controller.getEnvironment().getEntity()){
            double currentTranslateX = robotsView.get(index).getTranslateX();
            double currentTranslateY = robotsView.get(index).getTranslateY();
            robotsView.get(index).setFitHeight(ZOOM*2); robotsView.get(index).setFitWidth(ZOOM*2);
            transition(duration, currentTranslateX, currentTranslateY,(r.getCoordinate().x()*ZOOM)+TOTAL_X_SCROLL,
                    (r.getCoordinate().y()*ZOOM)+TOTAL_Y_SCROLL, robotsView.get(index));
            robotsView.get(index).setX(r.getCoordinate().x());
            robotsView.get(index).setY(r.getCoordinate().y());
            Label conditions = new Label();
            setLabelConditions(r, conditions);
            transition(duration, currentTranslateX+ZOOM*1.5, currentTranslateY-ZOOM/5,r.getCoordinate().x()*ZOOM+ZOOM*1.5+TOTAL_X_SCROLL,
                    r.getCoordinate().y()*ZOOM-ZOOM/5+TOTAL_Y_SCROLL, conditions);
            index++;
        }
        showConditions(controller.getEnvironment().getShapesData());
    }

    private void setLabelConditions(Robot r, Label conditions){
        conditions.toFront();
        conditions.setText(getRobotConditions(r));
        conditions.setFont(new Font(ZOOM/2));
        fieldArea.getChildren().add(conditions);
    }


    private String getRobotConditions(Robot r){
        String labels= "";
        for(String label: r.getConditions())
            labels+=label+" ";
        return labels;
    }

    /**
     * perform a transition
     * @param duration of the transition
     * @param startX start X value
     * @param startY start Y value
     * @param endX end X value
     * @param endY end Y value
     * @param node to apply the transition
     */
    private void transition(Duration duration, double startX, double startY, double endX, double endY, Node node){
        TranslateTransition translateTransition = new TranslateTransition(duration, node);
        translateTransition.setFromX(startX);
        translateTransition.setFromY(startY);
        translateTransition.setToX(endX);
        translateTransition.setToY(endY);
        translateTransition.play();
    }


    private void showConditions(List<ShapeData> shapesData){
        removePreviousConditions();
        int index=0;
        for(ShapeData shapeData: shapesData){
            if(shapeData.shape().equals("RECTANGLE")){
                showRectangleShapeData(shapeData, index);
            }else{
                showCircleShapeData(shapeData, index);
            }
            index++;
        }
    }

    private void showRectangleShapeData(ShapeData shapeData, int index){
        double x = shapeData.args()[0] - (shapeData.args()[2] / 2.0);
        double y = shapeData.args()[1] - (shapeData.args()[3] / 2.0);
        Rectangle rectangle=new Rectangle(x*ZOOM+TOTAL_X_SCROLL+ZOOM, y*ZOOM+TOTAL_Y_SCROLL+ZOOM, shapeData.args()[2]*ZOOM, shapeData.args()[3]*ZOOM);
        rectangle.setFill(conditionsColor.get(index));
        fieldArea.getChildren().add(rectangle);
    }

    private void showCircleShapeData(ShapeData shapeData, int index){
        Circle circle=new Circle(shapeData.args()[0]*ZOOM+TOTAL_X_SCROLL+ZOOM, shapeData.args()[1]*ZOOM+TOTAL_Y_SCROLL+ZOOM, shapeData.args()[2]*ZOOM);
        circle.setFill(conditionsColor.get(index));
        fieldArea.getChildren().add(circle);
    }

    private void removePreviousConditions(){
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node node : fieldArea.getChildren()) {
            if (isRectangleOrCircle(node))
                nodesToRemove.add(node);
        }
        fieldArea.getChildren().removeAll(nodesToRemove);
    }

    private void removePreviousConditionsRobot(){
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node node : fieldArea.getChildren()) {
            if (node.getClass().getSimpleName().equals("Label"))
                nodesToRemove.add(node);
        }
        fieldArea.getChildren().removeAll(nodesToRemove);
    }

    private Color randomColor(){
        double red = Math.random();
        double green = Math.random();
        double blue = Math.random();
        return new Color(red, green, blue, 0.5);
    }

    private boolean isRectangleOrCircle(Node node) {
        String className = node.getClass().getSimpleName();
        return className.equals("Rectangle") || className.equals("Circle");
    }

    @FXML
    private void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.LEFT) {
            onScrollRightCommand();
        }
        if (event.getCode() == KeyCode.RIGHT) {
            onScrollLeftCommand();
        }
        if (event.getCode() == KeyCode.UP) {
            onScrollUpCommand();
        }
        if (event.getCode() == KeyCode.DOWN) {
            onScrollDownCommand();
        }
        if (event.getCode() == KeyCode.I) {
            zoomIn();
        }
        if (event.getCode() == KeyCode.O) {
            zoomOut();
        }
        if (event.getCode() == KeyCode.F) {
            onStepForwardCommand();
        }
        if (event.getCode() == KeyCode.B) {
            onStepBackwardCommand();
        }
    }

    public void onTimeExecuting() {
        if(verifyIntTextField(timeExecutingTextField)){
            timeToExecute=Integer.parseInt(timeExecutingTextField.getText());
            timeExecuteIsSet=true;
        }
    }

    private File openFile(String fileInfo, Event event){
        FileChooser robotFileChooser = new FileChooser();
        robotFileChooser.setTitle(fileInfo);
        robotFileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Txt Files", "*.txt"));
        return robotFileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
    }

    /**
     * set the initial environment and show it
     */
    public void onStartCommandFile(Event event) {
        try {
            if(verifyIntTextField(numRobotTextField)) {
                File program=openFile("Robot program file", event);
                controller= Controller.startRobotController(Integer.parseInt(numRobotTextField.getText()),
                        openFile("Environment condition file", event), program);
                showProgram(program);
                changeButtonProperty(false);
                setRandomConditionsColor();
                saveRobotFirstPosition();
                showEnvironment(COMMAND_DURATION);
                fieldArea.requestFocus();
                startButton.setDisable(true);
            }
        }catch(Exception e) {
            new Alert(Alert.AlertType.WARNING, e.getMessage() , ButtonType.OK).showAndWait();
        }
    }
}
