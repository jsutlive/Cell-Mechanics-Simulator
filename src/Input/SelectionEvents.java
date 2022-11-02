package Input;

import Framework.Events.IEvent;
import Framework.Object.Component;
import Framework.Object.Entity;

public class SelectionEvents {
    public static Entity selectedEntity;

    public static void addComponentToSelected(Component c){
        if(selectedEntity == null)return;
        selectedEntity.addComponent(c);
    }



}
