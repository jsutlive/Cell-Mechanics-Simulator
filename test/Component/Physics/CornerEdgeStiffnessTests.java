package Component.Physics;

import Morphogenesis.Components.Physics.Collision.CornerStiffness2D;
import Morphogenesis.Rigidbodies.Nodes.Node;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CornerEdgeStiffnessTests {

    static Node2D a,b,c,d;
    @BeforeAll
    static void setup(){
        a = new Node2D(-1,1);
        b = new Node2D(0,0);
        c = new Node2D(1,0);
        d = new Node2D(2,1);
    }

    @Test
    void check_forces_apply_equally_to_mirrored_node_setups_corner_stiffness(){
        CornerStiffness2D cstiff = new CornerStiffness2D();
        List<Node> cornerA = new ArrayList<>(Arrays.asList(a,b,c));
        List<Node> cornerB = new ArrayList<>(Arrays.asList(b,c,d));

        Vector cornerAForce = cstiff.calculateCornerStiffness(cornerA);
        Vector cornerBForce = cstiff.calculateCornerStiffness(cornerB);

        assertEquals(cornerAForce.mag(), cornerBForce.mag());
        assertEquals(Math.abs(cornerAForce.get(0)), Math.abs(cornerAForce.get(0)));
        assertEquals(Math.abs(cornerAForce.get(1)), Math.abs(cornerAForce.get(1)));

    }
}
