package Physics.Rigidbodies;

import Physics.ForceTests;
import Physics.Forces.Force;
import Utilities.Geometry.Vector2f;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

public class ApicalEdgeTests {

    static ApicalEdge e;
    @BeforeAll
    static void setup()
    {
        Vector2f pos_1 = new Vector2f(0,0);
        Vector2f pos_2 = new Vector2f(0,1);

        Node a = new Node(pos_1);
        Node b = new Node(pos_2);

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