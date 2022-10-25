package Framework.Object;

import Morphogenesis.Components.ReloadComponentOnChange;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public abstract class Component implements IBehavior, IExposeToGUI {
    protected transient Entity parent;
    public void setParent(Entity mono){
        parent = mono;
    }

    /**
     * Casts parent as an assignable subclass of entity, used for more specific operations. Returns null if parent
     * is not an instance of "type"
     * @param type subclass of Entity denoted by T
     * @param <T> type of Entity
     * @return parent as a subclass of Entity
     */
    public <T extends Entity> T getParentAs(Class<T> type){
        return type.isInstance(parent) ? type.cast(parent) : null;
    }

    public <T extends Entity> T parent(){
        if(parent.getClass().isAssignableFrom(Entity.class)){
            return (T) parent.getClass().cast(parent);
        }
        return null;
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

    public void awake(){}
    public void start(){}
    public void update(){}
    public void earlyUpdate(){}
    public void lateUpdate(){}
    public void onDestroy(){}
    public void onValidate(){}

    private <T extends Object> T reflect(Class<T> componentClass){
        return(T)this;
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
                        f.set(c.reflect(getClass().getSuperclass()), value);
                        System.out.println("TEST");
                    }
                    else {
                        f.set(this, value);
                    }
                    f.setAccessible(accessible);
                }
                catch (IllegalAccessException e){
                    e.printStackTrace();
                }
                if(f.getDeclaredAnnotation(ReloadComponentOnChange.class)!=null){
                    c.awake();
                }
            }
        }
    }

}
