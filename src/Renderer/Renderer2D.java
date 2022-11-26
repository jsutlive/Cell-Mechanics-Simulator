package Renderer;

import Renderer.Graphics.DisplayWindow;
import Utilities.Geometry.Vector.Vector2i;

import java.awt.*;
import java.awt.image.BufferStrategy;


public class Renderer2D extends Renderer
{
    DisplayWindow displayWindow;
    BufferStrategy bufferStrategy;

    public Renderer2D()
    {
        bounds = new Dimension(800,800);
        displayWindow = new DisplayWindow(windowTitle, bounds.width, bounds.height);
        camera = new Camera(bounds.width, bounds.height);
    }
    /**
     * Renders graphics to the screen. Should only be accessed from the Engine object.
     */
    @Override
    public void render()
    {
        bufferStrategy = displayWindow.GetCanvas().getBufferStrategy();
        if(bufferStrategy == null)
        {
            displayWindow.GetCanvas().createBufferStrategy(3);
            return;
        }
        g = bufferStrategy.getDrawGraphics();

        g.clearRect(0,0, camera.width, camera.height);


        for(int i = batch.size() -1; i >= 0; i--){
            batch.get(i).render();
        }

        bufferStrategy.show();
        displayWindow.GetCanvas().prepareImageForExport();
        g.dispose();
    }

    @Override
    public void clearAllEvents(){
        camera.clearAllEvents();
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
