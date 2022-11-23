package Morphogenesis.Components;

import Framework.Object.Component;
import Framework.Object.Annotations.DoNotExposeInGUI;
import Framework.Object.Entity;
import Input.InputEvents;
import Input.SelectionEvents;
import Morphogenesis.Components.Meshing.Mesh;
import Renderer.Renderer;
import Utilities.Geometry.Vector.Vector2i;
import javafx.scene.input.MouseButton;

import java.awt.event.MouseEvent;

@DoNotExposeInGUI
public class MouseSelector extends Component {

    private static boolean alt = false;
    private static boolean shiftKey = false;
    @Override
    public void awake() {
        InputEvents.onClick.subscribe(this::onMouseClicked);
        InputEvents.onPress.subscribe(this::onMousePressed);
        InputEvents.onDrag.subscribe(this::onMouseDragged);
        InputEvents.onRelease.subscribe(this::onMouseReleased);
        InputEvents.onAlt.subscribe(this::setAlt);
        InputEvents.onShiftKey.subscribe(this::setShiftModifier);
    }

    public void setAlt(boolean _alt){
        alt = _alt;
    }

    public void setShiftModifier(boolean _shift){
        if(_shift) {
            SelectionEvents.beginSelectingMultiple();
            shiftKey = true;
        }
        else {
            SelectionEvents.cancelSelectingMultiple();
            shiftKey = false;
        }
    }

    /**
     * Left-click: get mouse position based on screenpoint/ camera
     * @param e mouse event
     */
    private void onMouseClicked(MouseEvent e){
        assert Renderer.getCamera() != null;
        Vector2i mousePosition = Renderer.getCamera().getScreenPoint(new Vector2i(e.getX(), e.getY()));
        if(e.getButton() == MouseEvent.BUTTON1) {
            if(shiftKey) SelectionEvents.beginSelectingMultiple();
            if(alt) deselectEntity(mousePosition);
            else selectEntity(mousePosition);
        }else if(e.getButton() == MouseEvent.BUTTON3){
            deselectEntity(mousePosition);
        }
    }

    private void onMousePressed(MouseEvent e){
        onMouseClicked(e);
        SelectionEvents.beginSelectingMultiple();
    }

    private void onMouseDragged(MouseEvent e){
        assert Renderer.getCamera() != null;
        Vector2i mousePosition = Renderer.getCamera().getScreenPoint(new Vector2i(e.getX(), e.getY()));
        if(alt || e.getButton() == MouseEvent.BUTTON3) deselectEntity(mousePosition);
        else selectEntity(mousePosition);
    }

    private void onMouseReleased(MouseEvent e){
        SelectionEvents.cancelSelectingMultiple();
    }

    /**
     * Select an entity whose mesh contains a given point. Return this object's parent if none exists.
     * @param mousePosition derived mouse position from cursor location and camera state
     */
    private void selectEntity(Vector2i mousePosition) {
        Entity selected = getComponent(Mesh.class).returnCellContainingPoint(mousePosition.asFloat());
        if(selected == parent) selected = getComponent(Yolk.class).checkSelection(mousePosition.asFloat());
        SelectionEvents.selectEntity(selected);
    }

    private void deselectEntity(Vector2i mousePosition){
        Entity selected = getComponent(Mesh.class).returnCellContainingPoint(mousePosition.asFloat());
        if(selected == parent) selected = getComponent(Yolk.class).checkSelection(mousePosition.asFloat());
        SelectionEvents.deselectEntity(selected);
    }

    @Override
    public void onDestroy() {
        InputEvents.onClick.unSubscribe(this::onMouseClicked);
        InputEvents.onPress.unSubscribe(this::onMousePressed);
        InputEvents.onDrag.unSubscribe(this::onMouseDragged);
        InputEvents.onRelease.unSubscribe(this::onMouseReleased);
        InputEvents.onAlt.unSubscribe(this::setAlt);
    }
}
