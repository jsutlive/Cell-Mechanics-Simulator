package Physics;

import Physics.Bodies.Cell.CellEdge;
import Physics.Bodies.Cell.CellNode;
import Physics.Bodies.Vertex;
import Physics.Forces.Springs.ElasticSpring;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SpringTests {
    @Test
    void check_elastic_spring_calculation()
    {
        Vertex a = new CellNode();
        a.setPosition(0f,1f);

        Vertex b = new CellNode();
        b.setPosition(0f,0f);

        CellEdge edge = new CellEdge(a,b);
        ElasticSpring spring = ElasticSpring.configureNew(10, .1f);

        float forceTest = spring.getForceMagnitude(edge);

        assertEquals(9f, forceTest);
    }
}
