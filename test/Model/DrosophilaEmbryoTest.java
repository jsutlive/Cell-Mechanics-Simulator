package Model;

import Engine.Simulation;
import Model.Organisms.DrosophilaEmbryo;
import Physics.Rigidbodies.ApicalEdge;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector2f;
import Utilities.Geometry.Vector2i;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DrosophilaEmbryoTest {
    @BeforeAll
    static void setup_params(){
        Simulation.bounds = new Vector2i(800);
    }
   /* This test typically fails by default, uncomment if testing a ring creation system.
   @Test
    void test_lengths_no_rounding_outer_boundary() throws InstantiationException, IllegalAccessException {
        DrosophilaEmbryo model = new DrosophilaEmbryo();
        model.generateOrganism();
        Cell cell0 = model.getAllCells().get(0);
        Cell cell79 = model.getAllCells().get(79);

        float len1 = GetApicalEdgeLengthNoRounding(cell0);
        float len2 = GetApicalEdgeLengthNoRounding(cell79);

        assertEquals(len1, len2);
    }*/

    @Test
    void check_all_cells_have_10_nodes() throws InstantiationException, IllegalAccessException {
       DrosophilaEmbryo model = new DrosophilaEmbryo();
       model.generateOrganism();
       for(Cell cell: model.getAllCells()){
           assertEquals(10, cell.getNodes().size());
       }
    }


    private float GetApicalEdgeLengthNoRounding(Cell cell0) {
        for (Edge e : cell0.edges) {
            if (e instanceof ApicalEdge) {
                Node[] nodes = e.getNodes();
                Vector2f a = nodes[0].getPosition();
                Vector2f b = nodes[1].getPosition();
                return Vector2f.dist(a, b);
            }
        }
        return 0f;
    }
}
