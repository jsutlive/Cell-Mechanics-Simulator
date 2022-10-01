package Morphogenesis.Components.Physics.Forces;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class GradientTests {

    @Test
    void linear_gradient_constant_step_calculation_is_2_when_5_steps_between_10_and_2(){
        LinearGradient lg = new LinearGradient();
        lg.calculate(10, 10,1);
        assertEquals(2.5f, lg.getConstantStep());
    }

    @Test
    void gradient_applied_consistently_across_cells_in_gradient_that_center_has_highest_constant(){
        GaussianGradient gg = new GaussianGradient(0f, .5f);
        gg.calculate(30,
                10f, .01f,
                2f, .0001f);
        System.out.println(gg.constants[1]);
        System.out.println(gg.constants[2]);
        for (int i = 1; i < gg.constants.length; i++) {
            assertNotEquals(gg.constants[i - 1], gg.constants[i]);
            assertTrue(gg.constants[i - 1] >= gg.constants[i]);
        }
    }
}
