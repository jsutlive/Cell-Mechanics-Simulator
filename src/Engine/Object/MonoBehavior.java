package Engine.Object;

import Engine.States.State;
import Model.Components.Component;
import java.util.ArrayList;
import java.util.List;

public abstract class MonoBehavior implements IBehavior
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
   public static void setGlobalID(MonoBehavior monoBehavior)
   {
      monoBehavior.uniqueID = _ID_COUNTER;
      _ID_COUNTER++;
   }
   public Tag getTag() {return tag;}
   public void addTag(Tag tag){this.tag = tag;}

   public void awake() throws InstantiationException, IllegalAccessException {}
   public void start() {for (Component c: components) c.start();}
   public void update() {for (Component c: components) c.update();}
   public void lateUpdate(){for(Component c: components)c.lateUpdate();}
   public void earlyUpdate(){for (Component c: components) c.earlyUpdate();}
   public void onDestroy(){for (Component c: components) c.onDestroy();}

   /**
    * Removes the object and its references from the current state
    */
   public void destroy()
   {
      State.destroy(this);
   }

   /**
    * Adds a component to this object
    * @param c component
    * @param <T> type of component
    * @return the component (if needed for immediate access)
    */
   public <T extends Component> T addComponent(Component c){
      if(!Component.class.isAssignableFrom(c.getClass())){
         throw new IllegalArgumentException("Object not instance of component");
      }
      components.add(c);
      c.setParent(this);
      c.awake();
      return (T) c;
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

   public <T extends Component> void removeComponent(Class<T> componentClass){
      components.removeIf(c -> componentClass.isAssignableFrom(c.getClass()));
   }
}
