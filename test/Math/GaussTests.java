package Math;

import Utilities.Geometry.Vector2f;
import Utilities.Math.Gauss;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GaussTests {

    /**
     * We create a simple square and see if Gaussian shoelace method can accurately predict its area.
     * The coordinates must be given in clockwise/counter-clockwise order.
     */
    @Test
    void find_area_of_basic_square()
    {
        List<Vector2f> coords = new ArrayList<>();
        coords.add(new Vector2f(0, 0));
        coords.add(new Vector2f(0, 1));
        coords.add(new Vector2f(1, 1));
        coords.add(new Vector2f(1, 0));

        assertEquals(1, Gauss.shoelace(coords));
    }

    /**
     * We create a convex shape that appears like this:
     *       _______
     *      |  __  |
     *      |_| |_|
     * and determine if our shoelace method accurately predicts the area
     */
    @Test
    void find_area_of_convex_shape(){
        List<Vector2f> coords = new ArrayList<>();
        coords.add(new Vector2f(0,0));
        coords.add(new Vector2f(0, 2));
        coords.add(new Vector2f(3, 2));
        coords.add(new Vector2f(3,0));
        coords.add(new Vector2f(2, 0));
        coords.add(new Vector2f(2,1));
        coords.add(new Vector2f(1, 1));
        coords.add(new Vector2f(1,0));

        assertEquals(5, Gauss.shoelace(coords));
    }
}
