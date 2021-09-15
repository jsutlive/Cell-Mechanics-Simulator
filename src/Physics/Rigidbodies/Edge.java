package Physics.Rigidbodies;

import GUI.IColor;
import Utilities.Geometry.Vector2f;
import Utilities.Math.CustomMath;

import java.awt.*;

public abstract class Edge implements IRigidbody, IColor
{
    protected Color color;
    protected Node[] nodes = new Node[2];
    protected float initialLength;

    @Override
    public void AddForceVector(Vector2f forceVector) {

    }

    protected void MakeNewEdge(Node a, Node b)
    {
        nodes[0] = a;
        nodes[1] = b;
        initialLength = getLength();
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

    public float getLength()
    {
        Vector2f a = nodes[0].getPosition();
        Vector2f b = nodes[1].getPosition();
        float dist = (float)Math.hypot(b.x -a.x, b.y - a.y);
        return CustomMath.round(dist, 3);
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        for(Node node: nodes){
            if(node instanceof IColor){
                ((IColor)node).setColor(color);
            }
        }
        this.color = color;
    }
}
