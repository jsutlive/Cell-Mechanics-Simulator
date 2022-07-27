package Physics.Rigidbodies;

import Engine.Simulation;
import Utilities.Geometry.Vector2f;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NodeTests {
    @Test
    void forces_only_applied_once()
    {
        Node n = new Node(0,0);
        n.AddForceVector(new Vector2f(0f,1f));
        n.AddForceVector(new Vector2f(1f, 0f));
        n.Move();
        //Check to make sure reset occurs
        n.Move();
        assertEquals(1 * Simulation.TIMESTEP, n.getPosition().x);
        assertEquals(1 * Simulation.TIMESTEP, n.getPosition().y);
    }

    @Test
    void moveTo_moves_to_correct_position()
    {
        Node n = new Node(0,0);
        n.MoveTo(new Vector2f(1));
        assertEquals(1, n.getPosition().x);
        assertEquals(1, n.getPosition().y);
    }

    @Test
    void reset_Resultant_Force_Sets_Values_To_Zero()
    {
        Node n = new Node(0, 0);
        assertEquals(n.getResultantForce().x, 0f);
        assertEquals(n.getResultantForce().x, 0f);
        n.AddForceVector(new Vector2f(5));
        n.resetResultantForce();
        assertEquals(n.getResultantForce().x, 0f);
        assertEquals(n.getResultantForce().x, 0f);

    }
}
