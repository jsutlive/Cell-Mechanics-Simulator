package Renderer;

import Utilities.Geometry.Vector.Vector2f;
import Utilities.Geometry.Vector.Vector2i;
import java.awt.*;

public abstract class Renderer implements Runnable {
    //Renderer.Graphics object that our painter class references to draw objects
    public static Graphics g;

    //Renderer object singleton instance.
    private static Renderer instance;

    protected static Dimension bounds;

    protected Camera camera;

    /**
     * Used to generate a singleton instance of our Renderer.
     * @return the current Renderer, or create and return a new renderer if it is currently null.
     */
    public static Renderer getInstance() {
        if(instance == null)
        {
            instance = build(Renderer2D.class);
        }
        return instance;
    }
    public static Vector2i windowSize(){
        return new Vector2i(bounds.width, bounds.height);
    }

    public static Camera getCamera(){
        if(instance == null) return null;
        else return instance.camera;
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
    // Draw any circular objects or points
    public abstract void drawCircle(Vector2i center, int diameter);

    // Draw any lines
    public abstract void drawLine(Vector2i pointA, Vector2i pointB);

    // Set the renderer color
    public abstract  void setColor(Color color);

    public void clearAllEvents(){}
}