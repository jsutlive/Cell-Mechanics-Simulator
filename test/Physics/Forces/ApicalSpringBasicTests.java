package Physics.Forces;

import Model.SimpleFourCell;
import Physics.Bodies.Cell.CellEdge;
import Physics.Bodies.Edge;
import Physics.Bodies.PhysicsBody;
import Physics.Forces.Springs.SimpleApicalSpring;
import Physics.Forces.Springs.Spring;
import Utilities.Geometry.Vector2f;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApicalSpringBasicTests
{
    @Test
    void ensure_two_cells_each_with_negative_sign_using_a_simple_four_cell_organism()
    {
        SimpleFourCell fourCell = new SimpleFourCell();
        fourCell.generateOrganism();
        SimpleApicalSpring spring = SimpleApicalSpring.configureNew(1f, .5f);
        spring.attach(fourCell.allCells);
        int count = 0;
        for(PhysicsBody pb : spring.listeners)
        {
            CellEdge e = (CellEdge) pb;
            int id = e.getCell().getID();
            if(id < 2 ) count++;
        }
        assertEquals(2, count);
    }

    @Test
    void ensure_the_x_component_of_force_is_equal_and_opposite_in_cell_0_and_cell_3_in_four_cell_organism()
    {
        SimpleFourCell fourCell = new SimpleFourCell();
        fourCell.generateOrganism();
        SimpleApicalSpring spring = SimpleApicalSpring.configureNew(1f, .5f);
        spring.attach(fourCell.allCells.getCell(0));
        spring.attach(fourCell.allCells.getCell(3));

        int count = 0;
        Vector2f forceExpected = new Vector2f();
        Vector2f forceActual = new Vector2f();
        for(PhysicsBody body: spring.getListeners())
        {
            int sign =-1;
            CellEdge edge = (CellEdge) body;
            int id = edge.getCell().getID();
            if(id< 2) sign *= -1;
            float f = sign * getForceMagnitude(edge, spring);
            //Vector2f force = new Vector2f(f);
            Vector2f force = new Vector2f(f * edge.getXLength()/edge.getLength(), f* edge.getYLength()/edge.getLength());
            if(count == 0) forceExpected = force;
            else forceActual = force;
            count++;
        }

        assertEquals(forceExpected.x, -1 * forceActual.x);

    }

    private float getForceMagnitude(CellEdge edge, Spring spring) {
        float f = spring.getConstant() * (spring.getLength() - (spring.getRatio() * edge.initialLength));
        return f;
    }
}
