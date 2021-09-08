package Physics.Bodies;

import Physics.Bodies.Cell.Cell;
import Physics.Bodies.Cell.CellNode;
import Physics.Forces.Springs.ElasticSpring;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CellTests {
    @Test
    void check_if_adjacent_cells_add_forces_to_shared_vertices()
    {
        List<Cell> cells = TestUtilityBodies.maketwoBasicCells();
        Cell cellA = cells.get(0);
        Cell cellB = cells.get(1);
        ElasticSpring spring = ElasticSpring.configureNew(1f,.5f);

        //spring.attach(cellA);
        //spring.attach(cellB);

        spring.update();

        CellNode test = new CellNode();
        for(Vertex v: cellA.vertices)
        {
            CellNode node = (CellNode)v;
            if(cellB.vertices.contains(node))
            {
                test = node;
                break;
            }
        }
        assertEquals(2, test.timesForceAdded);
    }
    @Test
    void check_number_of_listeners_of_spring_force()
    {
        List<Cell> cells = TestUtilityBodies.maketwoBasicCells();
        Cell cellA = cells.get(0);
        Cell cellB = cells.get(1);
        ElasticSpring spring = ElasticSpring.configureNew(1f,.5f);

        spring.attach(cellA);
        spring.attach(cellB);
        assertEquals(2, spring.getListeners().size());
    }
}
