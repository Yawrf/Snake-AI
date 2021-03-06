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
    
    private SnakeBoard b;
    
    private Timer timer;
    private final int baseSpeed = 5; // base ms the timer starts at
    private int speed = 0; 
    private final int speedMod = 5; // ms faster the game gets each time the snake eats
    
    private boolean buttonPressed = false;
    
    private final int snakeCount = 750;
    private int generation = 1;
    
    private final boolean acidMode = false; //Makes all colours scramble each time food is collected
    private Color snakeColor = Color.BLACK;
    private Color bgColor = Color.WHITE;
    private Color foodColor = Color.RED;
    private Color scoreColor = Color.BLACK;
    private boolean scramble = true;
    
    private final boolean warpWalls = false; //Makes snake warp from one side of board to other when hitting a wall
    
    private final boolean generational = true; //True for generations, false for ui/dumbAI
    
    /**
     * Runs the Generational or Normal Step functions
     */
    private void step() {
        if(generational) {
            stepGenerational();
        } else {
            stepNormal();
        }
    }
    
    /** 
     * Runs all the main processes:
     * * Runs SnakeBoardGenerational's step() function
     * * Redraws the Canvas
     * * Updates the Score, Generation, and HighScore
     * * Creates new generation if all Snakes are dead
     */
    private void stepGenerational() {
        SnakeBoardGenerational b;
        if(this.b instanceof SnakeBoardGenerational) {
            b = (SnakeBoardGenerational) this.b;
            
            //Make New Step
            b.step();

            //Erase Last Step
            GraphicsContext gc = board.getGraphicsContext2D();
            gc.setFill(bgColor);
            gc.fillRect(0, 0, board.getWidth(), board.getHeight());

            //Draw Current Step of leading Snake
            for(SnakeJoint sj : b.getLeadingSnake().getJoints()) {
                drawSquare(gc, sj.getX(), sj.getY(), sj.size, snakeColor);
            }
            drawSquare(gc, b.getFood(b.getLeadingSnake()).getX(), b.getFood(b.getLeadingSnake()).getY(), b.getSize(), foodColor);

            //Update Score shown (Leading Snake's)
            String scorePrefix = "Score: ";
            int scoreNum = (int)b.getLeadingSnake().getScore();
            gc.setFill(scoreColor);
            gc.fillText(scorePrefix + scoreNum, 10, board.getHeight() - 10);

            //Update Generation count
            String genPrefix = "Generation: ";
            gc.fillText(genPrefix + generation, 10, board.getHeight() - 30);

            //Update High Score
            String hsPrefix = "High Score: ";
            gc.fillText(hsPrefix + b.getHighScore(), 10, board.getHeight() - 50);

            //Check if all dead
            boolean allDead = true;
            for(SnakeObject s : b.getSnakes()) {
                if(!s.isDead()) {
                    allDead = false;
                }
            }
            if(allDead) {
                System.out.print(">> Generation: " + generation + " - ");
                b.newGeneration();
                generation++;
                System.out.println(b.getHighScore());
            }
        }
    }
    
    /**
     * Runs all the main processes:
     * * Runs SnakeBoard's step() function
     * * Checks if the food was eaten and responds accordingly
     * * Redraws the Canvas
     * * Updates the Score
     * * Allows a new Input
     * * Ends the Game if the Snake is Dead
     */
    private void stepNormal() {
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
        
//        //Check if Ate Food
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
            System.out.println(b.getSnake().getScore());
//            gc.fillText("Died", 500, 500); //For manual reset
            setBoard(); //For automatic reset
            timer.setDelay(speed); //For automatic reset
        }
    }
    
    /**
     * Draws a rectangle on the Canvas
     * @param gc
     * @param x - X Coordinate
     * @param y - Y Coordinate
     * @param size - Side length
     * @param c  - Fill Colour
     */
    private void drawSquare(GraphicsContext gc, int x, int y, int size, Color c) {
        gc.setFill(c);
        gc.fillRect(x, y, size, size);
    }
    
    /**
     * Interprets KeyPresses to tell the board which direction to go
     * - Only runs if a command has not already been entered since last step()
     * @param e 
     */
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
                    if(!generational) {
                        setBoard();
                        if(!timer.isRunning()) {
                            timer.start();
                        }
                    }
                }
                    break;
                case Q: System.exit(0);
            }
            b.setDirection(moving);
        }
    }
    
    /**
     * Increases the speed at which the game is called. 
     * If the speed would drop below 1, it resets it to 1
     * * Decreases speed by speedMod
     */
    public void speedUp() {
        speed -= speedMod;
        if(speed <= 0) {
            speed = 1;
        }
        timer.setDelay(speed);
    }
    
    /**
     * Makes a new SnakeBoard (with a new Snake)
     * Resets the speed to baseSpeed
     */
    private void setBoard() {
        
        if(generational) {
            b = new SnakeBoardGenerational((int)board.getWidth(), (int)board.getHeight(), warpWalls, snakeCount);
        } else {
            b = new SnakeBoard((int)board.getWidth(), (int)board.getHeight(), warpWalls);
        }
        speed = baseSpeed;
    }
    
    /**
     * Creates a new Timer
     * @param time - Ms between each firing
     */
    private void setTimer(int time) {
        timer = new Timer(time, e -> step());
        timer.setCoalesce(true);
        timer.setRepeats(true);
        timer.setInitialDelay(0);
        timer.start();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        speed = baseSpeed;
        setTimer(speed);
        setBoard();
        
    }
}
