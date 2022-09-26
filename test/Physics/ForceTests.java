package Physics;

import Physics.Forces.Force;
import Physics.Rigidbodies.BasicEdge;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector.Vector2f;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ForceTests {

    Edge edge;
    private final Vector2f pos00 = new Vector2f(0,0);
    private final Vector2f pos01 = new Vector2f(0,1);
    @BeforeEach
    void setup()
    {
        Node a = new Node(pos00);
        Node b = new Node(pos01);
        edge = new BasicEdge(a, b);
    }
    @Test
    void determine_elastic_constant_of_zero_does_not_move_nodes_in_constricted_edge()
    {
        float elasticConstant = 0f;
        Force.elastic(edge, elasticConstant);
        Node a = edge.getNodes()[0];
        assertEquals(a.getPosition().x, pos00.x);
        assertEquals(a.getPosition().y, pos00.y);
    }

    @Test
    void determine_force_of_5_not_damped_if_ratio_is_1(){
       //Vector2f force = new Vector2f(3, 4);
       //Vector2f testForce = Force.dampen(force, 3, 1f);
       //assertEquals(force.x, testForce.x);
       //assertEquals(force.y, testForce.y);
    }
    @Test
    void determine_force_of_5_damped_to_4_if_ratio_is_0_5f(){
       //Vector2f force = new Vector2f(3, 4);
       //Vector2f testForce = Force.dampen(force, 3, .5f);

       //float fMag1 = testForce.mag();
       //assertEquals(4f, fMag1);
    }

    @Test
    void determine_exception_thrown_if_ratio_greater_than_1(){
       // assertThrows(IllegalArgumentException.class, () ->{
       //     Force.dampen(new Vector2f(3, 4), 3, 1.5f);
       // });
    }

    @Test
    void determine_constrict_never_flips_edge_nodes(){
        float high_num = 1e10f;
        Force.constrict(edge, high_num, 0.01f);
        Node a = edge.getNodes()[0];
        Node b = edge.getNodes()[1];
        a.Move(); b.Move();
        assertTrue(a.getPosition().y < b.getPosition().y);
    }
}
