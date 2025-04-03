package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;


import javafx.stage.Stage;

import java.io.IOException;

// my imports
import javafx.scene.shape.Circle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseButton;
import java.util.ArrayList;

public class HelloApplication extends Application {

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
        public PaneClass(ArrayList<Point> initialPoints) {
            for(Point point : initialPoints) {
                Circle circle = new Circle();
                circle.setCenterX((double) point.x());
                circle.setCenterY((double) point.y());
                circle.setRadius(5.0f);
                circle.setFill(Color.BLACK);
                this.getChildren().add(circle);
            }
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        stage.setTitle("Project 2");


        // Create an ArrayList of type Point's
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(200.0, 300.0));
        points.add(new Point(250.0, 300.0));
        points.add(new Point(330.0, 270.0));
        points.add(new Point(150.0, 380.0));
        points.add(new Point(126.0, 172.0));
        points.add(new Point(397.0, 379.0));
        points.add(new Point(334.0, 441.0));
        points.add(new Point(53.0, 228.0));
        points.add(new Point(89.0, 433.0));
        points.add(new Point(182.0, 215.0));
        points.add(new Point(251.0, 414.0));


        Pane pane = new Pane();
        PaneClass customPaneClass = new PaneClass(points);



        // Mouse events for adding circles on left click and removing circles on right click

        pane.setOnMouseClicked(e -> {
            // Add a circle on left click
            if (e.getButton() == MouseButton.PRIMARY) {
                Circle circle = new Circle();
                circle.setCenterX(e.getX());
                circle.setCenterY(e.getY());
                circle.setRadius(5.0f);
                circle.setFill(Color.BLACK);
                pane.getChildren().add(circle);
            } // Right-click: Remove a circle
            else if (e.getButton() == MouseButton.SECONDARY) {
                // Use a reverse loop to safely remove items while iterating
                for (int i = pane.getChildren().size() - 1; i >= 0; i--) {
                    if (pane.getChildren().get(i) instanceof Circle) {
                        //can replace this with a pattern variable
                        Circle circle = (Circle) pane.getChildren().get(i);
                        if (circle.contains(e.getX(), e.getY())) {
                            pane.getChildren().remove(i);
                            break;  // stop after removing one circle
                        }
                    }
                }
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