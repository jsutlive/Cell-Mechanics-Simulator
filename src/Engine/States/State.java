package Engine.States;

import Engine.Object.MonoBehavior;
import Engine.Object.Tag;
import GUI.IRender;
import Model.Components.ObjectRenderer;
import Utilities.Geometry.Vector2f;

import java.util.ArrayList;
import java.util.List;

public abstract class State
{
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
    public static void addToResultantForce(Vector2f v){RESULTANT_FORCE.add(v);}

    protected static List<MonoBehavior<?>> allObjects = new ArrayList<>();
    protected static List<IRender> renderBatch = new ArrayList<>();
    protected static List<Thread> physicsThreads = new ArrayList<>();

    /**
     * Change state between running simulation and an editor state
     * editor state not implemented
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static void ChangeState() throws InstantiationException, IllegalAccessException {
        if(state == null || state instanceof RunState)
        {
            SetState(new EditorState());
        }
        else
        {
            SetState(new RunState());
        }
        GetState().Init();
    }

    protected static void reset() {
        renderBatch.clear();
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
     * @param type a MonoBehavior class to create an instance of
     * @param <T> type of MonoBehavior class
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T extends MonoBehavior<T>> MonoBehavior<T> create(Class<T> type)
            throws InstantiationException, IllegalAccessException {
        if(!MonoBehavior.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException("Class not assignable from MonoBehavior");
        }
        MonoBehavior mono = MonoBehavior.createObject(type);
        MonoBehavior.setGlobalID(mono);
        allObjects.add(mono);
        Thread thread = new Thread(mono);
        physicsThreads.add(thread);
        thread.start();
        mono.awake();

        return mono;
    }

    /**
     * Look for a tagged object and return the first object with that tag
     * @param tag
     * @return
     */
    public static MonoBehavior<?> findObjectWithTag(Tag tag)
    {
        for (MonoBehavior mono: allObjects) {
            if(mono.getTag() == tag) return mono;
        }
        return null;
    }

    public static void setFlagToRender(MonoBehavior<?> mono)
    {
        IRender rend = mono.getComponent(ObjectRenderer.class);
        renderBatch.add(rend);
    }

    public static void destroy(MonoBehavior<?> mono)
    {
        allObjects.remove(mono);
        mono.removeComponent(ObjectRenderer.class);

    }

}
