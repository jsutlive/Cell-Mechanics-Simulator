package Math;

import Utilities.Geometry.Vector2f;
import Utilities.Math.Gauss;
import org.junit.jupiter.api.Test;
import sun.jvm.hotspot.utilities.Assert;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GaussTests {

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
}
