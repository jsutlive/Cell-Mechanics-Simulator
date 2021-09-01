package Model;

import Physics.Bodies.Cell.ApicalEdge;
import Physics.Bodies.Cell.BasalEdge;
import Physics.Bodies.Edge;
import Utilities.Geometry.Vector2i;
import org.junit.jupiter.api.Test;

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


}