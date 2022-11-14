package Framework.States;

import Framework.Engine;
import Framework.Events.EventHandler;
import Framework.Object.Entity;
import Framework.Object.Tag;
import Framework.Timer.Time;
import Renderer.Graphics.IRender;
import Framework.Object.Component;
import Morphogenesis.Components.Render.ObjectRenderer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Framework.Data.File.save;
import static Framework.Data.File.load;

public abstract class State
{
    int count = 0;
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
    protected List<IRender> renderBatch = new ArrayList<>();
    protected static List<Thread> physicsThreads = new ArrayList<>();

    public static List<Entity> getAllObjects(){
        return allObjects;
    }

    public static EventHandler<Entity> onAddEntity = new EventHandler<>();

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
        List<Entity> currentObjects = new ArrayList<>();
        List<IRender> renderBatch = new ArrayList<>();
        if(state!= null) {
            currentObjects = allObjects;
            renderBatch = state.renderBatch;
        }
        if(state == null)
        {
            SetState(new EditorState());
        }
        else
        {
            state.OnChangeState();
            allObjects = currentObjects;
            state.renderBatch = renderBatch;
        }
        resetGlobalID();
        Time.reset();
        GetState().Init();
    }

    private static void resetGlobalID(){
        _ID_COUNTER = 0;
    }


    protected static void reset() {
        state.renderBatch.clear();
        allObjects.clear();
        physicsThreads.clear();
    }

    /**
     * Initializes entities when the state starts. Only called once.
     */
    public abstract void Init() throws InstantiationException, IllegalAccessException;

    public abstract void Render();

    /**
     * Performs all calculations to be updated once per frame cycle.
     */
    public abstract void Tick() throws InstantiationException, IllegalAccessException;

    /**
     * Base method to create an object and assign it to the given state     *
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
        state.renderBatch.add(rend);
    }

    /**
     * Add graphical representation of object that does not have attached physics
     * @param rend object that implements the IRender interface
     */
    public static void addGraphicToScene(IRender rend){
        state.renderBatch.add(rend);
    }

    public static void destroy(Entity obj)
    {
        allObjects.remove(obj);
        if(obj.getComponent(ObjectRenderer.class)!=null)
        state.renderBatch.remove(obj.getComponent(ObjectRenderer.class));

    }

    protected void saveRecurring()
    {
        if(count > 10) return;
        try {
            FileWriter filewriter = new FileWriter("save_data/embryo_" + count + "_.txt");
            filewriter.write(Engine.gson.toJson(allObjects));
            filewriter.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
        count++;
    }

    protected void saveInitial(){
       save(allObjects);
    }

    public static void addComponentToAll(Class<?> componentClass){
        if(Component.class.isAssignableFrom(componentClass)){
            for(Entity entity: allObjects){
                try {
                    entity.addComponent((Component) componentClass.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

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
