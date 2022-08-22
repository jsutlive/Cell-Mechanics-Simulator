package Model.Cells;

import Engine.States.State;
import Physics.Forces.Force;
import Physics.Rigidbodies.Node;

import java.util.List;

public class Yolk extends Cell {

    public static Cell build(List<Node> yolkNodes) throws IllegalAccessException, InstantiationException {
        Cell yolk = (Cell) State.create(Yolk.class);
        //yolk.setNodes(yolkNodes);
        return yolk;
    }


    @Override
    public void awake() {
        //this.osmosisConstant = .004f;
    }

    @Override
    public void start() {
        //getRestingArea();
    }

    @Override
    public void update() {
        //calculateYolkRestoringForce();
    }



    /*private void calculateYolkRestoringForce() {
        Force.restore(this, osmosisConstant);
    }*/
}
