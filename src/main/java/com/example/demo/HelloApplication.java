package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;


import javafx.stage.Stage;

import java.io.*;

// my imports
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseButton;
import java.util.ArrayList;
import java.io.FileReader;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class HelloApplication extends Application {

    public ArrayList<Point> ReadFile() {
        // This class will read the file and return an ArrayList of points
        // The file will be in the format of x,y
        // The file will be read line by line and each line will be split by the comma
        // The x and y values will be parsed as doubles and added to the ArrayList
        ArrayList<Point> initalList = new ArrayList<>();
        BufferedReader reader = null;
        String line = null;

        try{
            reader = new BufferedReader(new FileReader("src/InitialPoints.txt"));
            System.out.println("File found");
        } catch(FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
        try {

            while((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                Point point = new Point(x, y);
                initalList.add(point);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return initalList;

    };

    /*
 This program should consist of three classes. The first class should have two instance variables
of type double that represent the x and y coordinates of the point. It should be an immutable
class the implements the Comparable interface with the following public methods:

             A constructor that initializes the x and y coordinates of the point
             A method that returns the x ordinate
             A method that returns the y ordinate
             A method that is passed a second point and returns true if the second point is below and
            to the left of the point on which it is invoked and false otherwise
             A compareTo method that compares only the x ordinates of the two points
    */
    public static final class Point implements Comparable<Point> {

        private final double x;
        private final double y;

        Point(double xValue, double yValue) {
            this.x = xValue;
            this.y = yValue;
        }

        public boolean isBelowAndLeft(Point other) {
            return other.x < this.x && other.y < this.y;
        }

        public double getX() {
            return x;
        }
        public double getY() {
            return y;
        }

        @Override
            public int compareTo(Point other) {
                return Double.compare(this.x, other.x);
            }

        @Override
        public String toString() {
            return "Point[x=" + x + ", y=" + y + "]";
        }

    }

    /*
     A constructor that is supplied an array list of points that produces the initial point set and
    determines the maximal points and connects them
     A private event handler that handles mouse clicks that adds a point with a left click,
    removes a point with a right click and recomputes the maximal point set afterward
     A private method that finds the maximal set and draws the lines that connect them
    */
    public static class PaneClass extends Pane {

        ArrayList<Point> pointsList = null;
        ArrayList<Point> maximalPointsList = new ArrayList<>();
        boolean isMaximal = true;
        double largestX = Double.MIN_VALUE;
        double largestY = Double.MIN_VALUE;



        public PaneClass(ArrayList<Point> initialPoints) {
            pointsList = initialPoints;

            // set the largest x and y coordinates // which also dynamically sets the size of the window
            this.setGreatestXY();
            for(Point point : pointsList) {
                Circle circle = new Circle();
                circle.setCenterX((double) point.getX());
                circle.setCenterY((double) point.getY());
                circle.setRadius(5.0f);
                circle.setFill(Color.BLACK);
                this.getChildren().add(circle);
            }
            // find Maximal Points
            this.findMaximalPoints();
//            System.out.println("Maximal Points: ");
//            System.out.println(maximalPointsList);


            //sort maximal points in order from smallest to largest by x coordinate using insertion sort
            this.sortMaximalPoints();
            //change color of maximal points
            this.changeMaximalPointColor();
            // draw maximal line
            this.drawMaximalPointLines();

//            System.out.println("Sorted Maximal Points: ");
//            System.out.println(maximalPointsList);

        }

        public void changeMaximalPointColor(){
            //reset any previous maximal colors
            for(int i = 0; i < this.getChildren().size(); i++){
                if(this.getChildren().get(i) instanceof Circle){
                    ( (Circle) this.getChildren().get(i) ).setFill(Color.BLACK);
                }
            }
            // change maximal points to green
            for(Point point : maximalPointsList) {

                for(int i = 0; i < this.getChildren().size(); i++){
                    if (this.getChildren().get(i) instanceof Circle
                            && ((Circle) this.getChildren().get(i)).getCenterX() == point.getX()
                            && ((Circle) this.getChildren().get(i)).getCenterY() == point.getY()) {

                        ((Circle) this.getChildren().get(i)).setFill(Color.GREEN);
                    }
                }

            }
        }

        public void findMaximalPoints() {
            //reset any previous maximal points
            maximalPointsList.clear();
            for(Point p : pointsList) {
                this.isMaximal = true;
                for(Point q : pointsList) {
                    if(p.getX() == q.getX() && p.getY() == q.getY()){
                        continue;
                    }
                    if(q.getY() <= p.getY() && q.getX() > p.getX()) {
                        isMaximal = false;
                        break;
                    }
                }
                if(isMaximal) {
                    maximalPointsList.add(p);
                }
            }


        }

        public void sortMaximalPoints() {
            for(int i = 1; i < maximalPointsList.size(); i++){
                Point currentValue = maximalPointsList.get(i);
                int j = i - 1;
                while(j >= 0 && maximalPointsList.get(j).compareTo(currentValue) > 0 ){
                    maximalPointsList.set(j + 1, maximalPointsList.get(j));
                    j--;
                }
                maximalPointsList.set(j + 1, currentValue);

            }
        }

        public void drawMaximalPointLines(){
            // remove all previously drawn lines
            for(int i = this.getChildren().size() - 1; i >= 0; i--){
                if(this.getChildren().get(i) instanceof Line){
                    this.getChildren().remove(i);
                }
            }
            for(int i = 0; i< maximalPointsList.size()-1; i++) {
                Line line1 = new Line();
                Line line2 = new Line();



                line1.setStartX(maximalPointsList.get(i).getX());
                line1.setStartY(maximalPointsList.get(i).getY());
                line1.setEndX(maximalPointsList.get(i).getX());
                line1.setEndY(maximalPointsList.get(i+1).getY());
                line1.setStroke(Color.RED);
                line2.setStartX(maximalPointsList.get(i).getX());
                line2.setStartY(maximalPointsList.get(i+1).getY());
                line2.setEndX(maximalPointsList.get(i+1).getX());
                line2.setEndY(maximalPointsList.get(i+1).getY());
                this.getChildren().add(line1);
                this.getChildren().add(line2);
            }
        }

        public void setGreatestXY() {

            double maxX = Double.MIN_VALUE;
            double maxY = Double.MIN_VALUE;
            for(Point point : pointsList) {
                if(point.getX() > maxX) {
                    maxX = point.getX();
                }
                if(point.getY() > maxY) {
                    maxY = point.getY();
                }
            }

            this.largestX = maxX;
            this.largestY = maxY;
        }

        private void mouseClickPrimaryHandler(MouseButton e, double x, double y){
            Circle circle = new Circle();
            circle.setCenterX(x);
            circle.setCenterY(y);
            circle.setFill(Color.BLACK);
            circle.setRadius(5.0f);
            // Add the point to the children list
            this.getChildren().add(circle);
            // add this to pointsList
            Point currentPoint = new Point(x,y);
            this.pointsList.add(currentPoint);
        }

        private void mouseClickSecondaryHandler(MouseButton e, double x, double y){

            if(this.getChildren().size()  >= 0){
                for(int i = this.getChildren().size() -1; i >= 0 ; i--){
                    if(this.getChildren().get(i) instanceof Circle){
                        Circle circle = (Circle) this.getChildren().get(i);
                        if(circle.contains(x, y)) {
                            System.out.println("Point " + this.getChildren().get(i) + " was removed");
                            this.getChildren().remove(i);
                            //now remove from pointsList
                            this.pointsList.remove(i);
                            break;
                        }
                    }

                }
            }
        }

        private void addPointToChildren(Circle circle) {
            this.getChildren().add(circle);
        }

        public void printChildren() {
           for(int i = 0; i < pointsList.size(); i++) {
               System.out.println("Point " + i + ": " + pointsList.get(i));
           }
        }
     }

    @Override
    public void start(Stage stage) throws IOException {

        stage.setTitle("Project 2");
        ArrayList<Point> initialPointsFile = new ArrayList<>();
        // Read the file and get the points
        initialPointsFile = ReadFile();

        // Create the pane and add the points to it
        PaneClass customPaneClass = new PaneClass(initialPointsFile);

        // Mouse events for adding circles on left click and removing circles on right click
        customPaneClass.setOnMouseClicked(e -> {
            boolean pointsChanged = false;


            // Add a circle on left click
            if (e.getButton() == MouseButton.PRIMARY) {
               customPaneClass.mouseClickPrimaryHandler(e.getButton() ,e.getX(), e.getY());
               pointsChanged = true;

            }
            else if (e.getButton() == MouseButton.SECONDARY) {
                customPaneClass.mouseClickSecondaryHandler(e.getButton() ,e.getX(), e.getY());
                pointsChanged = true;
            }
            if (pointsChanged) {
                customPaneClass.setGreatestXY();
                stage.setWidth(customPaneClass.largestX + 100);
                stage.setHeight(customPaneClass.largestY + 100);
                customPaneClass.findMaximalPoints();
                customPaneClass.sortMaximalPoints();
                customPaneClass.changeMaximalPointColor();
                customPaneClass.drawMaximalPointLines();
            }
        });

        // Set the scene
        Scene scene = new Scene(customPaneClass, customPaneClass.largestX + 100, customPaneClass.largestY + 100);
        stage.setScene(scene);
        stage.show();

    }
    public static void main(String[] args) {
        launch();
    }
}