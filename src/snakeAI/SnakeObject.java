/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakeAI;

import java.util.ArrayList;
import java.util.Random;
import snakeAI.Snake.Direction;
import static snakeAI.Snake.Direction.*;

/**
 *
 * @author rewil
 */
public class SnakeObject {
    
    private Direction moving;
    private ArrayList<SnakeJoint> joints;
    private int size;
    private int buffer;
    private boolean dead = false;
    
    private long score = 0;
    
    /**
     * Creates a new SnakeObject
     * @param startMove - Starting Direction of Movement
     * @param startJoints - Number of SnakeJoints to start with
     * @param startX - Initial x Position
     * @param startY - Initial y Position
     * @param squareSize - Size of SnakeJoints
     * @param bufferSize - Size of space between SnakeJoints
     * @param xBorder - Maximum x Snake can be before dying
     * @param yBorder - Maximum y Snake can be before dying
     */
    public SnakeObject(Direction startMove, int startJoints, int startX, int startY, int squareSize, int bufferSize, int xBorder, int yBorder) {
        moving = startMove;
        size = squareSize;
        buffer = bufferSize;
        joints = new ArrayList<>();
        for(int i = 0; i < startJoints; ++i) {
            joints.add(new SnakeJoint(startX, startY, size, buffer));
        }
        maxX = xBorder;
        maxY = yBorder;
    }
    
    /**
     * Creates a new SnakeObject with default SnakeJoint size of 9 with default space between joints of 1
     * @param startMove - Starting Direction of Movement
     * @param startJoints - Number of SnakeJoints to start with
     * @param startX - Initial x Position
     * @param startY - Initial y Position
     * @param xBorder - Maximum x Snake can be before dying
     * @param yBorder - Maximum y Snake can be before dying
     */
    public SnakeObject(Direction startMove, int startJoints, int startX, int startY, int xBorder, int yBorder) {
        moving = startMove;
        size = 9;
        buffer = 1;
        joints = new ArrayList<>();
        for(int i = 0; i < startJoints; ++i) {
            joints.add(new SnakeJoint(startX, startY, size, buffer));
        }
        maxX = xBorder;
        maxY = yBorder;
    }
    
    /**
     * Progresses all SnakeJoints except the leading one (head) one position ahead in line, and the head moves one position in the moving direction
     */
    public void move() {
        if(!dead) {
            for(int i = joints.size() - 1; i > 0; --i) {
                SnakeJoint j1 = joints.get(i);
                SnakeJoint j2 = joints.get(i-1);
                if(j1.getX() != j2.getX() || j1.getY() != j2.getY()) {
                    joints.get(i).move(j2);
                }
            }
            joints.get(0).move(moving);
            checkCollision();
        }
    }
   
    /**
     * Checks if the head (leading SnakeJoint) is colliding with any SnakeJoints in body or any walls
     */
    public void checkCollision() {
        int headX = joints.get(0).getX();
        int headY = joints.get(0).getY();
        if(headX >= maxX || headX < 0 || headY >= maxY || headY < 0) {
            dead = true;
        }
        for(int i = 1; i < joints.size(); ++i) {
            if(!dead) {
                int[] temp = joints.get(i).getPos();
                if(temp[0] == headX && temp[1] == headY) {
                    dead = true;
                }
            }
        }
//        System.out.println(dead + " - " + headX + ", " + headY);
    }
    
    /**
     * Returns if head (leading SnakeJoint) is in given position
     * @param x X Position to check
     * @param y Y Position to check
     * @return True if Snake's head is in given position, else false
     */
    public boolean atSpot(int x, int y) {
        return joints.get(0).getX() == x && joints.get(0).getY() == y;
    }
    
    /**
     * Adds a SnakeJoint to the Snake in same position as current last Joint
     */
    public void addJoint() {
        joints.add(new SnakeJoint(joints.get(joints.size() - 1)));
    }
   
    /**
     * Increases the Snake's score by given amount
     * @param add Amount to add to Snake's score
     */
    public void addScore(long add) {
        if(!dead) {
            score += add;
        }
    }
    
    /**
     * Returns the Snake's current Score
     * @return 
     */
    public long getScore() {
        return score;
    }
    
    /**
     * Returns the ArrayList of the Snake's Joints
     * @return 
     */
    public ArrayList<SnakeJoint> getJoints() {
        return joints;
    }
    
    /**
     * Returns the direction the Snake is moving
     * @return 
     */
    public Direction getDirection() {
        return moving;
    }
    
    /**
     * Sets the direction the Snake is moving
     * @param d 
     */
    public void setDirection(Direction d) {
        moving = d;
    }
    
    /**
     * Returns if the Snake is Dead
     * @return 
     */
    public boolean isDead() {
        return dead;
    }
    
    /**
     * Returns the size of the SnakeJoints
     * @return 
     */
    public int getSize() {
        return size;
    }
    
    /**
     * Returns the size of the space between the SpaceJoints
     * @return 
     */
    public int getBuffer() {
        return buffer;
    }
    
    @Override
    public String toString() {
        String output = "";
        
        output += moving + ", ";
        output += joints.size() + " segments, ";
        output += joints;
        
        return output;
    }
    
// -------------------------------------------------------------------------------------------------------------------
    // All AI related parts are below this line
    
    private boolean upSafe = true;
    private boolean rightSafe = true;
    private boolean downSafe = true;
    private boolean leftSafe = true;
    
    private int foodDistX = 0;
    private int foodDistY = 0;
    
    private final int maxX;
    private final int maxY;
    
    /**
     * Runs all the main Processes:
     * * Checks if each direction is safe to move in
     * * Calculates the distance to the food
     * * Runs the dumbAI
     * @param foodX
     * @param foodY 
     */
    public void step(int foodX, int foodY) {
        if(!dead) {
            checkSafe();
            calcFoodDist(foodX, foodY);
            dumbAI();
        }
    }
    
    /**
     * Calculates which direction is both safe and advantageous to move in.
     * If no direction is both safe and advantageous, it moves in a random safe direction
     * If no direction is safe to move in, it does not run
     */
    private void dumbAI() {
        if(upSafe || rightSafe || downSafe || leftSafe) {
            if(foodDistX > 0 && rightSafe) {
                moving = RIGHT;
    //            System.out.println("Food Right");
            } else if(foodDistX < 0 && leftSafe) {
                moving = LEFT;
    //            System.out.println("Food Left");
            } else if(foodDistY < 0 && upSafe) {
                moving = UP;
    //            System.out.println("Food Up");
            } else if(foodDistY > 0 && downSafe) {
                moving = DOWN;
    //            System.out.println("Food Down");
            } else {
                switch(moving) {
                    case UP: {
                        if(!upSafe) {
                            moving = randomDirection();
                        }} break;
                    case RIGHT: {
                        if(!rightSafe) {
                            moving = randomDirection();
                        }} break;
                    case LEFT: {
                        if(!leftSafe) {
                            moving = randomDirection();
                        }} break;
                    case DOWN: {
                        if(!downSafe) {
                            moving = randomDirection();
                        }
                    }
                }
    //            System.out.println("Random Move");
            }
        }
    }
    
    /**
     * Generates a random direction that's safe to move in
     * @return 
     */
    private Direction randomDirection() {
        Random rand = new Random();
        Direction d = UP;
        boolean done = false;
        while(!done) {
            switch(rand.nextInt(4)) {
                case 0: {if(upSafe) {d = UP; done = true;}}
                    break;
                case 1: {if(rightSafe) {d = RIGHT; done = true;}}
                    break;
                case 2: {if(downSafe) {d = DOWN; done = true;}}
                    break;
                case 3: {if(leftSafe) {d = LEFT; done = true;}}
            }
        }
        return d;
    }
    
    /**
     * Calculates the x distance and y distance from the head (leading SnakeJoint) to the food
     * @param foodX
     * @param foodY 
     */
    private void calcFoodDist(int foodX, int foodY) {
        int headX = joints.get(0).getX();
        int headY = joints.get(0).getY();
        
        foodDistX = foodX - headX;
        foodDistY = foodY - headY;
        
//          System.out.println(foodDistX + ", " + foodDistY);
    }
    
    /**
     * Checks each direction from the head (leading SnakeJoint) to see if:
     * * It is occupied by another SnakeJoint
     * * It is a wall
     * and sets the Safe booleans accordingly
     */
    private void checkSafe() {
        int headX = joints.get(0).getX();
        int headY = joints.get(0).getY();
        
        upSafe = true;
        rightSafe = true;
        downSafe = true;
        leftSafe = true;
        
        for(SnakeJoint sj : joints) {
            switch(headX - sj.getX()) {
                case 0: {
                    switch (headY - sj.getY()) {
                        case 10: {
                            upSafe = false;
                        } break;
                        case -10: {
                            downSafe = false;
                        }
                    }
                } break;
                case 10: {
                    switch(headY - sj.getY()) {
                        case 0: leftSafe = false;
                    }
                } break;
                case -10: {
                    switch(headY - sj.getY()) {
                        case 0: rightSafe = false;
                    }
                }
            }
        }
        
        if(headX == 0) {
            leftSafe = false;
        }
        if(headX == maxX - (size + buffer)) {
            rightSafe = false;
        }
        if(headY == 0) {
            upSafe = false;
        }
        if(headY == maxY - (size + buffer)) {
            downSafe = false;
        }
//        
//        System.out.println(headX + ", " + headY);
//        System.out.println("\t" + upSafe + '\n' + leftSafe + '\t' + '\t' + rightSafe + '\n' + '\t' + downSafe);
//        System.out.println("-------------------------");
        
    }
    
}
