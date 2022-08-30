package Model.Cells;

import Engine.States.State;
import Physics.Forces.Force;
import Physics.Rigidbodies.BasalEdge;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Yolk extends Cell {

    public static Cell build(List<Node> yolkNodes) throws IllegalAccessException, InstantiationException {
        Cell yolk = (Cell) State.create(Yolk.class);
        int yolkCount = yolkNodes.size();
        List<Node> firstHalf = yolkNodes.subList(0, yolkCount/2);
        List<Node> secondHalf = yolkNodes.subList(yolkCount/2, yolkCount);
        Collections.reverse(secondHalf);
        yolkNodes = firstHalf;
        yolkNodes.addAll(secondHalf);
        yolk.setNodes(yolkNodes);
        for(int i = 0; i < yolkCount; i++){
            yolk.edges.add(new BasalEdge(yolkNodes.get((i+1) % yolkCount),yolkNodes.get(i)));
        }
        return yolk;
    }


    @Override
    public void awake() {
        this.osmosisConstant = -.0001f;
    }

    @Override
    public void start() {
        restingArea = getArea();
        setNodePositions();
    }

    @Override
    public void update() {
        setNodePositions();
        calculateYolkRestoringForce();
    }

    @Override
    public void run() {

    }

    private void calculateYolkRestoringForce() {
        Force.restore(this, osmosisConstant);
    }
}
