package Physics.Rigidbodies;

import GUI.IColor;
import Utilities.Geometry.Vector2f;

import java.awt.*;

public class Edge implements IRigidbody, IColor
{
    private Color color;
    private Node[] nodes;
    public Edge()
    {

    }
    @Override
    public void AddForceVector(Vector2f forceVector) {

    }

    public Vector2f[] getPositions(){
        Vector2f[] positions = new Vector2f[2];
        try {
            positions[0] = nodes[0].getPosition();
            positions[1] = nodes[1].getPosition();
        }catch(NullPointerException e)
        {
            System.out.println("One or both nodes in edge is null, cannot determine position.");
            return null;
        }
        return positions;
    }

    public Node[] getNodes(){
        return nodes;
    }

    @Override
    public void MoveTo(Vector2f newPosition) {

    }

    @Override
    public void Move() {

    }

    @Override
    public void getColor() {

    }

    @Override
    public void setColor(Color color) {

    }
}
