package Input;

import Framework.Events.EventHandler;
import Framework.Object.Component;
import Framework.Object.Entity;

import java.util.HashSet;

public class SelectionEvents {
    private static HashSet<Entity> selectedEntities = new HashSet<>();
    private static boolean selectingMultiple;
    // Event handler for when this object selects an entity
    public static EventHandler<HashSet<Entity>> onEntitySelected = new EventHandler<>();

    public static void selectEntity(Entity e){
        if(!selectingMultiple) selectedEntities.clear();
        selectedEntities.add(e);
        onEntitySelected.invoke(selectedEntities);
    }

    public static void refresh(){
        HashSet<Entity> temp = new HashSet<>(selectedEntities);
        selectedEntities.clear();
        for(Entity e: temp) selectEntity(e);
    }

    public static void beginSelectingMultiple(){
        selectingMultiple = true;
    }

    public static void cancelSelectingMultiple(){
        selectingMultiple = false;
    }

    public static void addComponentToSelected(Component c){
        if(selectedEntities == null)return;
        for(Entity e: selectedEntities) {
            e.addComponent(c);
        }
    }

    public static void deselectEntity(Entity selected) {
        if(selectedEntities.contains(selected)) selectedEntities.remove(selected);
        onEntitySelected.invoke(selectedEntities);
    }
}
