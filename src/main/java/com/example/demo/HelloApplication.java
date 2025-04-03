package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;


import javafx.stage.Stage;

import java.io.*;

// my imports
import javafx.scene.shape.Circle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseButton;
import java.util.ArrayList;
import java.io.FileReader;

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

    public record Point(double x, double y) implements Comparable<Point> {

        @Override
            public int compareTo(Point other) {
                return Double.compare(this.x, other.x);
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

        public PaneClass(ArrayList<Point> initialPoints) {

            pointsList = initialPoints;

            for(Point point : pointsList) {
                Circle circle = new Circle();
                circle.setCenterX((double) point.x());
                circle.setCenterY((double) point.y());
                circle.setRadius(5.0f);
                circle.setFill(Color.BLACK);
                this.getChildren().add(circle);
            }
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

    private void drawMaximalLine( ArrayList<Point> maximalLinePoints )
    {
        // list will include every point necessary to draw the entire maximal line,
        // including those necessary to start and end the line, and the corresponding
        // points to step between existing points

        for ( int i = 0; i < maximalLinePoints.size()-1; i++ )
        {
            // Draw line between this point and the next point

          //  Line line = new Line(100, 10, 10, 110);

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
        System.out.println(paneTotalPointList);
        Scene scene = new Scene(customPaneClass, 1000, 700);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}