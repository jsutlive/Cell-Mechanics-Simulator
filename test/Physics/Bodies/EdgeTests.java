package Physics.Bodies;
import Physics.Bodies.Cell.ApicalEdge;
import Physics.Bodies.Cell.Cell;
import Physics.Bodies.Cell.CellEdge;
import Physics.Bodies.Cell.CellNode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

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
    void calculate_edge_length_no_difference_vertex_order()
    {
        Vertex a = new CellNode();
        a.setPosition(0f,1f);

        Vertex b = new CellNode();
        b.setPosition(0f,0f);

        CellEdge edge = new CellEdge(b,a);
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

    @Test
    void calculate_edge_id_equals_cell_id()
    {
        CellNode a =new CellNode();
        a.setPosition(0,0);
        CellNode b =new CellNode();
        b.setPosition(0,1);
        ApicalEdge testEdge = new ApicalEdge<>(a, b);

        ArrayList<Edge> edge = new ArrayList<>();
        edge.add(testEdge);
        ArrayList<Vertex> vertices = new ArrayList<>();
        vertices.add(a); vertices.add(b);
        Cell testCell = Cell.createCellStructure(vertices, edge);
        Cell.resetID();

        assertEquals(testCell.getID(), testEdge.getCell().getID());
    }

}
