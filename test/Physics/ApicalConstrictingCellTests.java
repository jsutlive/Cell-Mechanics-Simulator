package Physics;

import Model.Cells.ApicalConstrictingCell;
import Model.Cells.Cell;
import Physics.Rigidbodies.ApicalEdge;
import Physics.Rigidbodies.Edge;
import Utilities.Geometry.Vector2f;
import Utilities.Geometry.Vector2i;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApicalConstrictingCellTests {

    static List<Cell> cells = new ArrayList<>();

    @BeforeAll
    static void setup() {

    }

/*
    @ParameterizedTest
    @MethodSource("getValues")
    public void testConstictingCellForcesAreEqualBetweenOppositeCells(int val)
    {
        List<Cell> testCells = returnCellsFromRingLocation(val);
        if(testCells.size() != 2) throw new IllegalArgumentException("Must have two cells per ring location, instead have " + testCells.size());
        Cell a = testCells.get(0);
        Cell b = testCells.get(1);
        Vector2f a_1_force = new Vector2f();
        Vector2f a_2_force = new Vector2f();
        Vector2f b_1_force = new Vector2f();
        Vector2f b_2_force = new Vector2f();

        if(a instanceof ApicalConstrictingCell)
        {
            ApicalConstrictingCell apicalA = (ApicalConstrictingCell)a;
            apicalA.constrictApicalEdge();
            for(Edge e: a.getEdges())
            {
                if(e instanceof ApicalEdge){
                    a_1_force = e.getNodes()[0].getResultantForce();
                    a_2_force = e.getNodes()[1].getResultantForce();
                }
            }
        }
        else throw new IllegalArgumentException("Cell a not instance of apical constricting cell");
        if(b instanceof ApicalConstrictingCell)
        {
            ApicalConstrictingCell apicalB = (ApicalConstrictingCell)a;
            apicalB.constrictApicalEdge();
            for(Edge e: b.getEdges())
            {
                if(e instanceof ApicalEdge){
                    b_1_force = e.getNodes()[0].getResultantForce();
                    b_2_force = e.getNodes()[1].getResultantForce();
                }
            }

        }
        else throw new IllegalArgumentException("Cell b not instance of apical constricting cell");

        assertEquals(Math.abs(a_1_force.x), Math.abs(b_2_force.x));
        assertEquals(Math.abs(a_1_force.y), Math.abs(b_2_force.y));
        assertEquals(Math.abs(a_2_force.x), Math.abs(b_1_force.x));
        assertEquals(Math.abs(a_2_force.x), Math.abs(b_1_force.x));

    }

    private List<Cell> returnCellsFromRingLocation(int loc)
    {
        List<Cell> returnedCells = new ArrayList<>();
        for (Cell cell: cells)
        {
            if(cell.getRingLocation() == loc) returnedCells.add(cell);
        }
        return returnedCells;
    }

    private static int[] getValues()
    {
        return new int[]{1,2,3,4,5,6,7,8,9,10};
    }*/

 // class RepeatedNestedTest()
 // {
 //     void foo() {
 //         for (Cell cell : cells) {
 //             for (Node node : cell.getNodes()) node.Move();
 //         }
 //     }

 // }

}
