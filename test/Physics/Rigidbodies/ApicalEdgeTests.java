package Physics.Rigidbodies;

import Physics.Forces.Force;
import Physics.Rigidbodies.Edges.ApicalEdge;
import Physics.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector2f;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class ApicalEdgeTests {

    static ApicalEdge e;
    @BeforeAll
    static void setup()
    {
        Vector2f pos_1 = new Vector2f(0,0);
        Vector2f pos_2 = new Vector2f(0,1);

        Node2D a = new Node2D(pos_1);
        Node2D b = new Node2D(pos_2);

        e = new ApicalEdge(a, b);
    }

    @ParameterizedTest
    @MethodSource("getValues")
    void constricting_edge_resultant_force_equals_calculated_spring_force(float constant)
    {
        float ratio = 0.5f;
        Vector2f result = Force.calculateSpringForce(e, constant, ratio);


    }

    private static float[] getValues()
    {
        return new float[]{.1f,.2f,.3f,.4f,.5f,.6f,.7f,.8f,.9f,1f};
    }
}
