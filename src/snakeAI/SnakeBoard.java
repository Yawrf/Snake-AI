/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakeAI;

import snakeAI.Snake.Direction;
import static snakeAI.Snake.Direction.*;

/**
 *
 * @author rewil
 */
public class SnakeBoard {
    
    protected int width;
    protected int height;
    
    protected final int scale = 10;
    protected final int squareSize = 9;
    protected final int squareBuffer = 1;
    
    protected final int foodScore = 100;
    protected final int stepScore = 1;
    
    protected boolean ate = false;
    
    protected SnakeObject snake;
    protected SnakeFood food;
    
    /**
     * Creates new SnakeBoard (with new Snake and new Food)
     * @param width Maximum X coordinate before the Snake should die
     * @param height Maximum Y coordinate before the Snake should die
     * @param foodSeed  What seed the SnakeFood should be generated with
     * @param warpWalls If true, the snake will warp from one side to the other upon contact with a wall
     */
    public SnakeBoard(int width, int height, long foodSeed, boolean warpWalls) {
        this.width = width;
        this.height = height;
        snake = new SnakeObject(RIGHT, 3, 0, 0, squareSize, squareBuffer, warpWalls, width, height);
        food = new SnakeFood(width, height, scale, foodSeed);
    } 
    
    /**
     * Creates new SnakeBoard (with new Snake and new Food)
     * @param width Maximum X coordinate before the Snake should die
     * @param height Maximum Y coordinate before the Snake should die
     * @param warpWalls If true, the snake will warp from one side to the other upon contact with a wall
     */
    public SnakeBoard(int width, int height, boolean warpWalls) {
        this.width = width;
        this.height = height;
        snake = new SnakeObject(RIGHT, 3, 0, 0, squareSize, squareBuffer, warpWalls, width, height);
        food = new SnakeFood(width, height, scale, 1537231739434l);
    } 
    
    /**
     * Checks if the Snake is at the same spot as the food
     * @return True if Snake is at food, else False
     */
    protected boolean checkFood() {
        if(snake.atSpot(food.getX(), food.getY())) {
            return true;
        }
        return false;
    }
    
    /**
     * - Not for generational use -
     * Does all actions for the Snake eating the food
     * * Assumes Snake is at proper location or that location doesn't matter
     */
    protected void eatFood() {
        snake.addScore(foodScore);
        snake.addJoint();
        food.getNew(snake);
        ate = true;
    }
    
    /**
     * - Not for generational use -
     * Returns if the Snake has eaten since the last time resetAte() was called
     * @return 
     */
    public boolean getAte() {
        return ate;
    }
    
    /**
     * - Not for generational use -
     * Resets the ate boolean to false
     */
    public void resetAte() {
        ate = false;
    }
    
    /**
     * Tells the Snake to move in a new direction
     * @param d Direction of new movement
     */
    public void setDirection(Direction d) {
        snake.setDirection(d);
    }
    
    /**
     * Runs all the main processes:
     * * Calls the Snake's step() providing it with the current x,y of the food
     * * Tells the Snake to move
     * * Has the Snake eat the Food if it's at the correct location
     * * Increases the Snake's Score
     */
    public void step() {
        snake.step(food.getX(), food.getY());
        snake.move();
        if(checkFood()) {
            eatFood();
        }
        snake.addScore(stepScore);
    }
    
    /**
     * - Not for generational use -
     * Returns the SnakeObject in use
     * @return 
     */
    public SnakeObject getSnake() {
        return snake;
    }
    
    /**
     * - Not for generational use -
     * Returns the SnakeFood in use
     * @return 
     */
    public SnakeFood getFood() {
        return food;
    }
    
    /**
     * Returns the size of the SnakeJoints
     * @return 
     */
    public int getSize() {
        return squareSize;
    }
    
    /**
     * Returns the size of the space between SnakeJoints
     * @return 
     */
    public int getBuffer() {
        return squareBuffer;
    }
    
    /**
     * Returns the size of the grid used by the Snake
     * @return 
     */
    public int getScale() {
        return scale;
    }
    
}