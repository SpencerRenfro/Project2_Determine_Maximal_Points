package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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

//        Point(Point point) {
//            this.x = point.x;
//            this.y = point.y;
//        }

        public boolean isBelowAndLeft(Point other) {
            return this.x < other.x && this.y < other.y;
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

        ArrayList<PointCirclePair> pointCirclePairs = new ArrayList<>();

        ArrayList<Point> pointsList = null;
        ArrayList<Point> maximalPointsList = new ArrayList<>();

        // experimental flag for calling drae line method for points being added or removed
        boolean pointsChanged = false;

        public PaneClass(ArrayList<Point> initialPoints) {
            pointsList = initialPoints;




            //sort maximal points in order from smallest to largest by x coordinate using insertion sort
            //this.sortMaximalPoints();
            // draw maximal line
            //this.drawMaximalPointLines();

            //creating new pointCirclePair collection
            this.addInitialPoints(initialPoints);
            // now print list
            for (PointCirclePair pointCirclePair : pointCirclePairs) {
                System.out.println("Points in pair list" + pointCirclePair.point +"Circles in pair list" + pointCirclePair.circle);
            }
            //finding maximal points in circlePointPairs
            this.findMaximalPointsInPointCirclePair();
            //print maximal points in pointCirclePair
            this.printMaximalPointsInPointCirclePair();

            //change maximal points in pointCirclePairs to red
            for(PointCirclePair pair: pointCirclePairs) {
                if(pair.isMaximalPoint){
                    System.out.println("maximal point true, changing fill to red");
                    pair.circle.setFill(Color.RED);
                }
            }

            //print maximal points in pointCirclePair to show new fill color
            System.out.println("\n\n RE-PRINTING PAIRS LIST, FILL SHOULD BE CHANGED");
            this.printMaximalPointsInPointCirclePair();
            // sort maximalPairs
//            this.sortMaximalPointsInPointCirclePair();
            System.out.println("Printing sorted maximal points");
            this.printMaximalPointsInPointCirclePair();
            this.sortMaximalPoints();


            // change maximal points to green
//            for(Point point : maximalPointsList) {
//                Circle circle = new Circle();
//                circle.setCenterX((double) point.getX());
//                circle.setCenterY((double) point.getY());
//                circle.setRadius(5.0f);
//                circle.setFill(Color.GREEN);
//                this.getChildren().add(circle);
//            }

        }

        private static class PointCirclePair{
            public final Point point;
            public final Circle circle;
            public boolean isMaximalPoint = false;

            // Static shared list of maximal points
            public static ArrayList<PointCirclePair> maximalPointCirclePairs = new ArrayList<>();
            public static ArrayList<PointCirclePair> sortedPointCirclePairs = new ArrayList<>();
            public PointCirclePair(Point point,Circle circle){
                this.point = point;
                this.circle = circle;
            }
        }

        private  void addInitialPoints(ArrayList<Point> initialPoints){
            for(Point point : initialPoints) {
               Circle circle = this.createCircle(point.getX(),point.getY());
               this.pointCirclePairs.add(new PointCirclePair(point, circle));
               this.addPointToChildren(circle);
            }
        }

        private Circle createCircle(double x, double y){
            Circle circle = new Circle();
            circle.setCenterX(x);
            circle.setCenterY(y);
            circle.setRadius(5.0f);
            circle.setFill(Color.BLACK);
            return circle;
        }

        private void findMaximalPointsInPointCirclePair(){
            // each time this method runs, clear the previous maximal points
            for(PointCirclePair point: pointCirclePairs) {
                point.isMaximalPoint = false;
            }

            for(PointCirclePair pPoint: pointCirclePairs) {
                Point p = new Point(pPoint.point.getX(), pPoint.point.getY());
                boolean localIsMax = true;
                for(PointCirclePair qPair: pointCirclePairs){
                    Point q = new Point(qPair.point.getX(), qPair.point.getY());
                    if( p.getX() == q.getX() && p.getY() == q.getY() ){
                        System.out.println("p and q are equal");
                        continue;
                    }
                    if (q.getY() <= p.getY() && q.getX() > p.getX()){
                        localIsMax = false;
                        break;
                    }

                }
                if(localIsMax) {
                    pPoint.isMaximalPoint = true;
                    PointCirclePair.maximalPointCirclePairs.add(pPoint);

                }

            }
        }

        private void sortMaximalPoints() {
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

//        private void sortMaximalPointsInPointCirclePair() {
//            for(int i = 1; i < PointCirclePair.maximalPointCirclePairs.size(); i++){
//                Point currentValue = PointCirclePair.maximalPointCirclePairs.get(i).point;
//                int j = i -1;
//                while(j >= 0 && maximalPointsList.get(j).compareTo(currentValue) > 0 ){
//                    PointCirclePair.maximalPointCirclePairs.set(j + 1, PointCirclePair.maximalPointCirclePairs.get(j));
//                    j--;
//                }
//                maximalPointsList.set(j + 1, currentValue);
//            }
//
//        }

        private void mouseClickPrimaryHandler(MouseButton e, double x, double y){
            Circle circle = new Circle();
            circle.setCenterX(x);
            circle.setCenterY(y);
            circle.setFill(Color.BLACK);
            circle.setRadius(5.0f);
            // Add the point to the children list
            this.addPointToChildren(circle);
            //set the pointsCHanged flag to true
            this.pointsChanged = true;
        }

        private void drawMaximalPointLines(){
            for(int i = 0; i< maximalPointsList.size()-1; i++) {
                Line line = new Line();
                line.setStartX(maximalPointsList.get(i).getX());
                line.setStartY(maximalPointsList.get(i).getY());
                line.setEndX(maximalPointsList.get(i).getX());
                line.setEndY(maximalPointsList.get(i+1).getY());
                line.setStroke(Color.RED);
                this.getChildren().add(line);
            }
        }

        private void mouseClickSecondaryHandler(MouseButton e, double x, double y){

            if(this.getChildren().size()  >1){
                for(int i = this.getChildren().size() -1; i > 0 ; i--){
                    if(this.getChildren().get(i) instanceof Circle){
                        Circle circle = (Circle) this.getChildren().get(i);
                        if(circle.contains(x, y)) {
                            System.out.println("Point " + this.getChildren().get(i) + " was removed");
                            this.getChildren().remove(i);
                            // set the pointsChanged flag to true
                            this.pointsChanged = true;
                            break;
                        }
                    }

                }
            }


        }

        private void addPointToChildren(Circle circle) {
            this.getChildren().add(circle);
        }

        public void printMaximalPointsInPointCirclePair(){
            for(PointCirclePair point: pointCirclePairs) {
                if(point.isMaximalPoint) {
                    System.out.println(point.point);
                }
            }
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
        ArrayList<Point> paneTotalPointList = new ArrayList<>();
        // Read the file and get the points
        paneTotalPointList = ReadFile();

        // Create the pane and add the points to it
        PaneClass customPaneClass = new PaneClass(paneTotalPointList);

        // Mouse events for adding circles on left click and removing circles on right click

        customPaneClass.setOnMouseClicked(e -> {

            //ArrayList<Point> maximalLinePoints = null;

            // Add a circle on left click
            if (e.getButton() == MouseButton.PRIMARY) {
                // Display new point on screen
               customPaneClass.mouseClickPrimaryHandler(e.getButton() ,e.getX(), e.getY());

            }
            else if (e.getButton() == MouseButton.SECONDARY) {
                customPaneClass.mouseClickSecondaryHandler(e.getButton() ,e.getX(), e.getY());
            }

            // Call method to recalculate maximal line if a point was added or removed, use a flag
            //          maximalLinePoints = calculateListOfPointsOnMaximalLine( pointsList );
            //          drawMaximalLine( maximalLinePoints );
        });


        // Set the scene
        Scene scene = new Scene(customPaneClass, 1000, 700);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
















//    private ArrayList<Point> calculateListOfPointsOnMaximalLine( ArrayList<Point> pointsList )
//    {
//
//    }

//    private void drawMaximalLine( ArrayList<Point> maximalLinePoints )
//    {
//        // list will include every point necessary to draw the entire maximal line,
//        // including those necessary to start and end the line, and the corresponding
//        // points to step between existing points
//
//        for ( int i = 0; i < maximalLinePoints.size()-1; i++ )
//        {
//            // Draw line between this point and the next point
//
//          //  Line line = new Line(100, 10, 10, 110);
//
//        }
//    }


// removig the points with right click
/*

//                for (int i = customPaneClass.getChildren().size() - 1; i >= 0; i--) {
//                    if (customPaneClass.getChildren().get(i) instanceof Circle) {
//                        //can replace this with a pattern variable
//                        Circle circle = (Circle) customPaneClass.getChildren().get(i);
//                        if (circle.contains(e.getX(), e.getY())) {
//                            customPaneClass.getChildren().remove(i);
//                            break;  // stop after removing one circle
//                        }
//                    }
//                }



-------------
extra?

   // Use a reverse loop to safely remove items while iterating
//                int removalIndex = customPaneClass.mouseClickHandler(e.getButton(), e.getX(), e.getY());
//                if (removalIndex != -1) {
//                    customPaneClass.getChildren().remove(removalIndex);
//                }




--------------

other attempt

            else if (e.getButton() == MouseButton.SECONDARY) {
                // Use a reverse loop to safely remove items while iterating
//                int removalIndex = customPaneClass.mouseClickHandler(e.getButton(), e.getX(), e.getY());
//                if (removalIndex != -1) {
//                    customPaneClass.getChildren().remove(removalIndex);
//                }


*/