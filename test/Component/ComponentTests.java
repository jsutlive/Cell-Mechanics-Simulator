package Component;

import Component.Component;
import Component.RingMesh;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ComponentTests {

    @Test
    void verify_component_recognized_during_adding_components(){
        assertTrue(Component.class.isAssignableFrom(RingMesh.class));
    }
}
