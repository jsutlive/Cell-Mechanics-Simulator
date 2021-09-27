package Physics.Rigidbodies;

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
        assertEquals(1, n.getPosition().x);
        assertEquals(1, n.getPosition().y);
    }

    @Test
    void moveTo_moves_to_correct_position()
    {
        Node n = new Node(0,0);
        n.MoveTo(new Vector2f(1));
        assertEquals(1, n.getPosition().x);
        assertEquals(1, n.getPosition().y);
    }
}