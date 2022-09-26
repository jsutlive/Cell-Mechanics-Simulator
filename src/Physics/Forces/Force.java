package Physics.Forces;


import Physics.Rigidbodies.Edges.BasicEdge;
import Physics.Rigidbodies.Edges.Edge;
import Physics.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Geometry;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Math.CustomMath;


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
        Node2D[] nodes = edge.getNodes();
        Vector2f forceVector = calculateSpringForce(edge, constant, ratio);

        // Apply force to node 0
        nodes[0].addForceVector(forceVector);

        // Apply an equal and opposite force to node 1
        forceVector.mul(-1);
        nodes[1].addForceVector(forceVector);
    }

    public static void constantConstrict(Edge edge, float constant, float ratio)
    {
        Node2D[] nodes = edge.getNodes();
        Vector2f forceVector = calculateConstrictionForce(edge, constant, ratio);
        // Apply force to node 0
        nodes[0].addForceVector(forceVector);

        // Apply an equal and opposite force to node 1
        forceVector.mul(-1);
        nodes[1].addForceVector(forceVector);
    }

    public static void constrict(Edge edge, float constant, float ratio, int id)
    {
        Node2D[] nodes = edge.getNodes();
        Vector2f forceVector = calculateSpringForce(edge, constant, ratio);
        System.out.println("FORCE(" + id + "): " +
                forceVector.x + ", " +
                forceVector.y + "::LENGTH: " +
                edge.getLength() + "::INITIAL LENGTH: "
                + edge.getInitialLength());

        // Apply force to node 0
        nodes[0].addForceVector(forceVector);

        // Apply an equal and opposite force to node 1
        forceVector.mul(-1);
        nodes[1].addForceVector(forceVector);
    }


    public static void halfConstrict(Edge edge, float constant, float ratio){
        Node2D[] nodes = edge.getNodes();
        Vector2f forceVector = calculateSpringForce(edge, constant, ratio);

        // Apply force to node 0
        nodes[0].addForceVector(forceVector);
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

    public static Vector2f calculateConstrictionForce(Edge edge, float constant, float ratio) {
        // Hooke's law calculation to get force magnitude
        float forceMag = Math.max(constant * Math.signum(edge.getLength() - (ratio * edge.getInitialLength())),0);
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
     * @param constant spring constant
     */
    public static void elastic(Edge edge, float constant){
        constrict(edge, constant, 1f);
    }

    public static void elastic(Edge edge){constrict(edge, edge.getElasticConstant(), 1f);}

    /*

    public static void restoreWithPerimeter(Cell cell, float constant){
        float forceMagnitude = constant * (cell.getArea() - cell.getRestingArea()) - 0.001f;

        List<Edge> edges = cell.getEdges();
        List<Vector2f> edgeVectors = new ArrayList<>();
        float perimeter = 0f;
        for(Edge e:edges){
            Node[] nodes = e.getNodes();
            Node node1 = nodes[0];
            Node node2 = nodes[1];
            Vector2f edgeVector = node2.getPosition().copy();
            edgeVector.sub(node1.getPosition());
            edgeVectors.add(edgeVector);
            perimeter += edgeVector.mag();
        }


        for(int i=0; i<edges.size();i++){
            Node[] nodes = edges.get(i).getNodes();
            Node node1 = nodes[0];
            Node node2 = nodes[1];

            Vector2f perpendicular = Vector2f.zero;
            perpendicular.x = edgeVectors.get(i).y;
            perpendicular.y = -edgeVectors.get(i).x;
            perpendicular.mul(-forceMagnitude * edgeVectors.get(i).mag() / perimeter);
            node1.AddForceVector(perpendicular);
            node2.AddForceVector(perpendicular);
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
    */
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
    public static Vector2f GetLennardJonesLikeForce(float ljConstant, Edge edge, Node2D n, LJForceType type) {
        Vector2f pointOnEdge = CustomMath.pointSlope(n, edge);
        Vector2f forceVector;
        if(Float.isNaN(pointOnEdge.x) || Float.isNaN(pointOnEdge.y))
        {
            System.out.println("POINT ON EDGE NULL");
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
            System.out.println("FORCE MAGNITUDE NULL");
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
