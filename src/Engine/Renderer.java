package Engine;

import Engine.States.State;
import GUI.DisplayWindow;
import Utilities.Geometry.Vector2i;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Renderer implements Runnable
{
    private static Renderer instance;

    /**
     * Graphics object that our painter class references to draw objects
     */
    private static Graphics g;
    public static final Color defaultColor = Color.white;
    DisplayWindow displayWindow;
    BufferStrategy bufferStrategy;
    String title;
    int width, height;

    /**
     * Used to generate a singleton instance of our Renderer.
     * @return the current Renderer, or create and return a new renderer if it is currently null.
     */
    public static Renderer getInstance()
    {
        if(instance == null)
        {
            instance = new Renderer();
        }
        return instance;
    }
    private Renderer()
    {
        width = Simulation.bounds.x;
        height = Simulation.bounds.y;
        title = Simulation.title;
        displayWindow = new DisplayWindow(title, width, height);
    }

    /**
     * Renders graphics to the screen. Should only be accessed from the Simulation object.
     */
    @Override
    public void run()
    {
        bufferStrategy = displayWindow.GetCanvas().getBufferStrategy();
        if(bufferStrategy == null)
        {
            displayWindow.GetCanvas().createBufferStrategy(3);
            return;
        }
        g = bufferStrategy.getDrawGraphics();

        g.clearRect(0,0,width,height);

        try {
            if(State.GetState() != null)
            {
                State.GetState().Render();

            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        bufferStrategy.show();
        g.dispose();


    }

    public DisplayWindow GetDisplayWindow()
    {
        return displayWindow;
    }

    public void setColor(Color color){
        if(g.getColor()==color) return;
        g.setColor(color);
    }

    public void drawCircle(Vector2i center, int radius){
        g.drawOval(center.x - radius/2, center.y - radius/2, radius, radius);
    }

    public void drawLine(Vector2i pointA, Vector2i pointB){
        g.drawLine(pointA.x, pointA.y, pointB.x, pointB.y);
    }

}
