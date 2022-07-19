package Physics;

import Model.Cells.Cell;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.LateralEdge;
import Physics.Rigidbodies.Node;
import Utilities.Model.Builder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CellTests {
    static List<Node> cellNodes = new ArrayList<>();
    static List<Edge> edgeListA = new ArrayList<>();
    static List<Edge> edgeListB = new ArrayList<>();
    static Cell testCell;

    @BeforeAll
    static void cell_setup() throws InstantiationException, IllegalAccessException {
        // Create six nodes with side a and side b
        Node a1 = new Node(0,0);    cellNodes.add(a1);
        Node a2 = new Node(0,1);    cellNodes.add(a2);
        Node a3 = new Node(0,2);    cellNodes.add(a3);
        Node b1 = new Node(1,0);    cellNodes.add(b1);
        Node b2 = new Node(1,1);    cellNodes.add(b2);
        Node b3 = new Node(1, 2);   cellNodes.add(b3);

        //create 4 lateral edges, two on each side
        //side a
        Edge ae1 = new LateralEdge(a1,a2);  edgeListA.add(ae1);
        Edge ae2 = new LateralEdge(a2,a3);  edgeListA.add(ae2);

        //side b
        Edge be1 = new LateralEdge(b1,b2);  edgeListB.add(be1);
        Edge be2 = new LateralEdge(b2,b3);  edgeListB.add(be2);

        //build cell
        testCell = Builder.createCell(edgeListA, edgeListB, Cell.class);
    }

    @Test
    void check_that_number_of_nodes_consistent_with_cell_creation(){
        assertEquals(6, testCell.getNodes().size());
    }

    @Test
    void check_that_nodes_consistent_during_cell_creation(){
        for(Node node: cellNodes){
            int numberOfCopiesOfNode = 0;
            for(Node cellNode: testCell.getNodes()){
                if(node == cellNode) numberOfCopiesOfNode++;
            }
            assertEquals(1, numberOfCopiesOfNode);
        }
    }
}
