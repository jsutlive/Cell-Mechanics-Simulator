package Physics.Bodies;

import Physics.Bodies.Cell.Cell;
import Physics.Bodies.Cell.CellEdge;
import Physics.Bodies.Cell.CellNode;
import Physics.Bodies.Edge;
import Physics.Bodies.Vertex;

import java.util.ArrayList;
import java.util.List;

public class TestUtilityBodies {

    public static List<Cell> maketwoBasicCells()
    {
        // Make 3 "top" nodes and 3 "bottom" nodes
        CellNode b_1 = new CellNode(); b_1.setPosition( 0,0);
        CellNode t_1 = new CellNode(); t_1.setPosition(0,1);
        CellNode b_2 = new CellNode(); b_2.setPosition( 1,0);
        CellNode t_2 = new CellNode(); t_2.setPosition(1,1);
        CellNode b_3 = new CellNode(); b_3.setPosition( 2,0);
        CellNode t_3 = new CellNode(); t_3.setPosition(2,1);

        Edge v1 = new CellEdge<>(b_1, t_1);
        Edge v2 = new CellEdge<>(b_2, t_2);
        Edge v3 = new CellEdge<>(b_3, t_3);

        Edge h1 = new CellEdge<>(t_1, t_2);
        Edge h2 = new CellEdge<>(b_1, b_2);
        Edge h3 = new CellEdge<>(t_2, t_3);
        Edge h4 = new CellEdge<>(b_2, b_3);

        List<Edge> cellAEdges = new ArrayList<>();
        List<Vertex> cellAVertices = new ArrayList<>();
        cellAVertices.add(b_1); cellAVertices.add(b_2); cellAVertices.add(t_1); cellAVertices.add(t_2);
        List<Vertex> cellBVertices = new ArrayList<>();
        cellBVertices.add(b_2); cellBVertices.add(b_3); cellBVertices.add(t_2); cellBVertices.add(t_3);

        cellAEdges.add(v1); cellAEdges.add(v2); cellAEdges.add(h1); cellAEdges.add(h2);
        List<Edge> cellBEdges = new ArrayList<>();
        cellBEdges.add(v2); cellBEdges.add(v3); cellBEdges.add(h3); cellBEdges.add(h4);

        Cell cellA = Cell.createCellStructure(cellAVertices, cellAEdges);
        Cell cellB = Cell.createCellStructure(cellBVertices, cellBEdges);

        List<Cell> cells = new ArrayList<>();
        cells.add(cellA); cells.add(cellB);
        return cells;
    }
}
