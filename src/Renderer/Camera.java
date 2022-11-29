package Renderer;

import Input.InputEvents;
import Utilities.Geometry.Vector.Vector2i;

public class Camera {
    public static Camera main;
    public int width, height;
    public Vector2i shift = new Vector2i(0);
    public float scale = 1f;

    public Camera(int width, int height){
        if(main == null) main = this;
        this.width = width;
        this.height = height;

        InputEvents.onShift.subscribe(this::setShift);
        InputEvents.onScale.subscribe(this::setScale);
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
     * @param pos scene (engine/data) position of an object
     * @return pixel/viewport coordinates of the object
     */
    public Vector2i transformToView(Vector2i pos){
        return new Vector2i(
                Math.round((pos.x + shift.x) * scale + width/2f),
                Math.round((pos.y + shift.y) * scale + height/2f)
        );
    }

    /**
     * Returns data (world) coordinates from given pixel location
     * @param pos pixel/viewport coordinate of an object
     * @return scene engine/data position of the object
     */
    public Vector2i getScreenPoint(Vector2i pos){
        return new Vector2i(
                Math.round((pos.x-width/2f)/scale - shift.x),
                Math.round((pos.y-height/2f)/scale - shift.y)
        );
    }

    public void clearAllEvents(){
        InputEvents.onShift.close();
        InputEvents.onScale.close();
    }
}
