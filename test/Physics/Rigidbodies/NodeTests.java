package Physics.Rigidbodies;

import Engine.Simulation;
import Physics.Rigidbodies.Nodes.Node;
import Physics.Rigidbodies.Nodes.Node2D;
import Utilities.Geometry.Vector.Vector;
import Utilities.Geometry.Vector.Vector2f;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class NodeTests {

    Node nodeA;
    Node nodeB;

    @BeforeEach
    void reset(){
        nodeA = new Node2D(0,0);
        nodeA.resetResultantForce();

        nodeB = new Node2D(1,1);
        nodeB.resetResultantForce();
    }

    @Test
    void check_force_added_to_node(){
        Vector force = new Vector2f(1,1);
        nodeA.addForceVector(force);
        assertTrue(force.equals(nodeA.getResultantForce()));
    }

    @Test
    void check_force_added_to_node_leads_to_change_in_node_position_when_moved(){
        Vector force = new Vector2f(1,1);
        nodeA.addForceVector(force);
        nodeA.move();
        assertTrue(nodeA.getPosition().equals(new Vector2f(1,1).mul(Simulation.TIMESTEP)));
    }

    @Test
    void check_multiple_forces_added_to_node_leads_to_change_in_node_position_when_moved(){
        Vector force = new Vector2f(1,1);
        Vector force2 = new Vector2f(1,2);
        nodeA.addForceVector(force);
        nodeA.addForceVector(force2);
        nodeA.move();
        assertTrue(nodeA.getPosition().equals(new Vector2f(2,3).mul(Simulation.TIMESTEP)));
    }

    @Test
    void check_multiple_forces_added_to_node_leads_to_change_in_node_position_when_two_nodes_moved_oppossite(){
        Vector force = new Vector2f(1,1);
        Vector force2 = force.neg();
        nodeA.addForceVector(force);
        nodeB.addForceVector(force2);
        nodeA.move();
        nodeB.move();
        assertTrue(nodeA.getPosition().equals(new Vector2f(1,1).mul(Simulation.TIMESTEP)));

        Vector testVectorB = new Vector2f(1,1).sub(force.mul(Simulation.TIMESTEP));
        assertTrue(nodeB.getPosition().equals(testVectorB));
    }
}
