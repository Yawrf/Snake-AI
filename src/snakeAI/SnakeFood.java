/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakeAI;

import java.util.Random;

/**
 *
 * @author rewil
 */
public class SnakeFood {
    
    private int xPos;
    private int yPos;
    private final Long seed;
    private final Random rand;
    private final int scale;
    private final int maxX;
    private final int maxY;
    
    /**
     * Creates a new SnakeFood 
     * @param xBound - Maximum x the Food can be at
     * @param yBound - Maximum y the Food can be at
     * @param scale - The size of the grid the game is running on
     */
    public SnakeFood(int xBound, int yBound, int scale) {
        long seed = System.currentTimeMillis();
        rand = new Random(seed);
        this.seed = seed;
        this.scale = scale;
        maxX = xBound;
        maxY = yBound;
        setCoords();
    }
    
    /**
     * Creates a new SnakeFood with a customized seed
     * @param xBound Maximum x the Food can be at
     * @param yBound Maximum y the food can be at
     * @param scale The size of the grid the game is running on
     * @param seed The seed for the random location generator
     */
    public SnakeFood(int xBound, int yBound, int scale, long seed) {
        rand = new Random(seed);
        this.seed = seed;
        this.scale = scale;
        maxX = xBound;
        maxY = yBound;
        setCoords();
    }
    
    /**
     * Returns the x coordinate
     * @return 
     */
    public int getX() {
        return xPos;
    }
    
    /**
     * Returns the y coordinate
     * @return 
     */
    public int getY() {
        return yPos;
    }
    
    /**
     * Returns the seed of the random generator
     * @return 
     */
    public Long getSeed() {
        return seed;
    }
    
    /**
     * Sets new coordinates for the food
     */
    public void getNew(SnakeObject snake) {
        setCoords();
        checkOverlay(snake);
    }
    
    /**
     * Generates new coordinates
     */
    private void setCoords() {
        xPos = rand.nextInt(maxX / scale) * scale;
        yPos = rand.nextInt(maxY / scale) * scale;
    }
    
    /**
     * Checks if food is generated on top of snake,
     * Generates new food if true
     * @param snake 
     */
    private void checkOverlay(SnakeObject snake) {
        boolean overlay = false;
        for(SnakeJoint sj : snake.getJoints()) {
            if(sj.getX() == xPos && sj.getY() == yPos) {
                overlay = true;
            }
        }
        if(overlay && !snake.isDead()) {
            getNew(snake);
        }
    }
    
}
