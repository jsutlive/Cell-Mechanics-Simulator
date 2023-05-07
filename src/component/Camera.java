package component;

import framework.object.annotations.DoNotDestroyInGUI;
import framework.object.Tag;
import utilities.geometry.Vector.Vector2f;
import utilities.geometry.Vector.Vector2i;

import java.awt.event.MouseEvent;
import static input.InputEvents.*;

/**
 * Camera: main interface for determining what will be visible in the canvas. Exists as a behavior on its own object
 *
 * Copyright (c) 2023 Joseph Sutlive and Tony Zhang
 * All rights reserved
 */
@DoNotDestroyInGUI
public class Camera extends Component {
    // Primary camera for rendering
    public static Camera main;

    // Camera field of view
    private final int width = 800, height = 800;

    // X - Y offset
    private Vector2i shift = new Vector2i(0);

    public void setShift(Vector2i newShift) {
        shift.add(newShift);
        getComponent(Transform.class).position = shift.asFloat();
    }

    public Vector2i getShift(){
        return shift;
    }

    private void overrideShift(Vector2f vec){
        shift = vec.asInt();
    }


    // Zoom
    public float scale = 1f;

    public void setScale(float newScale)
    {
        scale *= newScale;
    }

    public float getScale(){
        return scale;
    }

    private boolean isMovingCamera;
    private Vector2i mouseStartingClickPosition = new Vector2i();

    @Override
    public void awake() {
        // Default: set camera as "main camera", tag object as a camera
        if(main == null) main = this;
        if(parent.getTag()!= Tag.CAMERA) parent.addTag(Tag.CAMERA);

        //Subscribe to events
        getComponent(Transform.class).onPositionChanged.subscribe(this::overrideShift);
        onShift.subscribe(this::setShift);
        onScale.subscribe(this::setScale);
        onPress.subscribe(this::mousePressed);
        onRelease.subscribe(this::mouseReleased);
        onDrag.subscribe(this::mouseDragged);
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

    /**
     * Returns pixel viewing coordinates of a data (world) position
     * @param pos scene (engine/data) position of an object (int)
     * @return pixel/viewport coordinates of the object
     */
    public Vector2i transformToView(Vector2i pos){
        return new Vector2i(
                Math.round((pos.x + shift.x) * scale + width/2f),
                Math.round((pos.y + shift.y) * scale + height/2f)
        );
    }

    /**
     * Returns pixel viewing coordinates of a data (world) position
     * @param pos scene (engine/data) position of an object (float)
     * @return pixel/viewport coordinates of the object
     */
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
        onShift.unSubscribe(this::setShift);
        onScale.unSubscribe(this::setScale);
        onPress.unSubscribe(this::mousePressed);
        onRelease.unSubscribe(this::mouseReleased);
        onDrag.unSubscribe(this::mouseDragged);
        getComponent(Transform.class).onPositionChanged.unSubscribe(this::overrideShift);

    }
}
