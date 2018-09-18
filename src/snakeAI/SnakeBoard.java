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
    
    private int width;
    private int height;
    
    private final int scale = 10;
    private final int squareSize = 9;
    private final int squareBuffer = 1;
    
    private final int foodScore = 100;
    private final int stepScore = 1;
    
    private boolean ate = false;
    
    private SnakeObject snake;
    private SnakeFood food;
    
    public SnakeBoard(int width, int height, long foodSeed) {
        this.width = width;
        this.height = height;
        snake = new SnakeObject(RIGHT, 3, 0, 0, squareSize, squareBuffer, width, height);
        food = new SnakeFood(width, height, scale, foodSeed);
    } 
    
    public SnakeBoard(int width, int height) {
        this.width = width;
        this.height = height;
        snake = new SnakeObject(RIGHT, 3, 0, 0, squareSize, squareBuffer, width, height);
        food = new SnakeFood(width, height, scale, 1537231739434l);
    } 
    
    private boolean checkFood() {
        if(snake.atSpot(food.getX(), food.getY())) {
            return true;
        }
        return false;
    }
    
    private void eatFood() {
        snake.addScore(foodScore);
        snake.addJoint();
        food.getNew();
        ate = true;
    }
    
    public boolean getAte() {
        return ate;
    }
    
    public void resetAte() {
        ate = false;
    }
    
    public void setDirection(Direction d) {
        snake.setDirection(d);
    }
    
    public void step() {
        snake.step(food.getX(), food.getY());
        snake.move();
        if(checkFood()) {
            eatFood();
        }
        snake.addScore(stepScore);
        if(snake.isDead()) {
        }
    }
    
    public SnakeObject getSnake() {
        return snake;
    }
    
    public SnakeFood getFood() {
        return food;
    }
    
    public int getSize() {
        return squareSize;
    }
    
    public int getBuffer() {
        return squareBuffer;
    }
    
    public int getScale() {
        return scale;
    }
    
}
