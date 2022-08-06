package Model.Cells;

import Engine.States.State;
import Physics.Forces.Force;
import Physics.Rigidbodies.Node;

import java.util.List;

public class Yolk extends Cell {

    public static Cell build(List<Node> yolkNodes) throws IllegalAccessException, InstantiationException {
        Cell yolk = (Cell) State.create(Yolk.class);
        yolk.setNodes(yolkNodes);
        return yolk;
    }


    @Override
    public void awake() {
        this.osmosisConstant = .003f;
    }

    @Override
    public void start() {
        getRestingArea();
    }

    @Override
    public void update() {
        calculateYolkRestoringForce();
    }

    @Override
    public void run() {

    }

    private void calculateYolkRestoringForce() {
        Force.restore(this, osmosisConstant);
    }
}