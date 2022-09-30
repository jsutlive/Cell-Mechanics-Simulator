package Model.Components.Physics.Spring;

import Model.Components.Physics.Force;
import Physics.Rigidbodies.Edges.Edge;
import Physics.Rigidbodies.Nodes.Node;
import Utilities.Geometry.Vector.Vector;
import Utilities.Geometry.Vector.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class SpringForce extends Force {
    // The resting length may not be the initial length of the spring. What percentage
    // of the initial length is the target?
    protected float constant;
    protected float targetLengthRatio;
    public List<Edge> edges = new ArrayList<>();

    public void setTargetLengthRatio(float ratio){
        targetLengthRatio = ratio;
    }

    /**
     * Return Hooke's law calculation of spring force to extend or contract an edge.
     * Hooke's law: y = -k * (l - leq);
     * This implementation: y = k * (l - (l0 * targetLength));
     * where k is the spring constant, l , leq, and l0 are length, resting length, andf initial length, respectively.
     * @param edge edge to be constricted/ extended
     * @param constant the "k" value in Hooke's law
     */
    protected Vector calculateSpringForce(Edge edge, float constant) {
        // Hooke's law calculation to get force magnitude
        float forceMag = constant * (edge.getLength() - (targetLengthRatio * edge.getInitialLength()));
        // Find a unit vector showing x and y components of this edge
        Vector2f unit = new Vector2f(edge.getXUnit(), edge.getYUnit());
        // Multiply magnitude * unit vector
        return unit.mul(forceMag);
    }

    @Override
    /*
      Use this update call if spring constants between all edges are equal,
      override in subclasses if this is not the case.
     */
    public void update() {
        Vector force;
        for(Edge edge: edges){
            force = calculateSpringForce(edge, constant);
            edge.addConstrictionForceVector(getClass().getSimpleName(), force);
        }
    }
}
