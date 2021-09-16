package Physics;

import Model.Cell;
import Physics.Rigidbodies.*;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EdgeTests
{
    @Test
    void geometric_functions_work_for_simple_right_triangle_3_4_5()
    {
        // Make a 3-4-5 right triangle
        Node a = new Node(0f,0f);
        Node b = new Node(3f, 0f);
        Node c = new Node(3f, 4f);

        Edge A = new LateralEdge(a, b);
        Edge B = new LateralEdge(b,c);
        Edge C = new LateralEdge(a, c);

        assertEquals(5, C.getLength());
        assertEquals(3, A.getLength());
        assertEquals(4, B.getLength());
        assertEquals( A.getLength(), C.getXUnit() * C.getLength());
        //assertEquals(B.getLength(), C.getYUnit() * C.getLength());
    }

    @Test
    void edges_equal_on_making_simple_structure(){
        List<Cell> cells = getSimpleFourCellBox();
        float lastLength = 0f;
        float currentLength;
        for(Cell cell:cells){
            for(Edge edge: cell.getEdges()) {
                if (edge instanceof ApicalEdge) {
                    currentLength = edge.getLength();
                    //ASSERT: last apical edge equal to current apical edge
                    //        apical edge = 0;
                    if(lastLength!=0f){assertEquals(lastLength, currentLength);}
                    assertEquals(currentLength, 1f);
                    lastLength = currentLength;
                }
            }
        }
    }

    /**
     * Makes four cells with simple box structure where the cells have four edges, each with a length of 1.
     * Three are Basic Edges, one is an Apical Edge.
     * @return list of cells in the structure.
     */
    private List<Cell> getSimpleFourCellBox() {
        Node p00 = new Node(0,0);
        Node p01 = new Node(0,1);
        Node p10 = new Node(1, 0);
        Node p11 = new Node(1,1);

        Node p20 = new Node(2,0);
        Node p21 = new Node(2,1);

        Node p30 = new Node(3,0);
        Node p31 = new Node(3,1);

        Node p40 = new Node(4,0);
        Node p41 = new Node(4,1);

        List<Edge> cell1Edges = Arrays.asList(new BasicEdge(p00, p01),
                new BasicEdge(p01, p11),
                new ApicalEdge(p00, p10),
                new BasicEdge(p10, p11));
        List<Node> cell1Vertices = Arrays.asList(p00, p01, p10, p11);
        Cell cell1 = new Cell();
        cell1.setEdges(cell1Edges);
        cell1.setNodes(cell1Vertices);

        List<Edge> cell2Edges = Arrays.asList(new BasicEdge(p11, p21),
                new BasicEdge(p10, p11),
                new ApicalEdge(p10, p20),
                new BasicEdge(p20, p21));
        List<Node> cell2Vertices = Arrays.asList(p10, p11, p20, p21);
        Cell cell2 = new Cell();
        cell2.setEdges(cell2Edges);
        cell2.setNodes(cell2Vertices);

        List<Edge> cell3Edges = Arrays.asList(new BasicEdge(p21, p31),
                new BasicEdge(p20, p21),
                new ApicalEdge(p20, p30),
                new BasicEdge(p30, p31));
        List<Node> cell3Vertices = Arrays.asList(p20, p21, p30, p31);
        Cell cell3 = new Cell();
        cell3.setEdges(cell3Edges);
        cell3.setNodes(cell3Vertices);

        List<Edge> cell4Edges = Arrays.asList(new BasicEdge(p31, p41),
                new BasicEdge(p30, p31),
                new ApicalEdge(p30, p40),
                new BasicEdge(p40, p41));
        List<Node> cell4Vertices = Arrays.asList(p30, p31, p40, p41);
        Cell cell4 = new Cell();
        cell4.setEdges(cell4Edges);
        cell4.setNodes(cell4Vertices);

        List<Cell> cells = Arrays.asList(cell1,cell2,cell3,cell4);
        return cells;
    }
}
