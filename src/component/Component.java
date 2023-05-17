package component;

import framework.events.EventHandler;
import framework.object.Entity;
import framework.object.IBehavior;
import framework.object.IExposeToGUI;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Component is the primary base class for behaviors in the physics system.
 * Components that are attached to physics objects will be tied to the same
 * state-level function calls (start, update, etc.) as the entities they are
 * attached to. Components may be disabled to eliminate all interactions during
 * editor and play mode simulation loops.
 *
 * Copyright (c) 2023 Joseph Sutlive and Tony Zhang
 * All rights reserved
 */

public abstract class Component implements IBehavior, IExposeToGUI {
    public EventHandler<Component> onComponentChanged = new EventHandler<>();

    // This is the entity that the component is tied to for system updates
    protected transient Entity parent;
    public void setParent(Entity mono){
        parent = mono;
    }

    // Enable and disable the component with the following methods
    protected boolean enabled = true;
    public boolean isEnabled(){
        return enabled;
    }
    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }

    /**
     * Same as entity "getComponent", used here to make calls more concise in code
     * @param componentClass component class to be return
     * @param <T> a subtype of component
     * @return a given component from this object
     */
    protected <T extends Component> T getComponent(Class <T> componentClass){
        return parent.getComponent(componentClass);
    }

    // Methods in this block correspond to events in the program loop
    public void awake(){}
    public void start(){}
    public void update(){}
    public void earlyUpdate(){}
    public void lateUpdate(){}
    public void onDestroy(){}
    public void onValidate(){}
    //

    /**
     * Super hacky method to get instance of a subclass as its superclass, use wisely
     * @param <T> generic class type (needs to be of type component in this case)
     * @return component as type superClass (T)
     */
    private <T> T reflect(){
        return(T)this;
    }

    public void removeSelf(){
        onDestroy();
        parent.removeComponent(this.getClass());
    }

    public ArrayList<Entity> getChildren(){
        return parent.children;
    }

    /**
     * Accessed from GUI system to change values of variables in a component
     * @param name the name (as a string) of the field to be changed
     * @param value the value (as a base java object) to change the field to.
     */
    @Override
    public void changeFieldOnGUI(String name, Object value){
        // Loop through all fields to find the field that we are looking for
        for(Field f: getClass().getFields()){
            if(f.getName().equals(name)){
                try {
                    // override accessibility issues, get field from a superclass
                    // if necessary, and set value
                    boolean accessible = f.isAccessible();
                    f.setAccessible(true);
                    if(f.getDeclaringClass() == this.getClass().getSuperclass()) {
                        f.set(this.reflect(), value);
                    }
                    else {
                        f.set(this, value);
                    }
                    // reset accessibility to default after use
                    f.setAccessible(accessible);
                }
                // throw error if we can't change field value
                catch (IllegalAccessException e){
                    e.printStackTrace();
                }
                // alert GUI that the component has been changed, and run the
                // onValidate() method
                onValidate();
                onComponentChanged.invoke(this);
            }
        }
    }

}
