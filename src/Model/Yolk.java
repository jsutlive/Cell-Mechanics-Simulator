package Model;

import Engine.Object.MonoBehavior;
import Model.Cells.Cell;
import Physics.Rigidbodies.BasalEdge;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector2f;
import Utilities.Math.CustomMath;
import Utilities.Math.Gauss;

import java.util.List;

public class Yolk extends MonoBehavior {
    private float yolkConstant = 0.35f;
    public void setYolkConstant(float yolkConstant) {this.yolkConstant = yolkConstant;}
    private float restingArea;
    private float currentArea;
    private List<Cell> boundingCells;
    public float getArea() {
        currentArea = Gauss.nShoelace(yolkNodes);
        return currentArea;
    }

    private List<Node> yolkNodes;
    public void build(List<Cell> boundingCells, List<Node> yolkNodes){
        this.yolkNodes = yolkNodes;
        this.boundingCells = boundingCells;
        restingArea = Gauss.nShoelace(yolkNodes);
        currentArea = restingArea;
    }


    @Override
    public void awake() throws InstantiationException, IllegalAccessException {

    }

    @Override
    public void update() {
        calculateYolkRestoringForce();
    }

    @Override
    public void run() {

    }

    private void calculateYolkRestoringForce() {
        getArea();
        int multiplier = getMultiplier();
        for (Cell cell : boundingCells) {
            for (Edge edge : cell.getEdges()) {
                if (edge instanceof BasalEdge) {
                    Vector2f force = CustomMath.normal(edge);
                    force.mul(multiplier * yolkConstant);
                    edge.AddForceVector(force);
                }
            }
        }
    }

    private int getMultiplier() {
        int multiplier = 0;
        if(currentArea < restingArea) {
            multiplier = 1;
        }else if (currentArea > restingArea){
            multiplier = -1;
        }
        return multiplier;
    }
}
