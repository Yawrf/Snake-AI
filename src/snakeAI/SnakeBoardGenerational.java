/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakeAI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import snakeAI.Snake.Direction;
import static snakeAI.Snake.Direction.*;

/**
 *
 * @author rewil
 */
public class SnakeBoardGenerational extends SnakeBoard {
    
    private long highScore = 0;
    
    private HashMap<SnakeObject, SnakeFood> snakes;
    private SnakeObject leadingSnake;
    
    /**
     * Creates new SnakeBoard (with new Snake and new Food)
     * @param width Maximum X coordinate before the Snake should die
     * @param height Maximum Y coordinate before the Snake should die
     * @param foodSeed  What seed the SnakeFood should be generated with
     * @param warpWalls If true, the s will warp from one side to the other upon contact with a wall
     * @param snakeCount Number of Snakes to generate
     */
    public SnakeBoardGenerational(int width, int height, long foodSeed, boolean warpWalls, int snakeCount) {
        super(width, height, foodSeed, warpWalls);
        
        snakes = new HashMap<>();
        for(int i = 0; i < snakeCount; ++i) {
            snakes.put(new SnakeObject(RIGHT, 3, 0, 0, squareSize, squareBuffer, warpWalls, width, height), new SnakeFood(width, height, scale, foodSeed));
        }
    } 
    
    /**
     * Creates new SnakeBoard (with new Snake and new Food)
     * @param width Maximum X coordinate before the Snake should die
     * @param height Maximum Y coordinate before the Snake should die
     * @param warpWalls If true, the s will warp from one side to the other upon contact with a wall
     * @param snakeCount Number of Snakes to generate
     */
    public SnakeBoardGenerational(int width, int height, boolean warpWalls, int snakeCount) {
        super(width, height, warpWalls);
        
        snakes = new HashMap<>();
        for(int i = 0; i < snakeCount; ++i) {
            snakes.put(new SnakeObject(RIGHT, 3, 0, 0, squareSize, squareBuffer, warpWalls, width, height), new SnakeFood(width, height, scale));
        }
    } 
    
    /**
     * Creates a new generation of snakes, preserving the highest scoring one,
     * mutating the rest of the top half (score-wise), and randomly generating
     * the rest
     */
    public void newGeneration() {
        SnakeObject[] so = snakes.keySet().toArray(new SnakeObject[snakes.keySet().size()]);
        SnakeFood[] sf = snakes.values().toArray(new SnakeFood[snakes.values().size()]);
        snakes = new HashMap<>();
        sortByScore(so);
        so[0].resetScore();
        snakes.put(so[0], sf[0]); //Yes I'm aware these don't match, it's irrelevant
        for(int i = 1; i < so.length; ++i) {
            SnakeObject newS = new SnakeObject(RIGHT, 3, 0, 0, squareSize, squareBuffer, so[i].getWarpWalls(), width, height);
            if(i < so.length / 2) {
                newS.setSnakeBrainAdaptor(so[i].getSnakeBrainAdaptor());
            }
            newS.modifyBrain();
            snakes.put(newS, sf[i]);
        }
    }
    
    /**
     * Sorts a provided array of SnakeObjects by their score from highest to lowest
     * @param so
     * @return 
     */
    private SnakeObject[] sortByScore(SnakeObject[] so) {
        ArrayList<SnakeObject> sorting = new ArrayList<>();
        sorting.add(so[0]);
        for(int i = 1; i < so.length; ++i) {
            boolean put = false;
            for(int j = 0; j < sorting.size(); ++j) {
                if(!put) {
                    if(so[i].getScore() > sorting.get(j).getScore()) {
                        sorting.add(j, so[i]);
                        put = true;
                    }
                }
            }
            if(!put) {
                sorting.add(so[i]);
            }
        }
        for(int i = 0; i < so.length; ++i) {
            so[i] = sorting.remove(0);
        }
        
        System.out.println(so[0].getScore() + " - " + so[so.length/2].getScore() + " - " + so[so.length - 1].getScore());
        
        return so;
    }
    
    /**
     * Checks if any Snake is at the same spot as its f and consumes it if so
     * @return Always true
     */
    @Override
    protected boolean checkFood() {
        for(SnakeObject s : snakes.keySet()) {
            SnakeFood food = snakes.get(s);
            if(s.atSpot(food.getX(), food.getY())) {
                s.setAte();
                eatFood(s, food);
            }
        }
        return true;
    }
    
    /**
     * Does all actions for the Snake eating the f
     * Assumes Snake is at proper location or that location doesn't matter
     * @param s Snake to eat the f
     * @param f Food to be eaten
     */
    private void eatFood(SnakeObject s, SnakeFood f) {
        s.addScore(foodScore);
        s.addJoint();
        f.getNew(s);
    }
    
    /**
     * Returns if the Snake has eaten since the last time resetAte() was called
     * @param s Snake to check status of
     * @return 
     */
    public boolean getAte(SnakeObject s) {
        return s.getAte();
    }
    
    /**
     * Resets the ate boolean of a specified s damnto false
     * @param s 
     */
    public void resetAte(SnakeObject s) {
        s.resetAte();
    }
    
    /** 
     * Does Nothing :D
     * @param d Direction of new movement
     */
    @Override
    @Deprecated
    public void setDirection(Direction d) {
        
    }
    
    /**
     * Runs all the main processes for all the snakes:
     * Calls the Snakes' step() providing them with the current x,y of the f
     * Tells the Snake to move
     * Increases the Snakes' Scores
     * Checks which snake is in the lead and assigns them to leadingSnake
     */
    @Override
    public void step() {
        long temp = 0;
        SnakeObject high = null;
        checkFood();
        for(SnakeObject s : snakes.keySet()) {
            SnakeFood f = snakes.get(s);
            s.step(f.getX(), f.getY());
            s.move();
            s.addScore(stepScore);
            if(s.getScore() > temp) {
                temp = s.getScore();
                high = s;
            }
            if(s.getScore() > highScore) {
                highScore = s.getScore();
            }
        }
        leadingSnake = high;
    }
    
    /**
     * Returns the set of SnakeObjects in use
     * @return 
     */
    public Set<SnakeObject> getSnakes() {
        return snakes.keySet();
    }
    
    /**
     * Returns the current Snake with the highest score
     * @return 
     */
    public SnakeObject getLeadingSnake() {
        SnakeObject output = leadingSnake;
        if(leadingSnake == null) {
            output = snakes.keySet().toArray(new SnakeObject[snakes.keySet().size()])[0];
        }
        return output;
    }
    
    /**
     * Returns the SnakeFoods in use
     * @return 
     */
    public Collection<SnakeFood> getFoods() {
        return snakes.values();
    }
    
    /**
     * Returns the SnakeFood for a given SnakeObject
     * @param s
     * @return 
     */
    public SnakeFood getFood(SnakeObject s) {
        return snakes.get(s);
    }
    
    /**
     * Returns the current HighScore among all snakes and all generations
     * @return 
     */
    public long getHighScore() {
        return highScore;
    }
    
}
