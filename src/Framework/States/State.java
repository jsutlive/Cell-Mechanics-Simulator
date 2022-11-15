package Framework.States;

import Framework.Events.EventHandler;
import Framework.Object.Entity;
import Framework.Object.Tag;
import Framework.Timer.Time;
import Renderer.Graphics.IRender;
import Framework.Object.Component;
import Morphogenesis.Components.Render.ObjectRenderer;

import java.util.ArrayList;
import java.util.List;

import static Framework.Data.File.save;
import static Framework.Data.File.load;

public abstract class State
{
    private static int _ID_COUNTER = 0;
    public static State state = null;
    public static State GetState() throws InstantiationException, IllegalAccessException {
        if(state == null) ChangeState();
        return state;
    }
    public static void SetState(State _state)
    {
        state = _state;
    }

    protected static List<Entity> allObjects = new ArrayList<>();
    protected static List<IRender> renderBatch = new ArrayList<>();

    public static EventHandler<Entity> onAddEntity = new EventHandler<>();

    /**
     * Add entity to state and invoke the on add entity method for GUI
     * @param e entity added
     */
    public static void addEntity(Entity e ) {
        allObjects.add(e);
        onAddEntity.invoke(e);
    }

    /**
     * Change state between running simulation and an editor state
     * editor state not implemented
     * @throws InstantiationException problem during object creation upon state loading
     * @throws IllegalAccessException problem created due to changing states during the creation/ destruction of objects
     */
    public static void ChangeState() throws InstantiationException, IllegalAccessException {
        if(state == null)
        {
            SetState(new EditorState());
        }
        else
        {
            state.OnChangeState();
        }
        Time.reset();
        GetState().Init();
    }

    /**
     * Initializes entities when the state starts. Only called once.
     */
    public abstract void Init();

    /**
     * calls render function on all render components/ objects cached in render batch.
     */
    public abstract void Render();

    /**
     * Performs all calculations to be updated once per frame cycle.
     */
    public abstract void Tick();

    /**
     * Base method to create an object and assign it to the given state
     * @param obj Entity to be added to scene
     * @return an Entity as its subclass
     */
    public static Entity create(Entity obj) {
        if(obj!= null) {
            obj.setGlobalID(_ID_COUNTER);
            _ID_COUNTER++;
            addEntity(obj);
            obj.awake();
            return obj;
        }
        return null;
    }

    /**
     * Look for a tagged object and return the first object with that tag
     * @param tag specified tag to search all state entities for
     * @return the first entity found with the specified tag
     */
    public static Entity findObjectWithTag(Tag tag)
    {
        for (Entity mono: allObjects) {
            if(mono.getTag() == tag) return mono;
        }
        return null;
    }

    public static void setFlagToRender(Entity mono)
    {
        IRender rend = mono.getComponent(ObjectRenderer.class);
        renderBatch.add(rend);
    }

    /**
     * Add graphical representation of object that does not have attached physics
     * @param rend object that implements the IRender interface
     */
    public static void addGraphicToScene(IRender rend){
        renderBatch.add(rend);
    }

    /**
     * Remove object and its associated render component from the scene
     * @param obj object to be destroyed
     */
    public static void destroy(Entity obj)
    {
        allObjects.remove(obj);
        if(obj.getComponent(ObjectRenderer.class)!=null)
        renderBatch.remove(obj.getComponent(ObjectRenderer.class));

    }

    /**
     * save a json file with initial position
     */
    protected void saveInitial(){
       save(allObjects);
    }

    /**
     * Remove component from all entities
     * @param componentClass component to be removed
     * @param <T> component subtype
     */
    public <T extends Component> void removeComponentFromAll(Class<T> componentClass){
        if(Component.class.isAssignableFrom(componentClass)){
            for(Entity entity: allObjects){
                    entity.removeComponent(componentClass);
            }
        }
    }

    protected void loadModel()
    {
        Entity[] entities = load();
        if(entities!= null) {
            for (Entity e : entities) addEntity(e);
        }
    }

    abstract void OnChangeState();
}
