package Renderer;

import Engine.Simulation;
import Engine.States.State;
import GUI.DisplayWindow;
import Utilities.Geometry.Vector2i;

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