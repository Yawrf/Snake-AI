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
    
    public SnakeFood(int xBound, int yBound, int scale) {
        long seed = System.currentTimeMillis();
        rand = new Random(seed);
        this.seed = seed;
        this.scale = scale;
        maxX = xBound;
        maxY = yBound;
        setCoords();
    }
    
    public SnakeFood(int xBound, int yBound, int scale, long seed) {
        rand = new Random(seed);
        this.seed = seed;
        this.scale = scale;
        maxX = xBound;
        maxY = yBound;
        setCoords();
    }
    
    public int getX() {
        return xPos;
    }
    
    public int getY() {
        return yPos;
    }
    
    public Long getSeed() {
        return seed;
    }
    
    public void getNew() {
        setCoords();
    }
    
    private void setCoords() {
        xPos = rand.nextInt(maxX / scale) * scale;
        yPos = rand.nextInt(maxY / scale) * scale;
    }
    
}
