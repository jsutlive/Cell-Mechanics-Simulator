package Physics.Rigidbodies;

import GUI.IColor;
import Utilities.Geometry.Vector2f;
import Utilities.Math.CustomMath;

import java.awt.*;

/**
 * Edge: A container of two nodes which acts as a mechanism to apply physics forces between them.
 * This class is abstract, different physics characteristics can be configured depending on the type of edge
 * selected.
 */
public abstract class Edge implements IRigidbody, IColor
{
    protected Color color;
    protected Node[] nodes = new Node[2];
    protected float initialLength;
    protected float elasticConstant;
    protected Vector2f normal;
    public boolean hasActed;

    public float getElasticConstant()
    {
        return elasticConstant;
    }

    /**
     * Add a force vector equally to both nodes. Will result in the nodes acting in parallel
     * @param forceVector
     */
    @Override
    public void AddForceVector(Vector2f forceVector) {
        for(Node node: nodes) node.AddForceVector(forceVector);
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
        float dist = Vector2f.dist(a, b);
        return CustomMath.round(dist, 3);
    }

    public float getXUnit()
    {
        Vector2f[] pos = getPositions();
        return (pos[1].x - pos[0].x)/getLength();
    }

    public float getYUnit()
    {
        Vector2f[] pos = getPositions();
        return (pos[1].y - pos[0].y)/getLength();
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

    public void constrict(float constant, float ratio)
    {
        float forceMag = constant * (this.getLength() - (ratio * this.initialLength));
        Vector2f forceVector = new Vector2f(getXUnit(), getYUnit());
        forceVector.mul(forceMag);
        nodes[0].AddForceVector(forceVector);

        forceVector.mul(-1);
        nodes[1].AddForceVector(forceVector);
    }

    public Vector2f getCenter(){
        Vector2f posA = getPositions()[0];
        Vector2f posB = getPositions()[1];

        float x = CustomMath.avg(posA.x, posB.x);
        float y = CustomMath.avg(posA.y, posB.y);
        return new Vector2f(x,y);
    }

    public boolean contains(Node n){
        for(Node node: nodes){
            if (node == n) return true;
        }
        return false;
    }

}
