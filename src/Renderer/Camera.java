package Renderer;

import Framework.Object.Component;
import Framework.Object.Tag;
import Framework.Object.Transform;
import Input.InputEvents;
import Renderer.UIElements.SetSlider;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Geometry.Vector.Vector2i;

import java.awt.*;
import java.awt.event.MouseEvent;

import static Renderer.Renderer.graphics;

public class Camera extends Component {
    public static Camera main;
    private int width = 800, height = 800;
    private Vector2i shift = new Vector2i(0);
    @SetSlider(min = 0.1f, max = 2)
    public float scale = 1f;
    private boolean isMovingCamera;
    private Vector2i mouseStartingClickPosition = new Vector2i();

    @Override
    public void awake() {
        if(main == null) main = this;
        if(parent.getTag()!= Tag.CAMERA) parent.addTag(Tag.CAMERA);
        getComponent(Transform.class).onPositionChanged.subscribe(this::overrideShift);
        InputEvents.onShift.subscribe(this::setShift);
        InputEvents.onScale.subscribe(this::setScale);
        InputEvents.onPress.subscribe(this::mousePressed);
        InputEvents.onRelease.subscribe(this::mouseReleased);
        InputEvents.onDrag.subscribe(this::mouseDragged);
    }

    private void overrideShift(Vector2f vec){
        shift = vec.asInt();
    }

    private void mousePressed(MouseEvent e){
        if(e.getButton() == MouseEvent.BUTTON2){
            isMovingCamera = true;
            mouseStartingClickPosition = Camera.main.getScreenPoint(new Vector2i(e.getX(), e.getY()));
        }
    }

    private void mouseDragged(MouseEvent e){
        if(isMovingCamera){
            Vector2i mousePos = Camera.main.getScreenPoint(new Vector2i(e.getX(), e.getY()));
            Vector2i shift = mousePos.sub(mouseStartingClickPosition);
            setShift(shift);
        }
    }

    private void mouseReleased(MouseEvent e){
        if(e.getButton() == MouseEvent.BUTTON2){
            isMovingCamera = false;
        }
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
        getComponent(Transform.class).position = shift.asFloat();
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

    public Vector2i transformToView(Vector2f pos){
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
        InputEvents.onPress.unSubscribe(this::mousePressed);
        InputEvents.onRelease.unSubscribe(this::mouseReleased);
        InputEvents.onDrag.unSubscribe(this::mouseDragged);
        getComponent(Transform.class).onPositionChanged.unSubscribe(this::overrideShift);
    }
}
