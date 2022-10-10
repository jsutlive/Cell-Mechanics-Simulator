package Morphogenesis.Rigidbodies.Edges;

import Renderer.Graphics.IColor;
import Morphogenesis.Rigidbodies.IRigidbody;
import Morphogenesis.Rigidbodies.Nodes.Node;
import Utilities.Geometry.Vector.Vector;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Math.CustomMath;

import java.awt.*;

/**
 * Edge: A container of two nodes which acts as a mechanism to apply physics forces between them.
 * This class is abstract, different physics characteristics can be configured depending on the type of edge
 * selected.
 */
public abstract class Edge implements IRigidbody, IColor
{
    protected transient Color color;
    protected transient Node[] nodes = new Node[2];
    //order of nodes in cell list
    protected int[] nodesReference = new int[2];
    protected transient float initialLength;
    protected float elasticConstant;
    protected Vector2f normal;
    public transient boolean isNull = true;

    public float getElasticConstant()
    {
        return elasticConstant;
    }
    public void setElasticConstant(float constant){ elasticConstant = constant;}

    /**
     * Add a force vector equally to both nodes. Will result in the nodes acting in parallel
     * @param forceVector vector containing force information
     */
    @Override
    public void addForceVector(Vector forceVector) {
        for(Node node: nodes) node.addForceVector(forceVector);
    }

    @Override
    public void addForceVector(String name, Vector forceVector) {
        for(Node node: nodes) node.addForceVector(name, forceVector);
    }

    public void addConstrictionForceVector(String type, Vector force){
        Vector forceNeg = force.neg();
        nodes[0].addForceVector(type, force);
        nodes[1].addForceVector(type, forceNeg);
    }

    public void addConstrictionForceVector(Vector force){
        Vector forceNeg = force.neg();
        nodes[0].addForceVector("", force);
        nodes[1].addForceVector("", forceNeg);
    }

    public void setNodesReference(int a, int b){
        nodesReference[0] = a;
        nodesReference[1] = b;
    }

    protected void MakeNewEdge(Node a, Node b)
    {
        nodes[0] = a;
        nodes[1] = b;
        initialLength = getLength();
    }

    public void flip()
    {
        Node a = nodes[0];
        nodes[0] = nodes[1];
        nodes[1] = a;
    }

    /**
     * Returns the positions of the nodes that make up the edge. Throws a null pointer exception if one
     * or more nodes has a null value for its position.
     * @return an array (2) of Vector2 (floating point) objects each containing position data.
     */
    public Vector2f[] getPositions(){
        Vector2f[] positions = new Vector2f[2];
        try {
            positions[0] = (Vector2f) nodes[0].getPosition().copy();
            positions[1] = (Vector2f) nodes[1].getPosition().copy();
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

    public float getInitialLength(){
        return initialLength;
    }

    @Override
    public void moveTo(Vector newPosition) {
        for(Node node: nodes){
            node.moveTo(newPosition);
        }
    }

    @Override
    public void move() {

    }

    /**
     * Get the current length of the edge, rounded to three decimal places
     * @return edge length to three decimal places
     */
    public float getLength()
    {
        Vector a = nodes[0].getPosition();
        Vector b = nodes[1].getPosition();
        float dist = a.distanceTo(b);
        return CustomMath.round(dist, 5);
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

    /**
     * Find the center of the edge as a floating point x,y vector2
     * @return Vector2 (float) with x,y of the center
     */
    public Vector2f getCenter(){
        Vector2f posA = getPositions()[0];
        Vector2f posB = getPositions()[1];

        float x = CustomMath.avg(posA.x, posB.x);
        float y = CustomMath.avg(posA.y, posB.y);
        return new Vector2f(x,y);
    }

    /**
     * Check to see if a given node is one of the two nodes making up the edge
     * @param n the node we wish to see is part of the edge
     * @return true if n is part of the edge
     */
    public boolean contains(Node n){
        for(Node node: nodes){
            if(n.getPosition().equals (node.getPosition())) return true;
        }
        return false;
    }

    public abstract Edge clone();

    public void setNodes(Node[] nodes){
        this.nodes = nodes;
    }

    public void setNodes(Node a, Node b)
    {
        nodes[0] = a;
        nodes[1] = b;
    }

    public void mirrorAcrossXAxis(){
        for(Node n: getNodes()){
            n.mirrorAcrossXAxis();
        }
    }
    public void mirrorAcrossYAxis(){
        for(Node n: getNodes()){
            n.mirrorAcrossYAxis();
        }
    }

    @Override
    public Color getColor(){
        return color;
    }

    @Override
    public void setColor(Color color){
        for(Node node: nodes){
            if(node instanceof IColor){
                ((IColor)node).setColor(color);
            }
        }
        this.color = color;
    }

}