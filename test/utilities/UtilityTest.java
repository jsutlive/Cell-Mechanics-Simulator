package utilities;

import framework.data.FileBuilder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UtilityTest {
    @Test
    public void check_string_array_appended_properly(){
        String[] s = new String[]{
                "Bob",
                "Larry",
                "Junior"
        };
        String append = "TEST";
        s = FileBuilder.appendStringArray(s, append);
        assertEquals(append, s[3]);
    }
}
