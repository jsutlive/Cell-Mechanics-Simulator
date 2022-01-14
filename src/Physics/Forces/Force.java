package Physics.Forces;


import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector2f;


public class Force
{
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
}
