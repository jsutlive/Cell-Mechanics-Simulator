package Renderer;

import Utilities.Geometry.Vector.Vector2i;
import java.awt.*;

public abstract class Renderer implements Runnable {
    //Renderer object singleton instance.
    private static Renderer instance;

    /**
     * Used to generate a singleton instance of our Renderer.
     * @return the current Renderer, or create and return a new renderer if it is currently null.
     */
    public static Renderer getInstance() {
        if(instance == null)
        {
            instance = build(RendererStdout.class);
        }
        return instance;
    }

     static <T extends Renderer> T build(Class<T> type) {
        try {
            return type.newInstance();
        } catch (IllegalAccessException | InstantiationException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    // run the renderer in an update loop
    public void run() {

    }

    public void clearAllEvents(){}
}