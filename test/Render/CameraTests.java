package Render;

import Framework.Object.Entity;
import Component.Camera;
import Utilities.Geometry.Vector.Vector2i;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CameraTests {

    static Camera camera;

    @BeforeAll
    static void setup(){
        Entity e = new Entity().with(new Camera());
        camera = e.getComponent(Camera.class);
    }

    @Test
    void test_camera_at_origin_base_mag(){
        Vector2i newPoint = camera.transformToView(new Vector2i(0,0));
        assertEquals(400, newPoint.x);
        assertEquals(400, newPoint.y);
    }
}
