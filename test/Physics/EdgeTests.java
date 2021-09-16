package Physics;

import Model.ApicalConstrictingCell;
import Model.Cell;
import Physics.Rigidbodies.*;
import Utilities.Geometry.Vector2f;
import Utilities.Model.Builder;
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
    void edges_equal_on_making_simple_structure() throws InstantiationException, IllegalAccessException {
        List<Cell> cells = Builder.getSimpleFourCellBox();
        float lastLength = 0f;
        float currentLength;
        int numberOfApicalEdges = 0;
        for(Cell cell:cells){
            for(Edge edge: cell.getEdges()) {
                if (edge instanceof ApicalEdge) {
                    numberOfApicalEdges++;
                    currentLength = edge.getLength();
                    //ASSERT: last apical edge equal to current apical edge
                    //        apical edge = 0;
                    if(lastLength!=0f){assertEquals(lastLength, currentLength);}
                    assertEquals(currentLength, 100f);
                    lastLength = currentLength;
                }
            }
        }
        //ASSERT: There is one apical edge per cell, or the size of the cell list is equal to the apical edges counted
        assertEquals(cells.size(), numberOfApicalEdges);
    }


    @Test
    void get_correct_length_of_edge(){
        Node a = new Node(0,0);
        Node b = new Node(0,5);
        Edge e = new BasicEdge(a,b);
        Node[] nodes = e.getNodes();
        Vector2f aVec = nodes[0].getPosition();
        Vector2f bVec = nodes[1].getPosition();
        float dist = Vector2f.dist(aVec, bVec);
        assertEquals(5,dist);
    }
}
