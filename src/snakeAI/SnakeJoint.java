/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakeAI;

import snakeAI.Snake.Direction;

/**
 *
 * @author rewil
 */
public class SnakeJoint {
    
    private int xPos;
    private int yPos;
    private SnakeObject parent;
    public final int size;
    public final int buffer;
    
    public SnakeJoint(int x, int y, int squareSize, int bufferSize) {
        xPos = x;
        yPos = y;
        size = squareSize;
        buffer = bufferSize;
    }
    
    public SnakeJoint(SnakeJoint sj) {
        xPos = sj.getX();
        yPos = sj.getY();
        size = sj.size;
        buffer = sj.buffer;
    }
    
    public int getX() {
        return xPos;
    }
    
    public int getY() {
        return yPos;
    }
    
    public int[] getPos() {
        return new int[]{xPos, yPos};
    }
    
    public void move(Direction d) {
        int shift = size + buffer;
        switch(d) {
            case UP: yPos -= shift;
                break;
            case RIGHT: xPos += shift;
                break;
            case DOWN: yPos += shift;
                break;
            case LEFT: xPos -= shift;
        }
    }
    
    public void move(SnakeJoint sj) {
        xPos = sj.getX();
        yPos = sj.getY();
    }
    
    @Override
    public String toString() {
        String output = "";
        output += "[" + xPos + ", " + yPos + "]";
        return output;
    }
    
}
