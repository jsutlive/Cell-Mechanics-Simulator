package GeneralPhysics;

import Framework.Object.Component;
import Morphogenesis.Render.ObjectRenderer;
import java.awt.Color;

public class TestComponent extends Component {

    /**
     * Test variables placed here
     */
    public float foo = 5f;
    public float bar = 0f;
    public float fooOnUpdate = 0f;
    public Color destroyedColor = Color.GREEN;

    //only public variables seen in the GUI
    private float invisibleFoo = 3f;

    // Use the awake method to perform an action once when the object is created
    @Override
    public void awake() {
        bar = 2 * foo;
    }

    // Use the update method to perform an action once per physics cycle after the simulation starts
    @Override
    public void update() {
        fooOnUpdate += foo;
    }

    // Use the onDestroy method to perform an action when the component is removed.
    @Override
    public void onDestroy() {
        getComponent(ObjectRenderer.class).setColor(destroyedColor);
    }
}
