package Engine.Object;

import Engine.States.State;
import Model.Components.Component;

import java.util.ArrayList;
import java.util.List;

public abstract class MonoBehavior<T extends MonoBehavior<T>> implements IBehavior, Runnable
{
   private static int _ID_COUNTER = 0;
   private int uniqueID;
   private List<Component> components = new ArrayList<>();
   private Tag tag;

   public static <T extends MonoBehavior> T createObject(Class<T> monoClass)
           throws InstantiationException, IllegalAccessException {
      return monoClass.newInstance();
   }

   /**
    * Each object has a unique ID, which we can access if necessary. Currently not used for anything.
    * @return uniqueID of a given MonoBehavior
    */
   public int getStateID() {return uniqueID;}
   public static int getGlobalID(){return _ID_COUNTER;}
   public static void setGlobalID(MonoBehavior monoBehavior)
   {
      monoBehavior.uniqueID = _ID_COUNTER;
      _ID_COUNTER++;
   }
   public Tag getTag() {return tag;}
   public void addTag(Tag tag){this.tag = tag;}

   public void awake() throws InstantiationException, IllegalAccessException {}
   public void start() {}
   public void update() {}
   public void render(){}

   /**
    * Removes the object and its references from the current state
    */
   public void destroy()
   {
      State.destroy(this);
   }

   public void addComponent(Component c){
      components.add(c);
      c.setParent(this);
      c.init();
   }

   public <T extends Component> T getComponent(Class<T> componentClass) {
      for (Component c : components) {
         if (componentClass.isAssignableFrom(c.getClass())) {
            try {
               return componentClass.cast(c);
            } catch (ClassCastException e) {
               e.printStackTrace();
               assert false : "Error: Casting component.";
            }
         }
      }

      return null;
   }

   public void removeComponent(Class componentClass){
      components.removeIf(c -> componentClass.isAssignableFrom(c.getClass()));
   }
}
