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
        float f = getForceMagnitude(edge);
        Vector2f force = new Vector2f(f * edge.getXLength()/length, f* edge.getYLength()/length);
        applyForceToVertices(edge, force);
    }

    public float getForceMagnitude(CellEdge edge) {
        float f = constant * (length - (ratio * edge.initialLength));
        return f;
    }

    private void applyForceToVertices(CellEdge edge, Vector2f force) {
        Vertex[] vertices = edge.getVertices();
        /*if(edge.getCell().getID() == 79)
        {
            force.mul(-1);
        }
        if(edge.getCell().getID() > 10) {
            force = new Vector2f(-1f, 1f);
        }else
        {
            force = new Vector2f(1f, 1f);
        }
        System.out.println("FORCE::" + force.x +"|||" + force.y);
        System.out.println("CELL ID:" + edge.getCell().getID());
        System.out.println("-----/////-----");*/
        vertices[1].addForce(this, force);
        // Flip sign of force so vertices move opposite to each other
        force.mul(-1);
        //vertices[0].addForce(this, force);
    }


}
