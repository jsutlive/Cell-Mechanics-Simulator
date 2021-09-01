package Physics.Bodies;
import Physics.Bodies.Cell.CellEdge;
import Physics.Bodies.Cell.CellNode;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EdgeTests {
    @Test
    void calculate_edge_length_y_difference()
    {
        Vertex a = new CellNode();
        a.setPosition(0f,1f);

        Vertex b = new CellNode();
        b.setPosition(0f,0f);

        CellEdge edge = new CellEdge(a,b);
        assertEquals(1f, edge.getLength());
    }

    @Test
    void calculate_edge_length_xy_difference() {
        Vertex a = new CellNode();
        a.setPosition(3f, 4f);

        Vertex b = new CellNode();
        b.setPosition(0f, 0f);

        CellEdge edge = new CellEdge(a, b);
        assertEquals(5, edge.getLength());
    }

}
