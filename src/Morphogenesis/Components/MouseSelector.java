package Morphogenesis.Components;

import Framework.Events.EventHandler;
import Framework.Events.IEvent;
import Framework.Object.Component;
import Framework.Object.DoNotExposeInGUI;
import Framework.Object.Entity;
import Input.InputEvents;
import Morphogenesis.Components.Meshing.Mesh;
import Utilities.Geometry.Vector.Vector2i;

import java.awt.event.MouseEvent;

@DoNotExposeInGUI
public class MouseSelector extends Component {

    public static EventHandler<Entity> onEntitySelected = new EventHandler<>();
    public void selectEntity(Entity e){
        onEntitySelected.invoke(e);
    }

    @Override
    public void awake() {
        IEvent<MouseEvent> mouseClickEventSink = this::onMouseClicked;
        InputEvents.onClick.subscribe(mouseClickEventSink);
    }

    public void onMouseClicked(MouseEvent e){
        if(e.getButton() == MouseEvent.BUTTON1) {
            Vector2i mousePosition = new Vector2i(e.getX(), e.getY());
            selectEntity(mousePosition);
        }
    }

    public void onMouseDragged(MouseEvent e){

    }

    private  void selectEntity(Vector2i mousePosition) {
        Entity selected = getComponent(Mesh.class).returnCellContainingPoint(mousePosition.asFloat());
        selectEntity(selected);
    }

}
