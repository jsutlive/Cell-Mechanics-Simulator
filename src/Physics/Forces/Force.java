package Physics.Forces;


import Model.Cell;
import Physics.Rigidbodies.BasicEdge;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector2f;
import Utilities.Math.CustomMath;

import java.util.ArrayList;
import java.util.List;


public class Force
{
    public static List<Vector2f[]> debugDraw = new ArrayList<>();
    /**
     * Constrict or expand an edge based on a spring-mass system (where mass is assumed to be 1).
     * based on a constant "constant" and constriction ratio "ratio". After force calculation, a unit vector is used
     * to obtain the x and y components of the edge to determine direction. The force is applied to the first node in
     * the edge and -1 times the force is applied to the second node.
     *
     * @param edge edge to be constricted
     * @param constant constant to determine the stiffness of the spring
     * @param ratio constant to determine the equilibrium point driving the force.
     */
    public static void constrict(Edge edge, float constant, float ratio)
    {
        Node[] nodes = edge.getNodes();
        Vector2f forceVector = calculateSpringForce(edge, constant, ratio);

        // Apply force to node 0
        nodes[0].AddForceVector(forceVector);

        // Apply an equal and opposite force to node 1
        forceVector.mul(-1);
        nodes[1].AddForceVector(forceVector);
    }

    public static void constrict(Edge edge, float constant, float ratio, int id)
    {
        Node[] nodes = edge.getNodes();
        Vector2f forceVector = calculateSpringForce(edge, constant, ratio);
        System.out.println("FORCE(" + id + "): " +
                forceVector.x + ", " +
                forceVector.y + "::LENGTH: " +
                edge.getLength() + "::INITIAL LENGTH: "
                + edge.getInitialLength());



        // Apply force to node 0
        nodes[0].AddForceVector(forceVector);

        // Apply an equal and opposite force to node 1
        forceVector.mul(-1);
        nodes[1].AddForceVector(forceVector);
    }

    public static void halfConstrict(Edge edge, float constant, float ratio){
        Node[] nodes = edge.getNodes();
        Vector2f forceVector = calculateSpringForce(edge, constant, ratio);

        // Apply force to node 0
        nodes[0].AddForceVector(forceVector);
    }

    public static Vector2f calculateSpringForce(Edge edge, float constant, float ratio) {
        // Hooke's law calculation to get force magnitude
        float forceMag = constant * (edge.getLength() - (ratio * edge.getInitialLength()));
        // Find a unit vector showing x and y components of this edge
        Vector2f forceVector = new Vector2f(edge.getXUnit(), edge.getYUnit());
        // Multiply magnitude * unit vector
        forceVector.mul(forceMag);
        forceVector = limitForceFromLength(edge, forceVector);
        return forceVector;
    }

    /**
     *Constrict or expand an edge based on a spring-mass system (where mass is assumed to be 1).
     * based on a constant "constant"
     *
     * @param edge to be constricted
     * @param constant
     */
    public static void elastic(Edge edge, float constant){
        constrict(edge, constant, 1f);
    }

    public static void elastic(Edge edge){constrict(edge, edge.getElasticConstant(), 1f);}

    public static void restore(Cell cell, float constant){
        // get cell characteristics
        Vector2f center = cell.getCenter();
        float currentArea = cell.getArea();
        float restingArea = cell.getRestingArea();

        // calculate force magnitude
        float forceMagnitude = constant * (currentArea - restingArea);

        //for every node, get unit vector, multiply times magnitude, apply force
        for(Node node: cell.getNodes()) {
            //get unit vector
            Vector2f restoringForce = Vector2f.unit(center, node.getPosition());
            //multiply times magnitude
            restoringForce.mul(-forceMagnitude);
            // add force
            node.AddForceVector(restoringForce);
            //Vector2f endPosition = node.getPosition();
            //endPosition.add(restoringForce);

        }
    }

    public static Vector2f dampen(Vector2f force, float threshold, float ratio)
    {
        if(ratio > 1f) throw new IllegalArgumentException("Ratio must not exceed 1");
        float dampenedComponent = 0f;
        if (threshold < force.mag()) {
            dampenedComponent = (force.mag() - threshold) * ratio;
        }
        float dampenedMagnitude = dampenedComponent + threshold;
        Vector2f dampenedForce = Vector2f.unit(force);
        dampenedForce.mul(dampenedMagnitude);
        return dampenedForce;
    }

    public static Vector2f limitForceFromLength(Edge edge, Vector2f force){
        float limit = (edge.getLength()/2) * 0.999f;
        if(force.mag() < limit) return force;
        else
        {
            Vector2f limitedForce = Vector2f.unit(force);
            limitedForce.mul(limit);
            return limitedForce;
        }
    }
    public static Vector2f GetLennardJonesLikeForce(float ljConstant, Edge edge, Node n, String type) {
        Vector2f pointOnEdge = CustomMath.pointSlope(n, edge);
        Vector2f forceVector = Vector2f.unit(pointOnEdge, n.getPosition());
        Edge temp;
        Node t = new Node(pointOnEdge);
        temp = new BasicEdge(n, t);
        float forceMagnitude = calculateLJForceMagnitude(temp, ljConstant, type);
        forceVector.mul(forceMagnitude);
        return forceVector;
    }

    private static float calculateLJForceMagnitude(Edge e, float constant, String type)
    {
        switch(type)
        {
            case "simple":
                return Math.min(4f, constant * (1f/ (float)Math.pow(e.getLength(), 3)));
            case "ljLike":
                return calculateLennardJonesLikeForce(constant, e.getLength());
            case "lennardJones":
                return calculateLennardJonesForce(constant, e.getLength());
            default:
                break;
        }
        throw new IllegalArgumentException("Type must be 'simple', 'ljlike', or 'lennardjones'");
    }

    private static float calculateLennardJonesLikeForce(float constant, float distance)
    {
        return 0;
    }

    private static float calculateLennardJonesForce(float constant, float distance)
    {
        float epsilon = 0f;
        float sigma = 0f;

        return 0;
    }
}
