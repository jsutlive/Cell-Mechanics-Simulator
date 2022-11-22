package Input;

import Framework.Events.EventHandler;
import Framework.Object.Component;
import Framework.Object.Entity;

import java.util.HashSet;

public class SelectionEvents {
    private static HashSet<Entity> selectedEntities = new HashSet<>();
    // Event handler for when this object selects an entity
    public static EventHandler<HashSet<Entity>> onEntitySelected = new EventHandler<>();

    public static void selectEntity(Entity e){
        /******************************************************/
        //TODO: REMOVE THIS PART TO ALLOW MULTIPLE SELECTIONS
        if(selectedEntities.size() > 0) selectedEntities.clear();
        /*******************************************************/
        selectedEntities.add(e);
        onEntitySelected.invoke(selectedEntities);
    }

    public static void addComponentToSelected(Component c){
        if(selectedEntities == null)return;
        for(Entity e: selectedEntities) {
            e.addComponent(c);
        }
    }



}
