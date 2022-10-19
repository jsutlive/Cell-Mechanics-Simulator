package Framework.States;

import Framework.Engine;
import Framework.Events.EventHandler;
import Framework.Events.IEvent;
import Framework.Object.Entity;
import Framework.Object.Tag;
import Framework.Timer.Time;
import Input.InputEvents;
import Renderer.Graphics.IRender;
import Framework.Object.Component;
import Morphogenesis.Components.Render.ObjectRenderer;
import Utilities.Geometry.Vector.Vector2f;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class State
{
    int count = 0;
    public static State state = null;
    public static State GetState() throws InstantiationException, IllegalAccessException {
        if(state == null) ChangeState();
        return state;
    }
    public static void SetState(State _state)
    {
        state = _state;
    }
    public static Vector2f RESULTANT_FORCE = new Vector2f(0);
    public static void addToResultantForce(Vector2f v){RESULTANT_FORCE = RESULTANT_FORCE.add(v);}

    protected static List<Entity> allObjects = new ArrayList<>();
    protected List<IRender> renderBatch = new ArrayList<>();
    protected static List<Thread> physicsThreads = new ArrayList<>();

    public static EventHandler<Float> setNewElasticConstants = new EventHandler<>();
    public static void setNewElasticConstants(float f){
        setNewElasticConstants.invoke(f);
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
            currentObjects = state.allObjects;
            renderBatch = state.renderBatch;
        }
        if(state == null || state instanceof RunState)
        {
            if(state!= null) {
                state.OnChangeState();
                allObjects = currentObjects;
                state.renderBatch = renderBatch;
            }
            SetState(new EditorState());
        }
        else
        {
            SetState(new RunState());
            allObjects = currentObjects;
            state.renderBatch = renderBatch;
        }
        Entity.resetGlobalID();
        Time.reset();
        GetState().Init();
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
     * Base method to create an object and assign it to the given state
     * @param type a Entity class to create an instance of
     * @param <T> type of Entity class
     * @return an Entity as its subclass
     */
    public static <T extends Entity> T create(Class<T> type) {
        return create(type, null);
    }

    public static <T extends Entity> T create(Class<T> type, Component component) {
        Entity obj;
        if(!Entity.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException("Class not assignable from Entity");
        }
        if(component == null) {
            obj = Entity.createObject(type);
        }
        else{
            obj = Entity.createObject(type, component);
        }
        //Create entity and have it perform its awake functions, encapsulated in null check
        if(obj!= null) {
            Entity.setGlobalID(obj);
            allObjects.add(obj);
            return type.cast(obj);
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
        for (Entity mono: state.allObjects) {
            if(mono.getTag() == tag) return mono;
        }
        return null;
    }

    /**
     * Returns the first object of a given class type found
     * @param type a class which inherits from the entity base class
     * @param <T> subtype of entity
     * @return an object as its specific subclass
     */
    public static <T extends Entity>T findObjectOfType(Class<T> type){
        for(Entity obj: state.allObjects){
            if(type.isAssignableFrom(obj.getClass())){
                try {
                    return type.cast(obj);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error: Casting component.";
                }
            }
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
        state.allObjects.remove(obj);
        obj.removeComponent(ObjectRenderer.class);

    }

    public void save()
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

    public void saveInitial(){
        try {
            FileWriter filewriter = new FileWriter("settings.txt");
            filewriter.write(Engine.gsonOnce.toJson(findObjectWithTag(Tag.MODEL)));
            filewriter.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }


    public void load()
    {

    }

    abstract void OnChangeState();
}
