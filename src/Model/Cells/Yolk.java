package Model.Cells;

import Engine.States.State;
import Physics.Forces.Force;
import Physics.Rigidbodies.BasalEdge;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;

import java.util.ArrayList;
import java.util.List;

public class Yolk extends Cell {

    public static Cell build(List<Node> yolkNodes) throws IllegalAccessException, InstantiationException {
        Cell yolk = (Cell) State.create(Yolk.class);
        yolk.setNodes(yolkNodes);
        int yolkCount = yolkNodes.size();
        for(int i = 0; i < yolkCount/2-1; i++){
            yolk.edges.add(new BasalEdge(yolkNodes.get(i),yolkNodes.get(i+1)));
        }
        for(int i = yolkCount - 1; i > yolkCount/2; i--){
            yolk.edges.add(new BasalEdge(yolkNodes.get(i),yolkNodes.get(i-1)));
        }
        yolk.edges.add(new BasalEdge(yolkNodes.get(yolkCount/2),yolkNodes.get(0)));
        yolk.edges.add(new BasalEdge(yolkNodes.get(yolkCount/2-1),yolkNodes.get(yolkCount-1)));
//        yolk.edges.add(new BasalEdge(yolkNodes.get(41),yolkNodes.get(40)));
        return yolk;
    }


    @Override
    public void awake() {
        this.osmosisConstant = .005f;
    }

    @Override
    public void start() {
    }

    @Override
    public void update() {
        System.out.println(getRestingArea());
        calculateYolkRestoringForce();
    }

    @Override
    public void run() {

    }

    private void calculateYolkRestoringForce() {
        Force.restore(this, osmosisConstant);
    }
}
