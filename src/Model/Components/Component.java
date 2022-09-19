package Model.Components;

import Engine.Object.IBehavior;
import Engine.Object.Entity;

public abstract class Component implements IBehavior {
    protected transient Entity parent;
    public void setParent(Entity mono){
        parent = mono;
    }

    /**
     * Casts parent as an assignable subclass of monobehavior, used for more specific operations. Returns null if parent
     * is not an instance of "type"
     * @param type subclass of Entity denoted by T
     * @param <T> type of Entity
     * @return parent as a subclass of Entity
     */
    public <T extends Entity> T getParentAs(Class<T> type){
        return type.isInstance(parent) ? type.cast(parent) : null;
    }

    /**
     * Same as monobehavior "getComponent", used here to make calls more concise in code
     * @param componentClass component class to be return
     * @param <T> a subtype of component
     * @return a given component from this object
     */
    protected <T extends Component> T getComponent(Class <T> componentClass){
        return parent.getComponent(componentClass);
    }

    public void awake(){}
    public void start(){}
    public void update(){}
    public void earlyUpdate(){}
    public void lateUpdate(){}
    public void onDestroy(){}


}
