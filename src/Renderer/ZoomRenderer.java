package Renderer;

import Engine.Simulation;
import Engine.States.State;
import GUI.DisplayWindow;
import Utilities.Geometry.Vector.Vector2i;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class ZoomRenderer extends Renderer
{
    DisplayWindow displayWindow;
    BufferStrategy bufferStrategy;
    String title;
    int width, height;
    public Vector2i shift;
    public float scale;

    public ZoomRenderer()
    {
        width = Simulation.bounds.x;
        height = Simulation.bounds.y;
        title = Simulation.title;
        shift = new Vector2i(0);
        scale = 1f;
        displayWindow = new DisplayWindow(title, width, height);
    }

    public void setScale(float newScale)
    {
        scale = newScale;
    }

    public float getScale(){
        return scale;
    }

    public void setShift(Vector2i newShift)
    {
        shift = newShift;
    }

    public Vector2i getShift(){
        return shift;
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
        Vector2i transformedCenter = transform(center);
        int transformedDiameter = Math.round(diameter*scale);
        g.drawOval(transformedCenter.x - transformedDiameter/2, transformedCenter.y - transformedDiameter/2, transformedDiameter, transformedDiameter);
    }

    public void drawLine(Vector2i pointA, Vector2i pointB){
        Vector2i transformedPointA = transform(pointA);
        Vector2i transformedPointB = transform(pointB);
        g.drawLine(transformedPointA.x, transformedPointA.y, transformedPointB.x,transformedPointB.y);
    }

    private Vector2i transform(Vector2i point)
    {
        return new Vector2i(Math.round((point.x + shift.x - width/2f) * scale + width/2f),Math.round((point.y + shift.y - height/2f) * scale + height/2f));
    }

}
