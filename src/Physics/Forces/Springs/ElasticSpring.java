package Physics.Forces.Springs;

import Physics.Bodies.Cell.ApicalEdge;
import Physics.Bodies.Cell.Cell;
import Physics.Bodies.Cell.CellGroup;
import Physics.Bodies.Edge;

import java.util.HashSet;

public class ElasticSpring extends Spring
{
    public static ElasticSpring configureNew(float constant, float ratio)
    {
        ElasticSpring spring = new ElasticSpring();
        spring.constant = constant;
        spring.ratio = ratio;
        spring.listeners = new HashSet<>();
        return spring;
    }

    private ElasticSpring(){}

    @Override
    public void start() {

    }

    @Override
    public void attach(Cell cell)
    {
        for(Edge edge: cell.getAllEdges()) listeners.add(edge);
    }


}
