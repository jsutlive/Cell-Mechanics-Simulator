package Physics.Bodies;

import Physics.Bodies.Cell.CellNode;
import Physics.Forces.Springs.ApicalSpring;
import Physics.Forces.Springs.ElasticSpring;
import Utilities.Geometry.Vector2f;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CellNodeTests {
    @Test
    void check_if_node_adds_forces_when_called_properly()
    {
        CellNode node = new CellNode();
        node.setPosition(0,0);
        node.addForce(ElasticSpring.configureNew(1f,1f), new Vector2f(1f,1f));
        node.addForce(ElasticSpring.configureNew(1f,1f), new Vector2f(1f,1f));
        node.addForce(ElasticSpring.configureNew(1f,1f), new Vector2f(1f,1f));
        assertEquals(3, node.timesForceAdded);
    }
}
