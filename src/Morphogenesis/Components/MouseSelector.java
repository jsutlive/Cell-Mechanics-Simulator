package Morphogenesis.Components;

import Framework.Object.Component;
import Framework.Object.Annotations.DoNotExposeInGUI;
import Framework.Object.Entity;
import Input.InputEvents;
import Input.SelectionEvents;
import Morphogenesis.Components.Meshing.Mesh;
import Renderer.Renderer;
import Utilities.Geometry.Vector.Vector2i;

import java.awt.event.MouseEvent;

@DoNotExposeInGUI
public class MouseSelector extends Component {

    @Override
    public void awake() {
        InputEvents.onClick.subscribe(this::onMouseClicked);
        InputEvents.onPress.subscribe(this::onMousePressed);
        InputEvents.onDrag.subscribe(this::onMouseDragged);
        InputEvents.onRelease.subscribe(this::onMouseReleased);
    }

    /**
     * Left-click: get mouse position based on screenpoint/ camera
     * @param e mouse event
     */
    private void onMouseClicked(MouseEvent e){
        if(e.getButton() == MouseEvent.BUTTON1) {
            assert Renderer.getCamera() != null;
            Vector2i mousePosition = Renderer.getCamera().getScreenPoint(new Vector2i(e.getX(), e.getY()));
            selectEntity(mousePosition);
        }
    }

    private void onMousePressed(MouseEvent e){
        onMouseClicked(e);
        SelectionEvents.beginSelectingMultiple();
    }

    private void onMouseDragged(MouseEvent e){
        assert Renderer.getCamera() != null;
        Vector2i mousePosition = Renderer.getCamera().getScreenPoint(new Vector2i(e.getX(), e.getY()));
        selectEntity(mousePosition);
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

    @Override
    public void onDestroy() {
        InputEvents.onClick.unSubscribe(this::onMouseClicked);
    }
}
