package Engine;

import Utilities.Geometry.Vector2i;
import java.awt.*;

public abstract class Renderer implements Runnable{
    //Graphics object that our painter class references to draw objects
    protected static Graphics g;

    //Default color by which to paint objects in a scene
    public static final Color defaultColor = Color.white;

    //Renderer object singleton instance.
    private static Renderer instance;

    /**
     * Used to generate a singleton instance of our Renderer.
     * @return the current Renderer, or create and return a new renderer if it is currently null.
     */
    public static Renderer getInstance()
    {
        if(instance == null)
        {
            // by default, we load the simpleRenderer
            // change the Renderer type here, preferably using the same "makeInstance" convention.

            /*
                Example "makeInstance" function:
                 public static Renderer makeInstance(){
                    if (getInstance()!= null) return getInstance();         // maintain singleton renderer
                    return new ExampleRenderer();                           // create the renderer instance
                }
             */
            instance = ZoomRenderer.makeInstance();
        }
        return instance;
    }

    public static Boolean hasInstance(){
        return (instance != null);
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
}