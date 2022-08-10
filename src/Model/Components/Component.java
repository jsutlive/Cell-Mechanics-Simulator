package Model.Components;

import Engine.Object.MonoBehavior;

public abstract class Component {
    protected MonoBehavior parent;
    public void setParent(MonoBehavior mono){
        parent = mono;
    }

    public abstract void init();


}
