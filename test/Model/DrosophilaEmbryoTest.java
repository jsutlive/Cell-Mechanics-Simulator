package Model;

import Physics.Bodies.Cell.ApicalEdge;
import Physics.Bodies.Cell.BasalEdge;
import Physics.Bodies.Cell.Cell;
import Physics.Bodies.Edge;
import Utilities.Geometry.Vector2i;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class DrosophilaEmbryoTest {

    @Test
    void check_that_lengths_of_apical_edges_are_equal()
    {
        Vector2i bounds = new Vector2i(800);
        DrosophilaEmbryo embryo = new DrosophilaEmbryo(bounds);
        embryo.generateOrganism();
        ApicalEdge apicalEdge0 = new ApicalEdge();
        ApicalEdge apicalEdge79 = new ApicalEdge();
        for(Edge edge: embryo.getAllCells().getCell(0).getAllEdges())
        {
            if(edge instanceof ApicalEdge)apicalEdge0 = (ApicalEdge)edge;
        }
        for(Edge edge: embryo.getAllCells().getCell(79).getAllEdges())
        {
            if(edge instanceof ApicalEdge)apicalEdge79 = (ApicalEdge)edge;
        }

        assertEquals(apicalEdge0.getLength(),apicalEdge79.getLength());
    }

    @Test
    void check_that_cells_only_contain_one_apical_edge()
    {
        //ASSIGN
        Vector2i bounds = new Vector2i(800);
        DrosophilaEmbryo embryo = new DrosophilaEmbryo(bounds);
        embryo.generateOrganism();
        int totalApicalEdges = 0;
        //ACT
        for(Edge edge: embryo.getAllCells().getCell(0).getAllEdges())
        {
            if(edge instanceof ApicalEdge) totalApicalEdges ++;
        }
        //ASSERT
        assertEquals(totalApicalEdges, 1);
    }

    @Test
    void check_that_cells_only_contain_one_basal_edge()
    {
        //ASSIGN
        Vector2i bounds = new Vector2i(800);
        DrosophilaEmbryo embryo = new DrosophilaEmbryo(bounds);
        embryo.generateOrganism();
        int totalBasalEdges = 0;
        //ACT
        for(Edge edge: embryo.getAllCells().getCell(0).getAllEdges())
        {
            if(edge instanceof BasalEdge) totalBasalEdges ++;
        }
        //ASSERT
        assertEquals(totalBasalEdges, 1);
    }

    @Test
    void check_apical_edge_polarity_cell0()
    {
        Vector2i bounds = new Vector2i(800);
        DrosophilaEmbryo embryo = new DrosophilaEmbryo(bounds);
        embryo.generateOrganism();
        Collection<Edge> edges0 = embryo.getAllCells().getCell(0).getAllEdges();
        Collection<Edge> edges1 = embryo.getAllCells().getCell(1).getAllEdges();

        ApicalEdge edge0 = new ApicalEdge();
        ApicalEdge edge1 = new ApicalEdge();
        for(Edge e: edges0) if(e instanceof ApicalEdge)edge0 = (ApicalEdge)e;
        for(Edge e: edges1) if(e instanceof ApicalEdge)edge1 = (ApicalEdge)e;

        assertEquals(edge0.getVertices()[1], edge1.getVertices()[0]);
    }

    @Test
    void check_apical_edge_polarity_cell79()
    {
        Vector2i bounds = new Vector2i(800);
        DrosophilaEmbryo embryo = new DrosophilaEmbryo(bounds);
        embryo.generateOrganism();
        Collection<Edge> edges0 = embryo.getAllCells().getCell(79).getAllEdges();
        Collection<Edge> edges1 = embryo.getAllCells().getCell(0).getAllEdges();

        ApicalEdge edge0 = new ApicalEdge();
        ApicalEdge edge1 = new ApicalEdge();
        for(Edge e: edges0) if(e instanceof ApicalEdge)edge0 = (ApicalEdge)e;
        for(Edge e: edges1) if(e instanceof ApicalEdge)edge1 = (ApicalEdge)e;

        assertEquals(edge0.getVertices()[1], edge1.getVertices()[0]);
    }


}