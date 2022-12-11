package Morphogenesis;

import Framework.Object.Annotations.DoNotDestroyInGUI;
import Framework.Object.Component;
import Framework.Object.Entity;
import Framework.Object.Tag;
import Framework.States.StateMachine;
import Input.InputEvents;
import Input.SelectionEvents;
import Morphogenesis.Meshing.Mesh;
import Renderer.Camera;
import Utilities.Geometry.Vector.Vector2i;

import java.awt.event.MouseEvent;
@DoNotDestroyInGUI
public class MouseSelector extends Component {

    private static boolean alt = false;
    private static boolean shiftKey = false;
    private static boolean selecting = false;
    public StateMachine stateMachine;

    @Override
    public void awake() {
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

    private void onMousePressed(MouseEvent e){
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
        System.out.println(stateMachine.allObjects.size());
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

    private void deselectEntity(Vector2i mousePosition){
        for(Entity e:stateMachine.allObjects){
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
        InputEvents.onPress.close();
        InputEvents.onDrag.close();
        InputEvents.onRelease.close();
        InputEvents.onAlt.close();
    }
}
