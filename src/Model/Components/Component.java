package Model.Components;

import Engine.Object.IBehavior;
import Engine.Object.MonoBehavior;

public abstract class Component implements IBehavior {
    protected transient MonoBehavior parent;
    public void setParent(MonoBehavior mono){
        parent = mono;
    }

    /**
     * Casts parent as an assignable subclass of monobehavior, used for more specific operations. Returns null if parent
     * is not an instance of "type"
     * @param type subclass of MonoBehavior denoted by T
     * @param <T> type of MonoBehavior
     * @return parent as a subclass of MonoBehavior
     */
    public <T extends MonoBehavior> T getParentAs(Class<T> type){
        return type.isInstance(parent) ? type.cast(parent) : null;
    }

    public void awake(){init();}
    public abstract void init();
    public void start(){}
    public void update(){}
    public void earlyUpdate(){}
    public void lateUpdate(){}


}
