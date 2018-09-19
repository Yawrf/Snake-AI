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
    
    /**
     * Creates a new SnakeJoint
     * @param x - x Position
     * @param y - y Position
     * @param squareSize - Size of Joint
     * @param bufferSize - Size between Joints
     */
    public SnakeJoint(int x, int y, int squareSize, int bufferSize) {
        xPos = x;
        yPos = y;
        size = squareSize;
        buffer = bufferSize;
    }
    
    /**
     * Creates a SnakeJoint which is duplicating another SnakeJoint
     * @param sj SnakeJoint to duplicate
     */
    public SnakeJoint(SnakeJoint sj) {
        xPos = sj.getX();
        yPos = sj.getY();
        size = sj.size;
        buffer = sj.buffer;
    }
    
    /**
     * Returns x Position
     * @return 
     */
    public int getX() {
        return xPos;
    }
    
    /**
     * Returns y Position
     * @return 
     */
    public int getY() {
        return yPos;
    }
    
    /**
     * Returns array containing x Position, then y Position
     * @return 
     */
    public int[] getPos() {
        return new int[]{xPos, yPos};
    }
    
    /**
     * Sets the position of the Joint to a given position
     * @param coords Coordinates in the format: [xPos, yPos]
     */
    public void setPos(int[] coords) {
        xPos = coords[0];
        yPos = coords[1];
    }
    
    /**
     * Moves the Joint one position in the given Direction
     * @param d Direction to move
     */
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
    
    /**
     * Moves the Joint onto the same position as another SnakeJoint
     * @param sj SnakeJoint to move to position of
     */
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
