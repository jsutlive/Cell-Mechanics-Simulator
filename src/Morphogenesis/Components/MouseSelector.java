package Morphogenesis.Components;

import Framework.Object.Component;
import Framework.Object.Annotations.DoNotExposeInGUI;
import Framework.Object.Entity;
import Input.SelectionEvents;
import Morphogenesis.Components.Meshing.Mesh;
import Renderer.Renderer;
import Utilities.Geometry.Vector.Vector2f;
import Utilities.Geometry.Vector.Vector2i;

import java.awt.event.MouseEvent;

@DoNotExposeInGUI
public class MouseSelector extends Component {

    private static boolean alt = false;
    private static boolean shiftKey = false;
    private static boolean selecting = false;
    @Override
    public void awake() {

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
     * Select an entity whose mesh contains a given point. Return this object's parent if none exists.
     * @param mousePosition derived mouse position from cursor location and camera state
     */
    private void selectEntity(Vector2i mousePosition) {
        Entity selected = getComponent(Mesh.class).returnCellContainingPoint(mousePosition.asFloat());
        if(getComponent(Yolk.class)!= null) {
            if (selected == parent) selected = getComponent(Yolk.class).checkSelection(mousePosition.asFloat());
        }
        SelectionEvents.selectEntity(selected);
    }

    private void deselectEntity(Vector2i mousePosition){
        Entity selected = getComponent(Mesh.class).returnCellContainingPoint(mousePosition.asFloat());
        if(getComponent(Yolk.class)!= null) {
            if (selected == parent) selected = getComponent(Yolk.class).checkSelection(mousePosition.asFloat());
        }
        SelectionEvents.deselectEntity(selected);
    }

    @Override
    public void onDestroy() {

    }
}
