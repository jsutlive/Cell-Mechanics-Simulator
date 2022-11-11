package Renderer;

import Framework.Events.IEvent;
import Input.InputEvents;
import Utilities.Geometry.Vector.Vector2i;

public class Camera {
    public int width, height;
    public Vector2i shift;
    public float scale;

    public Camera(int width, int height){
        this.width = width;
        this.height = height;
        scale = 1f;
        shift = new Vector2i(0);

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

    public Vector2i transformToView(Vector2i pos){
        return new Vector2i(
                Math.round((pos.x + shift.x - width/2f) * scale + width/2f),
                Math.round((pos.y + shift.y - height/2f) * scale + height/2f)
        );

    }

    public Vector2i getScreenPoint(Vector2i pos){
        return new Vector2i(
                Math.round(((pos.x-width/2f)/scale)+width/2f-shift.x),
                Math.round(((pos.y-height/2f)/scale)+height/2f-shift.y)
        );
    }

    public void clearAllEvents(){
        InputEvents.onShift.close();
    }
}
