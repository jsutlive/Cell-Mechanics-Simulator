package framework.rigidbodies;

import utilities.geometry.Vector.Vector2f;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static framework.states.RunState.deltaTime;

public class Node2DTests {
    @Test
    void forces_only_applied_once()
    {
        Node2D n = new Node2D(0,0);
        n.addForceVector(new Vector2f(0f,1f));
        n.addForceVector(new Vector2f(1f, 0f));
        n.move();
        //Check to make sure reset occurs
        n.move();
        assertEquals(1 * deltaTime, n.getPosition().x);
        assertEquals(1 * deltaTime, n.getPosition().y);
    }

    @Test
    void moveTo_moves_to_correct_position()
    {
        Node2D n = new Node2D(0,0);
        n.moveTo(new Vector2f(1));
        assertEquals(1, n.getPosition().x);
        assertEquals(1, n.getPosition().y);
    }
}
