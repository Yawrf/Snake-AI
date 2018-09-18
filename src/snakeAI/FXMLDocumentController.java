/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakeAI;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javax.swing.Timer;
import snakeAI.Snake.Direction;
import static snakeAI.Snake.Direction.*;

/**
 *
 * @author rewil
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Canvas board;
    
//    @FXML
//    private TextField score;
    
    private SnakeBoard b;
    
    private Timer timer;
    private int baseSpeed = 100; //ms
    private int speed = 0;
    private int speedMod = 5;
    
    private boolean buttonPressed = false;
    
    private int count = 1;
    
    private boolean acidMode = false;
    private Color snakeColor = Color.BLACK;
    private Color bgColor = Color.WHITE;
    private Color foodColor = Color.RED;
    private Color scoreColor = Color.BLACK;
    private boolean scramble = true;
    
    private void step() {
        
        if(acidMode && scramble) {
            Random rand = new Random();
            snakeColor = Color.color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble());
            bgColor = Color.color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble());
            foodColor = Color.color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble());
            scoreColor = Color.color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble());
            scramble = false;
        }
        
        //Make New Step
        if(!b.getSnake().isDead()) {
            b.step();
        }
        
        //Check if Ate Food
        if(b.getAte()) {
            speedUp();
            scramble = true;
            b.resetAte();
        }
        
        //Erase Last Step
        GraphicsContext gc = board.getGraphicsContext2D();
        gc.setFill(bgColor);
        gc.fillRect(0, 0, board.getWidth(), board.getHeight());
        
        
        //Draw Current Step
        for(SnakeJoint sj : b.getSnake().getJoints()) {
            drawSquare(gc, sj.getX(), sj.getY(), sj.size, snakeColor);
        }
        drawSquare(gc, b.getFood().getX(), b.getFood().getY(), b.getSize(), foodColor);
        
        //Update Score
        String scorePrefix = "Score: ";
        int scoreNum = (int)b.getSnake().getScore();
        gc.setFill(scoreColor);
        gc.fillText(scorePrefix + scoreNum, 10, board.getHeight() - 10);
        
        //Allow New Input
        buttonPressed = false;
        
        //Stop if dead
        if(b.getSnake().isDead()) {
            timer.stop();
            System.out.println(b.getSnake().getScore() + " - " + b.getFood().getSeed());
            gc.fillText("Died", 500, 500);
            setBoard();
//            timer.setDelay(speed);
        }
    }
    
    private void drawSquare(GraphicsContext gc, int x, int y, int size, Color c) {
        gc.setFill(c);
        gc.fillRect(x, y, size, size);
    }
    
    @FXML
    private void changeDirection(KeyEvent e) {
//        System.out.println("Ping");
        if(!buttonPressed) {
            Direction moving = b.getSnake().getDirection();
            switch(e.getCode()) {
                case UP: {
                    if(moving != UP && moving != DOWN) {
                        moving = UP; 
                        buttonPressed = true;
                    }
                }
                    break;
                case RIGHT: {
                    if(moving != RIGHT && moving != LEFT) {
                    moving = RIGHT; 
                    buttonPressed = true;
                    }
                }
                    break; 
                case DOWN: {
                    if(moving != DOWN && moving != UP) {
                    moving = DOWN; 
                    buttonPressed = true;
                    }
                }
                    break;
                case LEFT: {
                    if(moving != LEFT && moving != RIGHT) {
                    moving = LEFT; 
                    buttonPressed = true;
                    }
                }
                    break;
                case R: {
                    setBoard();
                    if(!timer.isRunning()) {
                        timer.start();
                    }
                }
                    break;
                case Q: System.exit(0);
            }
            b.setDirection(moving);
        }
    }
    
    public void speedUp() {
        speed -= speedMod;
        if(speed <= 0) {
            speed = 1;
        }
        timer.setDelay(speed);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        setTimer(speed);
        setBoard();
        
    }
    
    private void setBoard() {
        b = new SnakeBoard((int)board.getWidth(), (int)board.getHeight());
        speed = baseSpeed;
//        timer.setDelay(speed);
    }
    
    private void setTimer(int time) {
        timer = new Timer(time, e -> step());
        timer.setCoalesce(true);
        timer.setRepeats(true);
        timer.setInitialDelay(0);
        timer.start();
    }
    
}
