package Physics;

import Model.ApicalConstrictingCell;
import Model.Cell;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector2f;
import Utilities.Geometry.Vector2i;
import Utilities.Model.Builder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;

public class ApicalConstrictingCellTests {

    static List<Cell> cells = new ArrayList<>();

    @BeforeAll
    static void setup() throws IllegalAccessException, InstantiationException {
        cells = Builder.getCellRing(
                80,
                2,
                100,
                300,
                new Vector2i(800));
    }


    @ParameterizedTest
    @MethodSource("getValues")
    public void testConstictingCellForcesAreEqualBetweenOppositeCells(int val)
    {
        List<Cell> testCells = returnCellsFromRingLocation(val);
        if(testCells.size() != 2) throw new IllegalArgumentException("Must have two cells per ring location, instead have " + testCells.size());
        Cell a = testCells.get(0);
        Cell b = testCells.get(1);

        if(a instanceof ApicalConstrictingCell)
        {
            ApicalConstrictingCell apicalA = (ApicalConstrictingCell)a;
            apicalA.constrictApicalEdge();
        }
        else throw new IllegalArgumentException("Cell a not instance of apical constricting cell");
        if(b instanceof ApicalConstrictingCell)
        {
            ApicalConstrictingCell apicalA = (ApicalConstrictingCell)a;
            apicalA.constrictApicalEdge();

        }
        else throw new IllegalArgumentException("Cell b not instance of apical constricting cell");


    }

    private void findApicalNode(){}

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
    }

 // class RepeatedNestedTest()
 // {
 //     void foo() {
 //         for (Cell cell : cells) {
 //             for (Node node : cell.getNodes()) node.Move();
 //         }
 //     }

 // }

}
