/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakeAI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author rewil
 */
public class Snake extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        System.exit(0);
//        SnakeObject s = new SnakeObject(Direction.RIGHT, 3, 0, 0);
//        System.out.println(s);
//        s.move();
//        System.out.println(s);
//        s.move();
//        System.out.println(s);
//        s.setDirection(Direction.DOWN);
//        s.move();
//        System.out.println(s);
//        s.move();
//        System.out.println(s);
//        s.move();
//        System.out.println(s);
    }
    
    public static enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT;
    }
    
}