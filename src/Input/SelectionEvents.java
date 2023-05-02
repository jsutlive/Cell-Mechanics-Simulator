package Input;

import Framework.Events.EventHandler;
import Component.Component;
import Framework.Object.Entity;
import Framework.Object.EntityGroup;
import Framework.Object.Tag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * SelectionEvents: Collection of events used to notify GUI layer and other engine objects upon selection of
 * an entity/entities
 *
 * Copyright (c) 2023 Joseph Sutlive and Tony Zhang
 * All rights reserved
 */
public class SelectionEvents {
    // Main list of entities handled by this class
    private static HashSet<Entity> selectedEntities = new HashSet<>();
    public static HashSet<Entity> getSelectedEntities() {return selectedEntities;}

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

    /**
     * Add a single entity to the list of selected entities, then notify listeners
     * @param e a physics object in the scene
     */
    public static void selectEntity(Entity e){
        if(!selectingMultiple) selectedEntities.clear();
        selectedEntities.add(e);
        onEntitySelected.invoke(selectedEntities);
        onSelectGroup.invoke(-1);
    }

    /**
     * Add a group of entities to the list of selected entities, then notify listeners
     * @param e a list of physics objects in the scene
     */
    public static void selectEntities(List<Entity> e){
        if(!selectingMultiple) selectedEntities.clear();
        selectedEntities.addAll(e);
        onEntitySelected.invoke(selectedEntities);
    }

    /**
     * Invoke the onEntitySelected method, typically as a method to refresh changes in the GUI
     */
    public static void refresh(){
        onEntitySelected.invoke(selectedEntities);
    }

    public static void beginSelectingMultiple(){
        selectingMultiple = true;
    }

    public static void cancelSelectingMultiple(){
        selectingMultiple = false;
    }

    /**
     * Create an EntityGroup to link 1 or more entities from a list of entities
     * @param e a list of physics objects in the scene
     */
    public static void createGroup(List<Entity> e){
        for(EntityGroup group:groups){
            if(group.entities.contains(e.get(0)))return;
        }
        groups.add(new EntityGroup(e, groups.size()));
    }

    /**
     * Add an existing entity group to this objects groups pool
     * @param group an EntityGroup object not currently part of this object's groups list.
     */
    public static void createGroup(EntityGroup group){
        if(groups.contains(group)) return;
        groups.add(group);
        group.groupID = groups.size()-1;
        onCreateGroup.invoke(group.groupID);
    }

    /**
     * Remove a group from the groups list
     * @param index location in groups list
     */
    public static void deleteGroup(int index){
        List<EntityGroup> newGroups = new ArrayList<>();
        for(int i = 0; i < groups.size(); i++){
            if(i!=index){
                groups.get(i).groupID = i;
                newGroups.add(groups.get(i));
            }
        }
        groups = newGroups;
        onDeleteGroup.invoke(index);
        onSelectGroup.invoke(-1);
    }

    /**
     * Remove all groups from list, notify listeners
     */
    public static void clearGroups(){
        groups.clear();
        onClearGroups.invoke(true);
    }

    /**
     * Select all entities of a single group based on its location in the groups list
     * @param idx location in groups list
     */
    public static void selectGroup(int idx){
        selectedEntities.clear();
        selectedEntities.addAll(groups.get(idx).entities);
        onSelectGroup.invoke(idx);
        onEntitySelected.invoke(selectedEntities);
    }

    /**
     * Select all entities of a specific instance of a group from a reference to that group
     * @param group a group
     */
    public static void selectGroup(EntityGroup group){
        selectedEntities.clear();
        selectedEntities.addAll(group.entities);
        onSelectGroup.invoke(group.groupID);
        onEntitySelected.invoke(selectedEntities);
    }

    /**
     * Deselect a single physics object
     * @param selected a currently selected physics object
     */
    public static void deselectEntity(Entity selected) {
        selectedEntities.remove(selected);
        onEntitySelected.invoke(selectedEntities);
    }

    /**
     * Deselect all objects
     */
    public static void clearSelection(){
        selectedEntities.clear();
        onEntitySelected.invoke(selectedEntities);
    }

    /**
     * Delete physics object being selected
     */
    public static void deleteSelection(){
        for(Entity e: selectedEntities){
            e.destroy();
        }
        clearSelection();
    }

    /**
     * Add a new component to the selected entity, re-notify listeners to ensure this change leads to updates
     * @param c a new component
     */
    public static void addComponentToSelected(Component c){
        if(selectedEntities == null)return;
        for(Entity e: selectedEntities) {
            e.addComponent(c);
        }
        onEntitySelected.invoke(selectedEntities);
    }

    /**
     * Remove instances of a specific class from the selected object
     * @param aClass a class which derives from component
     */
    public static void removeComponentFromSelected(Class<? extends Component> aClass) {
        for(Entity e: selectedEntities){
            e.removeComponent(aClass);
        }
        refresh();
    }
}
