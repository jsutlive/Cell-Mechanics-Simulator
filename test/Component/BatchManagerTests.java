package Component;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BatchManagerTests {
    @Test
    void check_if_class_can_be_returned_from_string() throws ClassNotFoundException {
        String className = "Component.BatchManagerTests";
        Class c = Class.forName(className);
        assertNotNull(c);
    }
}
