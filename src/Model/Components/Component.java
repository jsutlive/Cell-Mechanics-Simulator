package Model.Components;

import Engine.Object.IBehavior;
import Engine.Object.MonoBehavior;

public abstract class Component implements IBehavior {
    protected transient MonoBehavior parent;
    public void setParent(MonoBehavior mono){
        parent = mono;
    }

    public void awake(){init();}
    public abstract void init();
    public void start(){}
    public void update(){}
    public void earlyUpdate(){}
    public void lateUpdate(){}


}
