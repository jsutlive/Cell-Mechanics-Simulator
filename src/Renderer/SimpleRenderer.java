package Renderer;

import Framework.Engine;
import Framework.States.State;
import Renderer.Graphics.DisplayWindow;
import Utilities.Geometry.Vector.Vector2i;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class SimpleRenderer extends Renderer
{
    DisplayWindow displayWindow;
    BufferStrategy bufferStrategy;
    String title;
    int width, height;

    public SimpleRenderer()
    {
        width = Engine.bounds.x;
        height = Engine.bounds.y;
        title = Engine.title;
        displayWindow = new DisplayWindow(title, width, height);
    }

    /**
     * Renders graphics to the screen. Should only be accessed from the Engine object.
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
        } catch (InstantiationException | IllegalAccessException e) {
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

    public void drawCircle(Vector2i center, int diameter){
        g.drawOval(center.x - diameter/2, center.y - diameter/2, diameter, diameter);
    }

    public void drawLine(Vector2i pointA, Vector2i pointB){
        g.drawLine(pointA.x, pointA.y, pointB.x, pointB.y);
    }

}
