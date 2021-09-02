package Physics.Forces;

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
        spring.setLength(edge.getLength());

        float forceTest = spring.getForceMagnitude(edge);

        assertEquals(9f, forceTest);
    }

    @Test
    void check_initial_length_and_length_are_equal()
    {
        Vertex a = new CellNode();
        a.setPosition(0f,1f);

        Vertex b = new CellNode();
        b.setPosition(0f,0f);

        CellEdge edge = new CellEdge(a,b);
        assertEquals(edge.initialLength, edge.getLength());
    }

    @Test
    void check_ratio_matches_user_parameter()
    {
        float expectedRatio = 0.1f;
        ElasticSpring spring = ElasticSpring.configureNew(10, expectedRatio);
        assertEquals(expectedRatio, spring.getRatio());
    }
    @Test
    void check_constant_matches_user_parameter()
    {
        float expectedConstant = 10f;
        ElasticSpring spring = ElasticSpring.configureNew(expectedConstant, 0.1f);
        assertEquals(expectedConstant, spring.getConstant());
    }

}
