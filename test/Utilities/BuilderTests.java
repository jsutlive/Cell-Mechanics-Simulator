package Utilities;

import Model.Cell;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.LateralEdge;
import Physics.Rigidbodies.Node;
import Utilities.Model.Builder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BuilderTests {
    static List<Node> nodes = new ArrayList<>();
    static List<Edge> edgeListA = new ArrayList<>();
    static List<Edge> edgeListB = new ArrayList<>();

    @BeforeAll
    static void edge_and_node_setup(){
        Node a1 = new Node(0,0);    nodes.add(a1);
        Node a2 = new Node(0,1);    nodes.add(a2);
        Node a3 = new Node(0,2);    nodes.add(a3);
        Node b1 = new Node(1,0);    nodes.add(b1);
        Node b2 = new Node(1,1);    nodes.add(b2);
        Node b3 = new Node(1, 2);   nodes.add(b3);

        //create 4 lateral edges, two on each side
        //side a
        Edge ae1 = new LateralEdge(a1,a2);  edgeListA.add(ae1);
        Edge ae2 = new LateralEdge(a2,a3);  edgeListA.add(ae2);

        //side b
        Edge be1 = new LateralEdge(b1,b2);  edgeListB.add(be1);
        Edge be2 = new LateralEdge(b2,b3);  edgeListB.add(be2);
    }

    @Test
    void check_if_nodes_present_when_compiling_nodes_from_edges(){
        List<Node> testNodesList = new ArrayList<>();
        Builder.getVerticesFromEdgeSegments(edgeListA, edgeListB, testNodesList);
        for(Node node: nodes){
            int numberOfCopiesInEdgeLists = 0;
            for(Node testNode: testNodesList){
                if(node == testNode) numberOfCopiesInEdgeLists++;
            }
            assertEquals(1, numberOfCopiesInEdgeLists);
        }
    }
}
