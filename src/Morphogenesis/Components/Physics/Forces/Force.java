package Morphogenesis.Components.Physics.Forces;


import Morphogenesis.Rigidbodies.Edges.BasicEdge;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Geometry;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Math.CustomMath;


public class Force
{
    public static Vector2f GetLennardJonesLikeForce(float ljConstant, Edge edge, Node2D n, LJForceType type) {
        Vector2f pointOnEdge = CustomMath.pointSlope(n, edge);
        Vector2f forceVector;
        if(Float.isNaN(pointOnEdge.x) || Float.isNaN(pointOnEdge.y))
        {
            System.err.println("POINT ON EDGE NULL");
        }
        if(!Geometry.lineSegmentContainsPoint(pointOnEdge, edge.getPositions())) {
            forceVector = Vector2f.unit(pointOnEdge, n.getPosition());
        }
        else
        {
            Vector2f edgeNode0 = edge.getPositions()[0];
            Vector2f edgeNode1 = edge.getPositions()[1];
            if(Vector2f.dist(pointOnEdge, edgeNode0) < Vector2f.dist(pointOnEdge, edgeNode1)) {
                forceVector = Vector2f.unit(pointOnEdge, edgeNode0);
            }
            else
            {
                forceVector = Vector2f.unit(pointOnEdge, edgeNode1);
            }
        }
        Edge temp;
        Node2D t = new Node2D(pointOnEdge);
        temp = new BasicEdge(n, t);
        float forceMagnitude = calculateLJForceMagnitude(temp, ljConstant, type);
        if(Float.isNaN(forceMagnitude))
        {
            System.err.println("FORCE MAGNITUDE NULL");
        }
        forceVector.mul(forceMagnitude);

        return forceVector;
    }

    private static float calculateLJForceMagnitude(Edge e, float constant, LJForceType type)
    {

        if (type == LJForceType.simple) {
            return calculateSimpleCubicRegressionForce(e, constant);
        }
        else if (type ==  LJForceType.kang2021) {
            return calculateLennardJonesLikeForce(constant, e.getLength());
        }
        else if  (type == LJForceType.lennardJones) {
            return calculateLennardJonesForce(constant, e.getLength());
        }

        throw new IllegalArgumentException("Type must be 'simple', 'ljlike', or 'lennardjones'");
    }

    private static float calculateSimpleCubicRegressionForce(Edge e, float constant)
    {
        float dampener = 1e-15f;
        return dampener * constant * (1f/ (float)Math.pow(e.getLength(), 3));
    }

    /**
     * Calculate force based on equation 5 in Kang, et al 2021 (https://doi.org/10/1016/j.isci.2021.103252)
     * @param constant parameter to control force magnitude
     * @param distance determines distance at which the force begins to act on other nodes
     * @return magnitude of the lennard jones-like force
     */
    private static float calculateLennardJonesLikeForce(float constant, float distance)
    {
        float lJConstantConversionFactor = 5e35f;
        float newConstant = constant/lJConstantConversionFactor;
        float maxDistance = 0.3f;
        int p = 6;
        int q = 3;
            float exponentTerm = (float)(Math.pow(maxDistance/distance, p) - (2 * Math.pow(maxDistance/distance, q)));
            return newConstant * exponentTerm * (1/ CustomMath.sq(distance));
    }

    private static float calculateLennardJonesForce(float epsilon, float distance)
    {
        float sigma = 4e-6f;
        float A = 4* epsilon *(float)Math.pow(sigma, 12);
        float B = 4* epsilon *(float)Math.pow(sigma, 6);
        float r13 = (float)Math.pow(distance, 13);
        float r7 = (float)Math.pow(distance, 7);

        return (-12 * (A/r13)) + (6 * (B/r7));
    }
}
