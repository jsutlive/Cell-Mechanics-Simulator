package Component.Physics;

import Morphogenesis.Components.Physics.Collision.CornerStiffness2D;
import Morphogenesis.Components.Physics.Collision.EdgeStiffness2D;
import Morphogenesis.Rigidbodies.Nodes.Node;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static Utilities.Math.CustomMath.normal;

public class CornerEdgeStiffnessTests {

    static Node2D a,b,c,d;
    static EdgeStiffness2D eStiff;
    static CornerStiffness2D cStiff;

    @BeforeAll
    static void setup(){
        a = new Node2D(-1,1);
        b = new Node2D(0,0);
        c = new Node2D(1,0);
        d = new Node2D(2,1);
        eStiff = new EdgeStiffness2D();
        cStiff = new CornerStiffness2D();
    }

    @Test
    void check_forces_apply_equally_to_mirrored_node_setups_corner_stiffness(){
        List<Node> cornerA = new ArrayList<>(Arrays.asList(a,b,c));
        List<Node> cornerB = new ArrayList<>(Arrays.asList(b,c,d));

        Vector cornerAForce = cStiff.calculateCornerStiffness(cornerA);
        Vector cornerBForce = cStiff.calculateCornerStiffness(cornerB);

        assertEquals(cornerAForce.mag(), cornerBForce.mag());
        // This setup should have equal and opposite forces in x direction
        assertEquals(- 1 * cornerAForce.get(0), cornerBForce.get(0));
        // This setup should have equal forces in y direction
        assertEquals(cornerAForce.get(1),  cornerBForce.get(1));
    }

    @Test
    void check_forces_apply_equally_to_mirrored_node_setups_edge_stiffness(){
        List<Node2D> edgeA = new ArrayList<>(Arrays.asList(a,b,c));
        List<Node2D> edgeB = new ArrayList<>(Arrays.asList(b,c,d));

        Vector edgeAForce = normal(edgeA.get(0).getPosition(), edgeA.get(2).getPosition()).mul(eStiff.constant);
        Vector edgeBForce = normal(edgeB.get(0).getPosition(), edgeB.get(2).getPosition()).mul(eStiff.constant);

        assertEquals(edgeAForce.mag(), edgeBForce.mag());
        // This setup should have equal and opposite forces in x direction
        assertEquals(- 1 * edgeAForce.get(0), edgeBForce.get(0));
        // This setup should have equal forces in y direction
        assertEquals(edgeAForce.get(1),  edgeBForce.get(1));
    }

    @Test
    void check_forces_zero_if_90_degree_angle_corner_stiffness(){
        Node2D d_ = new Node2D(1, 1);
        List<Node> corner = new ArrayList<>(Arrays.asList(b,c,d_));
        Vector force = cStiff.calculateCornerStiffness(corner);
        assertEquals(0f, Math.abs(force.get(0)));
        assertEquals(0f, Math.abs(force.get(1)));
    }

    @Test
    void check_forces_zero_if_90_degree_angle_corner_stiffness_reverse_order(){
        Node2D d_ = new Node2D(1, 1);
        List<Node> corner = new ArrayList<>(Arrays.asList(d_, c,b));
        Vector force = cStiff.calculateCornerStiffness(corner);
        System.err.println(force.print());
        assertEquals(0f, Math.abs(force.get(0)));
        assertEquals(0f, Math.abs(force.get(1)));
    }
}
