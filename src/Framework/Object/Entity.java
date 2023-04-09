package Framework.Object;

import Component.Component;
import Component.Transform;
import Framework.Events.EventHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity: physics object for use in engine
 */
public final class Entity implements IBehavior
{
   public String name;
   private int uniqueID;
   final private List<Component> components = new ArrayList<>();
   private Tag tag;
   public Entity parent;
   public ArrayList<Entity> children = new ArrayList<>();

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
      if(tag!=Tag.MODEL && tag!= Tag.PHYSICS) {
         addComponent(new Transform());
      }
      onAddEntity.invoke(this);
   }

   public void setParent(Entity e){
         parent = e;
         e.children.add(this);
   }

   public void removeParent(){
      parent.children.remove(this);
      parent = null;
   }

   /**
    * Each object has a unique ID, which we can access if necessary. Is not currently used for
    * anything outside debugging purposes.
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
      if (tag == Tag.MODEL|| tag == Tag.PHYSICS){
         removeComponent(Transform.class);
      }
   }

   public void awake() {}
   public void start() {for (Component c: components) {
         if(c.isEnabled()) c.start();
   }}
   public void update() {for (Component c: components) {
      if(c.isEnabled()) c.update();
   }}
   public void lateUpdate(){for(Component c: components){
      if(c.isEnabled()) c.lateUpdate();
   }}
   public void earlyUpdate(){for (Component c: components) {
      if(c.isEnabled()) c.earlyUpdate();
   }}
   public void onDestroy(){for (Component c: components) {
      if(c.isEnabled()) c.onDestroy();
   }}
   public void onValidate(){for(Component c: components)
      c.onValidate();
   }

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
    * @param <T> subtype of component
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

   /**
    * Remove all components of a given type
    * @param componentClass class of component to remove
    * @param <T> subtype of component
    */
   public <T extends Component> void removeComponent(Class<T> componentClass){
      components.removeIf(c -> componentClass.isAssignableFrom(c.getClass()));
   }

   /**
    * Method to have an easy way for adding components to an Entity upon instantiation.
    * @param c component to be added
    * @return this Entity (with new component attached)
    */
   public Entity with(Component c){
      this.addComponent(c);
      return this;
   }

}
