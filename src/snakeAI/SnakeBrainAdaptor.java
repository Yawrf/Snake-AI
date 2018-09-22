/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakeAI;

import neuralnet.Brain;
import neuralnet.InputNode;
import neuralnet.Node;
import neuralnet.OutputNode;
import neuralnet.brainAdaptor;
import snakeAI.Snake.Direction;
import static snakeAI.Snake.Direction.*;

/**
 *
 * @author rewil
 */
public class SnakeBrainAdaptor implements brainAdaptor {
    
    private final Brain b;
    private Direction move = RIGHT;
    
    public SnakeBrainAdaptor() {
        int hiddenLayers = 3;
        int nodesPerLayer =  inputNodes.length > outputNodes.length ? inputNodes.length : outputNodes.length;
        b = new Brain(hiddenLayers, nodesPerLayer);
        
        for(int i = 0; i < inputNodes.length; ++i) {
            Node n = b.getLayers()[0].getNodes()[i];
            if(n instanceof InputNode) {
                InputNode in = (InputNode) n;
                in.setName(inputNodes[i]);
            }
        }
        for(int i = 0; i < outputNodes.length; ++i) {
            Node n = b.getLayers()[b.getLayers().length - 1].getNodes()[i];
            if(n instanceof OutputNode) {
                OutputNode on = (OutputNode) n;
                on.setName(outputNodes[i]);
            }
        }
        for(int i = 0; i < nodesPerLayer; ++i) {
            Node n = b.getLayers()[b.getLayers().length - 1].getNodes()[i];
            if(n instanceof OutputNode) {
                OutputNode on = (OutputNode) n;
                on.setParent(this);
            }
        }
    }
    
    public SnakeBrainAdaptor(SnakeBrainAdaptor sba) {
        b = sba.getBrain();
    }
    
    private final String[] inputNodes = new String[] {
        "upSafe",
        "rightSafe",
        "downSafe",
        "leftSafe",
        "xFoodDist",
        "yFoodDist",
        "xPos",
        "yPos",
        "maxX",
        "maxY"
    };
    private final String[] outputNodes = new String[] {
      "upMove",
      "rightMove",
      "downMove",
      "leftMove"
    };

    public void send(boolean up, boolean right, boolean down, boolean left, int xFood, int yFood, int xPos, int yPos, int maxX, int maxY) {
        for(Node n : b.getLayers()[0].getNodes()) {
            if(n instanceof InputNode) {
                InputNode in = (InputNode) n;
                switch(in.getName()) {
                    case "upSafe": in.receive(up ? 1d : -1d);
                        break;
                    case "rightSafe": in.receive(right ? 1d : -1d);
                        break;
                    case "downSafe": in.receive(down ? 1d : -1d);
                        break;
                    case "leftSafe": in.receive(left ? 1d : -1d);
                        break;
                    case "xFoodDiset": in.receive(xFood);
                        break;
                    case "yFoodDiset": in.receive(yFood);
                        break;
                    case "xPos": in.receive(xPos);
                        break;
                    case "yPos": in.receive(yPos);
                        break;
                    case "maxX": in.receive(maxX);
                        break;
                    case "maxY": in.receive(maxY);
                        break;
                }
            }
        }
        b.think();
    }
    
    @Override
    public void receive(String name, double value) {
        double cutoff = .1d;
        switch(name) {
            case "upMove": move = value >= cutoff ? UP : move;
                break;
            case "rightMove": move = value >= cutoff ? RIGHT : move;
                break;
            case "downMove": move = value >= cutoff ? DOWN : move;
                break;
            case "leftMove": move = value >= cutoff ? LEFT : move;
                break;
        }
//        System.out.println(name + " - " + value);
    }
    
    public Direction getMove() {
        return move;
    }
    
    public Brain getBrain() {
        return b;
    }
    
    public void modifyBrain() {
        b.modifyConnections();
    }
    
}
