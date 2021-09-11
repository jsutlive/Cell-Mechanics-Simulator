package Physics.Forces.Springs;

import Physics.Bodies.Cell.ApicalEdge;
import Physics.Bodies.Cell.Cell;
import Physics.Bodies.Cell.CellEdge;
import Physics.Bodies.Cell.CellGroup;
import Physics.Bodies.Edge;
import Physics.Bodies.PhysicsBody;
import Physics.Bodies.Vertex;
import Utilities.Geometry.Vector2f;

import java.util.HashSet;


public class SimpleApicalSpring extends Spring{


    public static SimpleApicalSpring configureNew(float constant, float ratio)
    {
        SimpleApicalSpring spring = new SimpleApicalSpring();
        spring.constant = constant;
        spring.ratio = ratio;
        spring.listeners = new HashSet<>();
        return spring;
    }

    @Override
    public void start() {

    }

    @Override
    public void calculateForce(PhysicsBody body)
    {

        int sign =1;
        CellEdge edge = (CellEdge) body;
        int id = edge.getCell().getID();
        if(id< 2) sign *= -1;
        float f = sign * getForceMagnitude(edge);
        //Vector2f force = new Vector2f(f);
        Vector2f force = new Vector2f(f * edge.getXLength()/edge.getLength(), f* edge.getYLength()/edge.getLength());
        applyForceToVertices(edge, force);
    }

    private void applyForceToVertices(CellEdge edge, Vector2f force) {
        Vertex[] vertices = edge.getVertices();

        vertices[1].addForce(this, force);
        // Flip sign of force so vertices move opposite to each other
        force.mul(-1);
        vertices[0].addForce(this, force);
    }

    @Override
    public void attach(Cell cell)
    {
        for(Edge edge: cell.getAllEdges())
        {
            if(edge instanceof ApicalEdge)
            {
                listeners.add(edge);
            }
        }
    }

    @Override
    public void attach(CellGroup cellGroup)
    {
        for(Cell cell: cellGroup.getCells())
        {
            attach(cell);
        }
    }
}
