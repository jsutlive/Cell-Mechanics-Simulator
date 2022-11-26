package Morphogenesis.Rigidbodies;


import Morphogenesis.Rigidbodies.Nodes.Node;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector;
import Utilities.Geometry.Vector.Vector2f;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static Framework.States.RunState.deltaTime;

public class NodeTests {

    static Node nodeA = new Node2D(0,0);
    static Node nodeB = new Node2D(1,1);

    @BeforeAll
    static void setup(){}

    @BeforeEach
    void reset(){
        nodeA = new Node2D(0,0);
        nodeB = new Node2D(1,1);
    }

    @Test
    void test_node_position_moves_when_force_applied(){
        Vector v = new Vector2f(1,1);
        nodeA.addForceVector(v);
        nodeA.move();
        Vector2f newPosition = (Vector2f) nodeA.getPosition();
        assertEquals(deltaTime, newPosition.x);
        assertEquals(deltaTime, newPosition.y);
    }

    @Test
    void test_node_position_moves_correctly_when_force_applied(){
        Vector v = new Vector2f(1,1);
        Vector w = v.neg();
        nodeA.addForceVector(v);
        nodeB.addForceVector(w);
        nodeA.move();
        nodeB.move();
        Vector2f newPositionA = (Vector2f) nodeA.getPosition();
        Vector2f newPositionB = (Vector2f) nodeB.getPosition();

        assertEquals( deltaTime, newPositionA.x);
        assertEquals(deltaTime, newPositionA.y);

        assertEquals(0f * deltaTime, newPositionB.x);
        assertEquals(0f * deltaTime, newPositionB.y);
    }
}
