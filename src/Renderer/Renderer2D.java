package Renderer;

import Framework.Engine;
import Framework.States.State;
import Renderer.Graphics.DisplayWindow;
import Utilities.Geometry.Vector.Vector2i;

import java.awt.*;
import java.awt.image.BufferStrategy;


public class Renderer2D extends Renderer
{
    DisplayWindow displayWindow;
    BufferStrategy bufferStrategy;
    String title;

    public Renderer2D()
    {
        title = Engine.title;
        bounds = new Dimension(800,800);
        displayWindow = new DisplayWindow(title, bounds.width, bounds.height);
        camera = new Camera(bounds.width, bounds.height);
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

        g.clearRect(0,0, camera.width, camera.height);

        try {
            if(State.GetState() != null)
            {
                State.GetState().Render();

            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        bufferStrategy.show();
        displayWindow.GetCanvas().prepareImageForExport();
        g.dispose();
    }

    @Override
    public void clearAllEvents(){
        camera.clearAllEvents();
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
        Vector2i transformedCenter = camera.transformToView(center);
        int transformedDiameter = Math.round(diameter* camera.getScale());
        g.drawOval(transformedCenter.x - transformedDiameter/2, transformedCenter.y - transformedDiameter/2, transformedDiameter, transformedDiameter);
    }

    public void drawLine(Vector2i pointA, Vector2i pointB){
        Vector2i transformedPointA = camera.transformToView(pointA);
        Vector2i transformedPointB = camera.transformToView(pointB);
        g.drawLine(transformedPointA.x, transformedPointA.y, transformedPointB.x,transformedPointB.y);
    }


}
