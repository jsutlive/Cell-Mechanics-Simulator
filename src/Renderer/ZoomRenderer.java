package Renderer;

import Framework.Engine;
import Framework.Events.IEvent;
import Framework.States.State;
import Input.InputEvents;
import Input.InputPanel;
import Renderer.Graphics.DisplayWindow;
import Utilities.Geometry.Vector.Vector2i;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class ZoomRenderer extends Renderer
{
    DisplayWindow displayWindow;
    BufferStrategy bufferStrategy;
    String title;
    public int width, height;
    public Vector2i shift;
    public float scale;

    public ZoomRenderer()
    {
        width = Engine.bounds.x;
        height = Engine.bounds.y;
        title = Engine.title;
        shift = new Vector2i(0);
        scale = 1f;
        displayWindow = new DisplayWindow(title, width, height);

        IEvent<Vector2i> shiftEventSink = this::setShift;
        IEvent<Float> scaleEventSink = this::setScale;

        InputEvents.onShift.subscribe(shiftEventSink);
        InputEvents.onScale.subscribe(scaleEventSink);
    }

    public void setScale(float newScale)
    {
        scale *= newScale;
    }

    public float getScale(){
        return scale;
    }

    public void setShift(Vector2i newShift)
    {
        shift.add(newShift);
    }

    public Vector2i getShift(){
        return shift;
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
        displayWindow.GetCanvas().prepareImageForExport();
        g.dispose();


    }

    @Override
    public void clearAllEvents(){
        InputEvents.onShift.close();
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

    @Override
    public Vector2i adjustMousePositionToCameraView(Vector2i pos){
        return new Vector2i(Math.round(((pos.x-width/2f)/scale)+width/2f-shift.x),Math.round(((pos.y-height/2f)/scale)+height/2f-shift.y));
    }

}
