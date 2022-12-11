package Framework.Object;

import Framework.Events.EventHandler;
import Framework.States.State;
import java.util.ArrayList;
import java.util.List;

public final class Entity implements IBehavior
{
   public String name;
   private int uniqueID;
   private List<Component> components = new ArrayList<>();
   private Tag tag;

   public static EventHandler<Entity> onAddEntity = new EventHandler<>();
   public static EventHandler<Entity> onRemoveEntity = new EventHandler<>();

   public Entity(){
      onAddEntity.invoke(this);
   }
   public Entity(String name){
      this.name = name;
      addComponent(new Transform());
      onAddEntity.invoke(this);
   }

   public Entity(String name, int uniqueID, Tag tag){
      this.name = name;
      this.uniqueID = uniqueID;
      this.tag = tag;
      if(tag!=Tag.MODEL) {
         addComponent(new Transform());
      }
      onAddEntity.invoke(this);
   }

   /**
    * Each object has a unique ID, which we can access if necessary. Currently not used for anything.
    * @return uniqueID of a given Entity
    */
   public int getStateID() {return uniqueID;}
   public void setStateID(int ID)
   {
      uniqueID = ID;
   }
   public Tag getTag() {return tag;}
   public void addTag(Tag tag){
      this.tag = tag;
      if (tag == Tag.MODEL){
         removeComponent(Transform.class);
      }
   }

   public void awake() {}
   public void start() {for (Component c: components) c.start();}
   public void update() {for (Component c: components) c.update();}
   public void lateUpdate(){for(Component c: components)c.lateUpdate();}
   public void earlyUpdate(){for (Component c: components) c.earlyUpdate();}
   public void onDestroy(){for (Component c: components) c.onDestroy();}
   public void onValidate(){}

   /**
    * Removes the object and its references from the current state
    */
   public void destroy() {
      onDestroy();
      onRemoveEntity.invoke(this);
   }

   /**
    * Adds a component to this object
    * @param c component
    * @param <T> type of component
    * @return the component (if needed for immediate access)
    */
   public <T extends Component> T addComponent(Component c){
      components.add(c);
      c.setParent(this);
      c.onValidate();
      c.awake();
      return (T) c;
   }

   public List<Component> getComponents(){
      return components;
   }

   /**
    * Returns a component of a given subclass of component
    * @param componentClass an instance of this class is added as a component
    * @param <T> sybtype of component
    * @return the component that was added to this entity
    */
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

   public Entity with(Component c){
      this.addComponent(c);
      return this;
   }

}
