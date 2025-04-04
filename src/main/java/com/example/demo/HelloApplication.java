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
import java.util.TreeSet;


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

    public final class Point implements Comparable<Point> {

        private final double x;
        private final double y;

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
        Point(double xValue, double yValue) {
            this.x = xValue;
            this.y = yValue;
        }

        /// MIGHT NEED TO SWAP > FOR < FOR THIS METHOD TO WORK PROPERLY
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


    public static class PaneClass extends Pane {
        /*
         A constructor that is supplied an array list of points that produces the initial point set and
        determines the maximal points and connects them
         A private event handler that handles mouse clicks that adds a point with a left click,
        removes a point with a right click and recomputes the maximal point set afterward
         A private method that finds the maximal set and draws the lines that connect them
        */

        ArrayList<Point> pointsList = null;

        public PaneClass(ArrayList<Point> initialPoints) {

            pointsList = initialPoints;
            ArrayList<Point> maximalPointsList = new ArrayList<>();
            boolean isMaximal = true;

            TreeSet<Point> sortedPoints = new TreeSet<>();

            Circle largestCircle = new Circle();
            largestCircle.setCenterX(0);
            largestCircle.setCenterY(0);

            Circle smallestCircle = new Circle();
            smallestCircle.setCenterX(0);
            smallestCircle.setCenterY(0);

            for(Point point : pointsList) {
                Circle circle = new Circle();
                circle.setCenterX((double) point.getX());
                circle.setCenterY((double) point.getY());
                circle.setRadius(5.0f);
                circle.setFill(Color.BLACK);
                this.getChildren().add(circle);
                // Check if this point is the largest
                if (point.getX() > largestCircle.getCenterX()) {
                    largestCircle.setCenterX(point.getX());
                    largestCircle.setCenterY(point.getY());
                }
                // Check if this point is the smallest
                if (point.getX() < smallestCircle.getCenterX()) {
                    smallestCircle.setCenterX(point.getX());
                    smallestCircle.setCenterY(point.getY());
                }

            }
            // find Maximal Points
            for(Point p : pointsList) {
                isMaximal = true;
                for(Point q : pointsList) {
                    if(p.getX() == q.getX() && p.getY() == q.getY()){
                        System.out.println("p and q are equal");
                        continue;
                    }
                    if(q.getY() <= p.getY() && q.getX() > p.getX()) {
                        isMaximal = false;
                        System.out.println("Point " + p + " is not maximal because of point " + q);
                        break;
                    }
                }
                if(isMaximal) {
                    maximalPointsList.add(p);
                }
            }




            System.out.println("Maximal Points: ");
            System.out.println(maximalPointsList);


            //sort maximal points in order from smallest to largest by x coordinate
            for(int i = 1; i < maximalPointsList.size(); i++){
                Point currentValue = maximalPointsList.get(i);
                int j = i - 1;
                while(j >= 0 && maximalPointsList.get(j).compareTo(currentValue) > 0 ){
                    maximalPointsList.set(j + 1, maximalPointsList.get(j));
                    j--;
                }
                maximalPointsList.set(j + 1, currentValue);

            }

            // draw maximal line
            for(int i = 0; i< maximalPointsList.size()-1; i++) {
                Line line = new Line();
                line.setStartX(maximalPointsList.get(i).getX());
                line.setStartY(maximalPointsList.get(i).getY());
                line.setEndX(maximalPointsList.get(i).getX());
                line.setEndY(maximalPointsList.get(i+1).getY());
                line.setStroke(Color.RED);
                this.getChildren().add(line);
            }

            System.out.println("Sorted Maximal Points: ");
            System.out.println(maximalPointsList);

            // change maxmal points to green
            for(Point point : maximalPointsList) {
                Circle circle = new Circle();
                circle.setCenterX((double) point.getX());
                circle.setCenterY((double) point.getY());
                circle.setRadius(5.0f);
                circle.setFill(Color.GREEN);
                this.getChildren().add(circle);
            }


            // change color for the largest and smallest circles
            //largestCircle.setFill(Color.RED);
            //largestCircle.setRadius(10.0f);
            smallestCircle.setFill(Color.BLUE);
            smallestCircle.setRadius(10.0f);
            //this.getChildren().add(largestCircle);
            this.getChildren().add(smallestCircle);

        }

        private Circle mouseClickHandler(double x, double y){
            Circle circle = new Circle();
            circle.setCenterX(x);
            circle.setCenterY(y);
            circle.setFill(Color.BLACK);
            circle.setRadius(5.0f);
            return circle;
        }

        private void mouseClickHandler(int index){

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

                customPaneClass.getChildren().add(customPaneClass.mouseClickHandler(e.getX(), e.getY()));

                // Add point to pointsList

                //????

                // Call method to recalculate maximal line since point was added

           //     maximalLinePoints = calculateListOfPointsOnMaximalLine( pointsList );
          //      drawMaximalLine( maximalLinePoints );

            } // Right-click: Remove a circle
            else if (e.getButton() == MouseButton.SECONDARY) {
                // Use a reverse loop to safely remove items while iterating
                for (int i = customPaneClass.getChildren().size() - 1; i >= 0; i--) {
                    if (customPaneClass.getChildren().get(i) instanceof Circle) {
                        //can replace this with a pattern variable
                        Circle circle = (Circle) customPaneClass.getChildren().get(i);
                        if (circle.contains(e.getX(), e.getY())) {
                            customPaneClass.getChildren().remove(i);
                            break;  // stop after removing one circle
                        }
                    }
                }
                // Remove point from pointsList

                //???

                // Call method to recalculate maximal line since point was removed

      //          maximalLinePoints = calculateListOfPointsOnMaximalLine( pointsList );
      //          drawMaximalLine( maximalLinePoints );

            }
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