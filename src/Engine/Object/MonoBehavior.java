package Engine.Object;

import Engine.States.State;

public abstract class MonoBehavior<T extends MonoBehavior<T>> implements IBehavior
{
   private static int _ID_COUNTER = 0;
   private int uniqueID;
   private Tag tag;

   public static <T extends MonoBehavior> T createObject(Class<T> monoClass)
           throws InstantiationException, IllegalAccessException {
      T mono = monoClass.newInstance();
      return mono;
   }

   public int getStateID() {return uniqueID;}
   public static int getGlobalID(){return _ID_COUNTER;}
   public static void setGlobalID(MonoBehavior monoBehavior)
   {
      monoBehavior.uniqueID = _ID_COUNTER;
      _ID_COUNTER++;
   }
   public Tag getTag() {return tag;}
   public void addTag(Tag tag){this.tag = tag;}

   public void awake()
   {

   }

   public void start() throws InstantiationException, IllegalAccessException {}

   public void render(){}

   /**
    * Removes the object and its references from the current state
    */
   public void destroy()
   {
      State.destroy(this);
   }
}
