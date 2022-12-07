package Renderer;

import Framework.Object.Component;
import Framework.Object.Tag;
import Input.InputEvents;
import Utilities.Geometry.Vector.Vector2i;

public class Camera extends Component {
    public static Camera main;
    private int width = 800, height = 800;
    public Vector2i shift = new Vector2i(0);
    public float scale = 1f;

    @Override
    public void awake() {
        if(main == null) main = this;
        if(parent.getTag()!= Tag.CAMERA) parent.addTag(Tag.CAMERA);
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

    @Override
    public void onDestroy() {
        clearAllEvents();
    }

    public void clearAllEvents(){
        InputEvents.onShift.close();
        InputEvents.onScale.close();
    }
}
