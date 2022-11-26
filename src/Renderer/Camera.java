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

    /**
     * Returns pixel viewing coordinates of a data (world) position
     * @param pos
     * @return
     */
    public Vector2i transformToView(Vector2i pos){
        return new Vector2i(
                Math.round((pos.x + shift.x) * scale + width/2f),
                Math.round((pos.y + shift.y) * scale + height/2f)
        );
    }

    /** Returns data (world) coordinates from given pixel location
     *
     * @param pos
     * @return
     */
    public Vector2i getScreenPoint(Vector2i pos){
        return new Vector2i(
                Math.round((pos.x-width/2f)/scale - shift.x),
                Math.round((pos.y-height/2f)/scale - shift.y)
        );
    }


    public void clearAllEvents(){
        InputEvents.onShift.close();
    }
}
