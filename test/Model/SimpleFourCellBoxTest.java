package Model;

import Model.Organisms.SimpleFourCell;
import Physics.Rigidbodies.ApicalEdge;
import Physics.Rigidbodies.Edge;
import Physics.Rigidbodies.Node;
import Utilities.Math.CustomMath;
import Utilities.Model.Builder;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleFourCellBoxTest
{
    @Test
    void equal_forces_added_on_opposite_sides() throws IllegalAccessException, InstantiationException {
        List<Cell> cells = Builder.getSimpleFourCellBox();
        Cell a = cells.get(1);
        Cell b = cells.get(2);

        Node nodeA = new Node(0,0), nodeB = new Node(0,0);
        for(Edge edge: a.edges)
        {
            if(edge instanceof ApicalEdge) nodeA = edge.getNodes()[0];
        }
        for(Edge edge: b.edges)
        {
            if(edge instanceof ApicalEdge) nodeB = edge.getNodes()[1];
        }

        for(Cell cell: cells)
        {
            for(Edge edge: cell.edges) {
                edge.constrict(cell.elasticConstant, cell.elasticRatio);
                if(edge instanceof ApicalEdge)
                {
                    edge.constrict(cell.constant, cell.ratio);
                }
            }
            for(Edge edge: cell.internalEdges) edge.constrict(cell.internalConstant, cell.elasticRatio);
        }

        assertEquals(CustomMath.abs(nodeA.getResultantForce().x), CustomMath.abs(nodeB.getResultantForce().x));
        assertEquals(CustomMath.abs(nodeA.getResultantForce().y), CustomMath.abs(nodeB.getResultantForce().y));

    }

}