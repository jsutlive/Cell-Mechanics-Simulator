package Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleFourCellTest
{
    /**
     * A simple four-cell model should always return four cells when asked to return all cells.
     */
    @Test
    void total_cells_should_equal_four() throws InstantiationException, IllegalAccessException {
        SimpleFourCell model = new SimpleFourCell();
        model.generateOrganism();

        assertEquals(4, model.getAllCells().size());
    }

    @Test
    void two_cells_each_should_have_loc_ids_of_1_and_2() throws InstantiationException, IllegalAccessException {
        SimpleFourCell model = new SimpleFourCell();
        model.generateOrganism();
        int count1 = 0;
        int count2 = 0;
        for(Cell cell: model.getAllCells())
        {
            if(cell.getRingLocation() == 1) count1++;
            if(cell.getRingLocation() == 2) count2++;
        }

        assertEquals(2, count1);
        assertEquals(2, count2);
    }
}
