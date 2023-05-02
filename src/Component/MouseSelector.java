package Component;

import Framework.Object.Annotations.DoNotDestroyInGUI;
import Framework.Object.Entity;
import Framework.Object.Tag;
import Framework.States.StateMachine;
import Input.InputEvents;
import Input.SelectionEvents;
import Renderer.Graphics.IRender;
import Utilities.Geometry.Vector.Vector2i;

import java.awt.*;
import java.awt.event.MouseEvent;
@DoNotDestroyInGUI
public class MouseSelector extends Component {

    static boolean alt = false;
    static boolean shiftKey = false;
    private boolean selecting = false;
    private boolean doNotSelect = false;
    public StateMachine stateMachine;

    @Override
    public void awake() {
        InputEvents.onPress.subscribe(this::onMousePressed);
        InputEvents.onDrag.subscribe(this::onMouseDragged);
        InputEvents.onRelease.subscribe(this::onMouseReleased);
        InputEvents.onAlt.subscribe(this::setAlt);
        InputEvents.onShiftKey.subscribe(this::setShiftModifier);
    }

    @Override
    public void onValidate() {

    }

    public void setAlt(boolean _alt){
        alt = _alt;
    }

    private void setShiftModifier(boolean _shift){
        if(_shift) {
            SelectionEvents.beginSelectingMultiple();
            shiftKey = true;
        }
        else {
            SelectionEvents.cancelSelectingMultiple();
            shiftKey = false;
        }
    }

    private void onMousePressed(MouseEvent e){
        if(e.getButton() == MouseEvent.BUTTON2) {
            doNotSelect = true;
            return;
        }
        doNotSelect = false;
        assert Camera.main != null;
        Vector2i mousePosition = Camera.main.getScreenPoint(new Vector2i(e.getX(), e.getY()));
        if(e.getButton() == MouseEvent.BUTTON1) {
            if(shiftKey) SelectionEvents.beginSelectingMultiple();
            selecting = !alt;
        }else if(e.getButton() == MouseEvent.BUTTON3){
            selecting = false;
        }
        if(selecting) selectEntity(mousePosition);
        else deselectEntity(mousePosition);
        SelectionEvents.beginSelectingMultiple();
    }

    private void onMouseDragged(MouseEvent e){
        if(doNotSelect)return;
        assert Camera.main != null;
        Vector2i mousePosition = Camera.main.getScreenPoint(new Vector2i(e.getX(), e.getY()));
        if(!selecting) deselectEntity(mousePosition);
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
        for(Entity e:stateMachine.allObjects){
            if(e.getTag()== Tag.MODEL) continue;
            Mesh mesh = e.getComponent(Mesh.class);
            if(mesh!= null){

                Entity selected = mesh.returnCellContainingPoint(mousePosition.asFloat());
                if(selected!= null) {
                    SelectionEvents.selectEntity(selected);
                    return;
                }
            }
        }
        SelectionEvents.clearSelection();
    }

    /**
     * If a selected entity exists in the current mousePosition, deselect it.
     * @param mousePosition
     */
    private void deselectEntity(Vector2i mousePosition){
        for(Entity e:SelectionEvents.getSelectedEntities()){
            if(e.getTag()== Tag.MODEL) continue;
            Mesh mesh = e.getComponent(Mesh.class);
            if(mesh!= null){
                Entity selected = mesh.returnCellContainingPoint(mousePosition.asFloat());
                if(selected!= null) {
                    SelectionEvents.deselectEntity(selected);
                    return;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        // Since the object with this attached is only destroyed upon simulation exit, close events instead of simply
        // unsubscribing from them.
        InputEvents.onPress.close();
        InputEvents.onDrag.close();
        InputEvents.onRelease.close();
        InputEvents.onAlt.close();
    }
}
