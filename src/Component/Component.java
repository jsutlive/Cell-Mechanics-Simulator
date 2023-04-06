package Component;

import Framework.Events.EventHandler;
import Framework.Object.Entity;
import Framework.Object.IBehavior;
import Framework.Object.IExposeToGUI;
import Annotations.ReloadComponentOnChange;
import Annotations.ReloadEntityOnChange;

import java.lang.reflect.Field;
import java.util.ArrayList;

public abstract class Component implements IBehavior, IExposeToGUI {
    public EventHandler<Component> onComponentChanged = new EventHandler<>();
    protected transient Entity parent;
    public void setParent(Entity mono){
        parent = mono;
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

    @Override
    public void changeFieldOnGUI(String name, Object value){
        Component c = this;
        for(Field f: getClass().getFields()){
            if(f.getName().equals(name)){
                try {
                    boolean accessible = f.isAccessible();
                    f.setAccessible(true);
                    if(f.getDeclaringClass() == this.getClass().getSuperclass()) {
                        f.set(c.reflect(), value);
                    }
                    else {
                        f.set(this, value);
                    }
                    f.setAccessible(accessible);
                }
                catch (IllegalAccessException e){
                    e.printStackTrace();
                }
                if(c.getClass().getDeclaredAnnotation(ReloadComponentOnChange.class)!=null){
                    c.onValidate();
                }
                if(f.getDeclaredAnnotation(ReloadEntityOnChange.class)!=null){
                    c.parent.awake();
                }
                c.onComponentChanged.invoke(this);
            }
        }
    }

}
