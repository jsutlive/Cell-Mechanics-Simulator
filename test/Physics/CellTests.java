package Physics;

import Morphogenesis.Entities.Cell;
import Morphogenesis.Rigidbodies.Edges.Edge;
import Morphogenesis.Rigidbodies.Edges.LateralEdge;
import Morphogenesis.Rigidbodies.Nodes.Node2D;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CellTests {
    static List<Node2D> cellNodes = new ArrayList<>();
    static List<Edge> edgeListA = new ArrayList<>();
    static List<Edge> edgeListB = new ArrayList<>();
    static Cell testCell;

    @BeforeAll
    static void cell_setup() {
        // Create six nodes with side a and side b
        Node2D a1 = new Node2D(0,0);    cellNodes.add(a1);
        Node2D a2 = new Node2D(0,1);    cellNodes.add(a2);
        Node2D a3 = new Node2D(0,2);    cellNodes.add(a3);
        Node2D b1 = new Node2D(1,0);    cellNodes.add(b1);
        Node2D b2 = new Node2D(1,1);    cellNodes.add(b2);
        Node2D b3 = new Node2D(1, 2);   cellNodes.add(b3);

        //create 4 lateral edges, two on each side
        //side a
        Edge ae1 = new LateralEdge(a1,a2);  edgeListA.add(ae1);
        Edge ae2 = new LateralEdge(a2,a3);  edgeListA.add(ae2);

        //side b
        Edge be1 = new LateralEdge(b1,b2);  edgeListB.add(be1);
        Edge be2 = new LateralEdge(b2,b3);  edgeListB.add(be2);

        //build cell

    }

    //@Test
  //  void check_that_number_of_nodes_consistent_with_cell_creation(){
  //      assertEquals(6, testCell.getNodes().size());
   // }

    @Test
    void check_that_nodes_consistent_during_cell_creation(){
        for(Node2D node: cellNodes){
            int numberOfCopiesOfNode = 0;
            //for(Node cellNode: testCell.getNodes()){
            //    if(node == cellNode) numberOfCopiesOfNode++;
            //}
            assertEquals(1, numberOfCopiesOfNode);
        }
    }
}
