package Physics.Bodies;

import Model.DrosophilaEmbryo;
import Physics.Bodies.Cell.Cell;
import Physics.Bodies.Cell.CellNode;
import Physics.Forces.Springs.ElasticSpring;
import Utilities.Geometry.Vector2f;
import Utilities.Geometry.Vector2i;
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

        spring.attach(cellA);
        spring.attach(cellB);

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
        assertEquals(7, spring.getListeners().size());
    }

    @Test
    /**
     * This test checks to see if the vertices shared between adjacent cells are represented by the same object instance
     */
    void check_if_vertex_shared_by_adjacent_cells()
    {

        List<Cell> cells = TestUtilityBodies.maketwoBasicCells();
        Cell cellA = cells.get(0);
        Cell cellB = cells.get(1);
        Vertex a = new CellNode();
        Vertex b = new CellNode();
        for(Vertex node: cellA.vertices)
        {
            if(node.position == new Vector2f(1f,0f)) a = node;
            break;
        }
        for(Vertex node: cellB.vertices)
        {
            if(node.position == new Vector2f(1f,0f)) b = node;
            break;
        }
        assertEquals(a,b);
    }

    @Test
    void check_if_vertex_shared_by_adjacent_cells_in_embryo_object()
    {
        Vector2i bounds = new Vector2i(800);
        DrosophilaEmbryo embryo = new DrosophilaEmbryo(bounds);
        embryo.generateOrganism();
        Cell cellA = embryo.allCells.getCell(0);
        Cell cellB = embryo.allCells.getCell(1);
        int count = 0;
        for (Vertex v: cellA.vertices)
        {
            for (Vertex w: cellB.vertices) {
                if (w == v) count++;
            }

        }
        assertEquals(5, count);
    }
}
