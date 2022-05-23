package Engine.States;

import Engine.Object.MonoBehavior;
import Engine.Object.Tag;
import GUI.IRender;
import Model.Components.CellRenderer;
import Model.Components.EdgeRenderer;
import Utilities.Geometry.Vector2f;

import java.util.ArrayList;
import java.util.HashSet;
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

    protected static List<MonoBehavior> allObjects = new ArrayList<>();
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
     * @param type
     * @param <T>
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T extends MonoBehavior<T>> MonoBehavior<T> create(Class<T> type)
            throws InstantiationException, IllegalAccessException {
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
     * @param <T>
     * @return
     */
    public static <T extends MonoBehavior<T>> MonoBehavior<T> findObjectWithTag(Tag tag)
    {
        for (MonoBehavior mono: allObjects) {
            if(mono.getTag() == tag) return mono;
        }
        return null;
    }

    public static void setFlagToRender(MonoBehavior mono)
    {
        CellRenderer rend = (CellRenderer)mono.getComponent(CellRenderer.class);
        //EdgeRenderer rend = (EdgeRenderer)mono.getComponent(EdgeRenderer.class);
        renderBatch.add(rend);
    }

    public static void destroy(MonoBehavior mono)
    {
        if(allObjects.contains(mono)) allObjects.remove(mono);
        if(renderBatch.contains((CellRenderer) mono.getComponent(CellRenderer.class)))
            renderBatch.remove((CellRenderer) mono.getComponent(CellRenderer.class));
    }

}
