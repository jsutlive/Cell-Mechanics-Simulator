package Physics.Forces.Springs;

import Physics.Bodies.Cell.CellEdge;
import Physics.Bodies.PhysicsBody;
import Physics.Bodies.Vertex;
import Physics.Forces.Force;
import Utilities.Geometry.Vector2f;

public abstract class Spring extends Force
{
   float constant;
   float length;
   float ratio;

    @Override
    public void calculateForce(PhysicsBody body) {
        CellEdge edge = (CellEdge) body;
        float f = constant * (length - (ratio * edge.initialLength));
        Vector2f force = new Vector2f(f * edge.getXLength()/length, f* edge.getYLength()/length);
        applyForceToVertices(edge, force);
        /*System.out.println(edge.getCell().getID() + "::constant::" + constant);
        System.out.println(edge.getCell().getID() + "::LengthX::" + edge.getXLength());
        System.out.println(edge.getCell().getID() + "::LengthY::" + edge.getYLength());
        System.out.println(edge.getCell().getID() + "::force::" + f);
        System.out.println("-----/////-----");*/
    }

    private void applyForceToVertices(CellEdge edge, Vector2f force) {
        Vertex[] vertices = edge.getVertices();
        vertices[1].addForce(this, force);
        // Flip sign of force so vertices move opposite to each other
        force.mul(-1);
        vertices[0].addForce(this, force);
    }


}
