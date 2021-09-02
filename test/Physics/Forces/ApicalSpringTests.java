package Physics.Forces;

import Model.DrosophilaEmbryo;
import Model.Model;
import Physics.Bodies.Cell.ApicalEdge;
import Physics.Bodies.Cell.Cell;
import Physics.Bodies.Cell.CellNode;
import Physics.Bodies.Edge;
import Physics.Bodies.PhysicsBody;
import Physics.Bodies.Vertex;
import Physics.Forces.Springs.ApicalSpring;
import Utilities.Geometry.Vector2i;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class ApicalSpringTests
{
    @Test
    void test_if_forces_equal_at_cell_79_and_0()
    {
        Model.TOTAL_CELLS = 80;
        Vector2i bounds = new Vector2i(800);
        DrosophilaEmbryo embryo = new DrosophilaEmbryo(bounds);
        embryo.generateOrganism();
        float[] constants = {0.5f,1};
        float[] ratios = {0.5f, 1};
        ApicalSpring spring = ApicalSpring.configureNew(constants, ratios);
        spring.attach(embryo.apicalConstrictingCells);
        spring.setEquationFactors();

        Cell cell0 = embryo.getAllCells().getCell(0);
        ApicalEdge edgeCell0 = new ApicalEdge<>();
        for(Edge edge: cell0.getAllEdges())
        {
            if(edge instanceof ApicalEdge) edgeCell0 = (ApicalEdge)edge;
        }
        Cell cell79 = embryo.getAllCells().getCell(79);
        ApicalEdge edgeCell79 = new ApicalEdge<>();
        for(Edge edge: cell79.getAllEdges())
        {
            if(edge instanceof ApicalEdge) edgeCell79 = (ApicalEdge)edge;
        }

        spring.calculateForce(edgeCell0);
        spring.calculateForce(edgeCell79);
        assertEquals(edgeCell0.getForce(spring), edgeCell79.getForce(spring));
    }

    @Test
    void test_if_net_force_at_central_vertex_equals_zero() {
        Vector2i bounds = new Vector2i(800);
        DrosophilaEmbryo embryo = new DrosophilaEmbryo(bounds);
        embryo.generateOrganism();
        float[] constants = {0.5f, 1};
        float[] ratios = {0.5f, 1};
        ApicalSpring spring = ApicalSpring.configureNew(constants, ratios);
        spring.attach(embryo.apicalConstrictingCells);
        spring.setEquationFactors();

        Cell cell0 = embryo.getAllCells().getCell(0);
        ApicalEdge edgeCell0 = new ApicalEdge<>();
        for (Edge edge : cell0.getAllEdges()) {
            if (edge instanceof ApicalEdge) edgeCell0 = (ApicalEdge) edge;
        }
        Cell cell79 = embryo.getAllCells().getCell(79);
        ApicalEdge edgeCell79 = new ApicalEdge<>();
        for (Edge edge : cell79.getAllEdges()) {
            if (edge instanceof ApicalEdge) edgeCell79 = (ApicalEdge) edge;
        }

        spring.calculateForce(edgeCell0);
        spring.calculateForce(edgeCell79);

        Vertex v = edgeCell0.getVertices()[0];
        assertEquals(0, Math.abs(v.getForce(spring).x));
    }

    @Test
    void test_if_net_force_at_central_vertex_equals_zero_from_other_side() {
        Vector2i bounds = new Vector2i(800);
        DrosophilaEmbryo embryo = new DrosophilaEmbryo(bounds);
        embryo.generateOrganism();
        float[] constants = {0.5f, 1};
        float[] ratios = {0.5f, 1};
        ApicalSpring spring = ApicalSpring.configureNew(constants, ratios);
        spring.attach(embryo.apicalConstrictingCells);
        spring.setEquationFactors();

        Cell cell0 = embryo.getAllCells().getCell(0);
        ApicalEdge edgeCell0 = new ApicalEdge<>();
        for (Edge edge : cell0.getAllEdges()) {
            if (edge instanceof ApicalEdge) edgeCell0 = (ApicalEdge) edge;
        }
        Cell cell79 = embryo.getAllCells().getCell(79);
        ApicalEdge edgeCell79 = new ApicalEdge<>();
        for (Edge edge : cell79.getAllEdges()) {
            if (edge instanceof ApicalEdge) edgeCell79 = (ApicalEdge) edge;
        }

        spring.calculateForce(edgeCell0);
        spring.calculateForce(edgeCell79);

        Vertex v = edgeCell79.getVertices()[1];
        assertEquals(0, Math.abs(v.getForce(spring).x));
    }

    @Test
    void test_if_constrict_constant_consistent_between_opposite_springs()
    {
        Vector2i bounds = new Vector2i(800);
        DrosophilaEmbryo embryo = new DrosophilaEmbryo(bounds);
        embryo.generateOrganism();
        float[] constants = {0.5f, 1};
        float[] ratios = {0.5f, 1};
        ApicalSpring spring = ApicalSpring.configureNew(constants, ratios);
        spring.attach(embryo.apicalConstrictingCells);
        spring.setEquationFactors();

        Cell cell0 = embryo.getAllCells().getCell(0);
        ApicalEdge edgeCell0 = new ApicalEdge<>();
        for (Edge edge : cell0.getAllEdges()) {
            if (edge instanceof ApicalEdge) edgeCell0 = (ApicalEdge) edge;
        }
        Cell cell79 = embryo.getAllCells().getCell(79);
        ApicalEdge edgeCell79 = new ApicalEdge<>();
        for (Edge edge : cell79.getAllEdges()) {
            if (edge instanceof ApicalEdge) edgeCell79 = (ApicalEdge) edge;
        }

        spring.calculateForce(edgeCell0);
        spring.calculateForce(edgeCell79);

        assertEquals(spring.getConstant(edgeCell0), Math.abs(spring.getConstant(edgeCell79)*-1));
    }

    @Test
    void test_if_constrict_ratio_consistent_between_opposite_springs()
    {
        Vector2i bounds = new Vector2i(800);
        DrosophilaEmbryo embryo = new DrosophilaEmbryo(bounds);
        embryo.generateOrganism();
        float[] constants = {0.5f, 1};
        float[] ratios = {0.5f, 1};
        ApicalSpring spring = ApicalSpring.configureNew(constants, ratios);
        spring.attach(embryo.apicalConstrictingCells);
        spring.setEquationFactors();

        Cell cell0 = embryo.getAllCells().getCell(0);
        ApicalEdge edgeCell0 = new ApicalEdge<>();
        for (Edge edge : cell0.getAllEdges()) {
            if (edge instanceof ApicalEdge) edgeCell0 = (ApicalEdge) edge;
        }
        Cell cell79 = embryo.getAllCells().getCell(79);
        ApicalEdge edgeCell79 = new ApicalEdge<>();
        for (Edge edge : cell79.getAllEdges()) {
            if (edge instanceof ApicalEdge) edgeCell79 = (ApicalEdge) edge;
        }

        spring.calculateForce(edgeCell0);
        spring.calculateForce(edgeCell79);

        assertEquals(spring.getRatio(edgeCell0), spring.getRatio(edgeCell79));
    }

    @Test
    void test_if_location_value_consistent_between_opposite_springs()
    {
        Model.TOTAL_CELLS = 80;
        Vector2i bounds = new Vector2i(800);
        DrosophilaEmbryo embryo = new DrosophilaEmbryo(bounds);
        embryo.generateOrganism();
        float[] constants = {0.5f, 1};
        float[] ratios = {0.5f, 1};
        ApicalSpring spring = ApicalSpring.configureNew(constants, ratios);
        spring.attach(embryo.apicalConstrictingCells);
        spring.setEquationFactors();

        Cell cell0 = embryo.getAllCells().getCell(0);
        ApicalEdge edgeCell0 = new ApicalEdge<>();
        for (Edge edge : cell0.getAllEdges()) {
            if (edge instanceof ApicalEdge) edgeCell0 = (ApicalEdge) edge;
        }
        Cell cell79 = embryo.getAllCells().getCell(79);
        ApicalEdge edgeCell79 = new ApicalEdge<>();
        for (Edge edge : cell79.getAllEdges()) {
            if (edge instanceof ApicalEdge) edgeCell79 = (ApicalEdge) edge;
        }

        spring.calculateForce(edgeCell0);
        spring.calculateForce(edgeCell79);

        assertEquals(spring.getLoc(edgeCell0), spring.getLoc(edgeCell79));
    }

    @Test
    void test_if_cutoff_value_consistent_between_opposite_springs()
    {
        Model.TOTAL_CELLS = 80;
        Vector2i bounds = new Vector2i(800);
        DrosophilaEmbryo embryo = new DrosophilaEmbryo(bounds);
        embryo.generateOrganism();
        float[] constants = {0.5f, 1};
        float[] ratios = {0.5f, 1};
        ApicalSpring spring = ApicalSpring.configureNew(constants, ratios);
        spring.attach(embryo.apicalConstrictingCells);
        spring.setEquationFactors();

        Cell cell0 = embryo.getAllCells().getCell(0);
        ApicalEdge edgeCell0 = new ApicalEdge<>();
        for (Edge edge : cell0.getAllEdges()) {
            if (edge instanceof ApicalEdge) edgeCell0 = (ApicalEdge) edge;
        }
        Cell cell79 = embryo.getAllCells().getCell(79);
        ApicalEdge edgeCell79 = new ApicalEdge<>();
        for (Edge edge : cell79.getAllEdges()) {
            if (edge instanceof ApicalEdge) edgeCell79 = (ApicalEdge) edge;
        }

        spring.calculateForce(edgeCell0);
        spring.calculateForce(edgeCell79);

        assertEquals(spring.getCutoff(edgeCell0), spring.getCutoff(edgeCell79));
    }

    @Test
    void test_if_location_value_for_cell_0_equals_1()
    {
        Model.TOTAL_CELLS = 80;
        Vector2i bounds = new Vector2i(800);
        DrosophilaEmbryo embryo = new DrosophilaEmbryo(bounds);
        embryo.generateOrganism();
        float[] constants = {0.5f, 1};
        float[] ratios = {0.5f, 1};
        ApicalSpring spring = ApicalSpring.configureNew(constants, ratios);
        spring.attach(embryo.apicalConstrictingCells);
        spring.setEquationFactors();

        Cell cell0 = embryo.getAllCells().getCell(0);
        ApicalEdge edgeCell0 = new ApicalEdge<>();
        for (Edge edge : cell0.getAllEdges()) {
            if (edge instanceof ApicalEdge) edgeCell0 = (ApicalEdge) edge;
        }
        assertEquals(1, spring.getLoc(edgeCell0));
    }

    @Test
    void test_if_location_value_for_cell_79_equals_1()
    {
        Model.TOTAL_CELLS = 80;
        Vector2i bounds = new Vector2i(800);
        DrosophilaEmbryo embryo = new DrosophilaEmbryo(bounds);
        embryo.generateOrganism();
        float[] constants = {0.5f, 1};
        float[] ratios = {0.5f, 1};
        ApicalSpring spring = ApicalSpring.configureNew(constants, ratios);
        spring.attach(embryo.apicalConstrictingCells);
        spring.setEquationFactors();

        Cell cell0 = embryo.getAllCells().getCell(79);
        ApicalEdge edgeCell0 = new ApicalEdge<>();
        for (Edge edge : cell0.getAllEdges()) {
            if (edge instanceof ApicalEdge) edgeCell0 = (ApicalEdge) edge;
        }
        assertEquals(1, spring.getLoc(edgeCell0));
    }

    @Test
    void test_if_apical_spring_only_adds_force_two_times_to_point_0()
    {
        Vector2i bounds = new Vector2i(800);
        DrosophilaEmbryo embryo = new DrosophilaEmbryo(bounds);
        embryo.generateOrganism();
        float[] constants = {0.5f, 1};
        float[] ratios = {0.5f, 1};
        ApicalSpring spring = ApicalSpring.configureNew(constants, ratios);
        spring.attach(embryo.apicalConstrictingCells);
        spring.setEquationFactors();

        Cell cell0 = embryo.getAllCells().getCell(0);
        ApicalEdge edgeCell0 = new ApicalEdge<>();
        for (Edge edge : cell0.getAllEdges()) {
            if (edge instanceof ApicalEdge) edgeCell0 = (ApicalEdge) edge;
        }
        CellNode node = (CellNode)edgeCell0.getVertices()[0];

        spring.update();

        assertEquals(2, node.timesForceAdded);
    }

    @Test
    void test_if_apical_spring_only_adds_force_two_times_to_random_other_point()
    {
        Vector2i bounds = new Vector2i(800);
        DrosophilaEmbryo embryo = new DrosophilaEmbryo(bounds);
        embryo.generateOrganism();
        float[] constants = {0.5f, 1};
        float[] ratios = {0.5f, 1};
        ApicalSpring spring = ApicalSpring.configureNew(constants, ratios);
        spring.attach(embryo.apicalConstrictingCells);
        spring.setEquationFactors();

        Cell cell0 = embryo.getAllCells().getCell(75);
        ApicalEdge edgeCell0 = new ApicalEdge<>();
        for (Edge edge : cell0.getAllEdges()) {
            if (edge instanceof ApicalEdge) edgeCell0 = (ApicalEdge) edge;
        }
        CellNode node = (CellNode)edgeCell0.getVertices()[0];

        spring.update();

        assertEquals(2, node.timesForceAdded);
    }

    @Test
    void test_if_apical_spring_only_adds_force_two_times_to_zero_point_hardcode_addition()
    {
        Vector2i bounds = new Vector2i(800);
        DrosophilaEmbryo embryo = new DrosophilaEmbryo(bounds);
        embryo.generateOrganism();
        float[] constants = {0.5f, 1};
        float[] ratios = {0.5f, 1};
        ApicalSpring spring = ApicalSpring.configureNew(constants, ratios);
        spring.attach(embryo.apicalConstrictingCells);
        spring.setEquationFactors();

        Cell cell0 = embryo.getAllCells().getCell(0);
        ApicalEdge edgeCell0 = new ApicalEdge<>();
        for (Edge edge : cell0.getAllEdges()) {
            if (edge instanceof ApicalEdge) edgeCell0 = (ApicalEdge) edge;
        }
        Cell cell79 = embryo.getAllCells().getCell(79);
        ApicalEdge edgeCell79 = new ApicalEdge<>();
        for (Edge edge : cell79.getAllEdges()) {
            if (edge instanceof ApicalEdge) edgeCell79 = (ApicalEdge) edge;
        }
        CellNode node = (CellNode)edgeCell0.getVertices()[0];

        spring.calculateForce(edgeCell0);
        spring.calculateForce(edgeCell79);

        assertEquals(2, node.timesForceAdded);
    }

    @Test
    void test_if_adjacent_edges_share_vertices() {
        Vector2i bounds = new Vector2i(800);
        DrosophilaEmbryo embryo = new DrosophilaEmbryo(bounds);
        embryo.generateOrganism();
        float[] constants = {0.5f, 1};
        float[] ratios = {0.5f, 1};
        ApicalSpring spring = ApicalSpring.configureNew(constants, ratios);
        spring.attach(embryo.apicalConstrictingCells);
        spring.setEquationFactors();

        Cell cell0 = embryo.getAllCells().getCell(0);
        ApicalEdge edgeCell0 = new ApicalEdge<>();
        for (Edge edge : cell0.getAllEdges()) {
            if (edge instanceof ApicalEdge) edgeCell0 = (ApicalEdge) edge;
        }
        Cell cell79 = embryo.getAllCells().getCell(79);
        ApicalEdge edgeCell79 = new ApicalEdge<>();
        for (Edge edge : cell79.getAllEdges()) {
            if (edge instanceof ApicalEdge) edgeCell79 = (ApicalEdge) edge;
        }

        assertEquals(edgeCell0.getVertices()[0], edgeCell79.getVertices()[1]);
    }
}
