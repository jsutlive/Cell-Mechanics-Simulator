package Input;

import Framework.Events.EventHandler;
import Framework.Object.Component;
import Framework.Object.Entity;
import Framework.Object.EntityGroup;
import Framework.Object.Tag;
import Renderer.UIElements.Panels.EntityPanel;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SelectionEvents {
    private static HashSet<Entity> selectedEntities = new HashSet<>();
    private static boolean selectingMultiple;
    // Event handler for when this object selects an entity
    public static EventHandler<HashSet<Entity>> onEntitySelected = new EventHandler<>();
    public static EventHandler<Component> onSelectionButtonPressed = new EventHandler<>();
    public static EventHandler<Tag> onTagSelected = new EventHandler<>();
    public static EventHandler<Integer> onCreateGroup = new EventHandler<>();
    public static EventHandler<Integer> onSelectGroup = new EventHandler<>();
    public static EventHandler<Integer> onDeleteGroup = new EventHandler<>();
    public static EventHandler<Boolean> onClearGroups = new EventHandler<>();


    public static List<EntityGroup> groups = new ArrayList<>();

    public static void selectEntity(Entity e){
        if(!selectingMultiple) selectedEntities.clear();
        selectedEntities.add(e);
        onEntitySelected.invoke(selectedEntities);
        onSelectGroup.invoke(-1);
    }

    public static void createGroup(List<Entity> e){
        for(EntityGroup group:groups){
            if(group.entities.contains(e.get(0)))return;
        }
        groups.add(new EntityGroup(e, groups.size()));
        onCreateGroup.invoke(groups.size()-1);
    }

    public static void deleteGroup(int index){
        List<EntityGroup> newGroups = new ArrayList<>();
        for(int i = 0; i < groups.size(); i++){
            if(i!=index)newGroups.add(groups.get(i));
        }
        groups = newGroups;
        onDeleteGroup.invoke(index);
        onSelectGroup.invoke(-1);
    }

    public static void clearGroups(){
        groups.clear();
        onClearGroups.invoke(true);
    }

    public static void selectGroup(int i){
        selectedEntities.clear();
        selectedEntities.addAll(groups.get(i).entities);
        onSelectGroup.invoke(i);
        onEntitySelected.invoke(selectedEntities);
    }

    public static void clearSelection(){
        selectedEntities.clear();
        onEntitySelected.invoke(selectedEntities);
    }

    public static void deleteSelection(){
        for(Entity e: selectedEntities){
            e.destroy();
        }
        clearSelection();
    }

    public static void selectEntities(List<Entity> e){
        if(!selectingMultiple) selectedEntities.clear();
        selectedEntities.addAll(e);
        onEntitySelected.invoke(selectedEntities);
    }

    public static HashSet<Entity> getSelectedEntities() {return selectedEntities;}

    public static void refresh(){
        onEntitySelected.invoke(selectedEntities);
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
        onEntitySelected.invoke(selectedEntities);
    }

    public static void deselectEntity(Entity selected) {
        selectedEntities.remove(selected);
        onEntitySelected.invoke(selectedEntities);
    }

    public static void removeComponentFromSelected(Class<? extends Component> aClass) {
        for(Entity e: selectedEntities){
            e.removeComponent(aClass);
        }
        refresh();
    }
}
