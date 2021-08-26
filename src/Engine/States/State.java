package Engine.States;

import Engine.Object.MonoBehavior;
import Engine.Object.Tag;

import java.util.HashSet;

public abstract class State
{
    public static State state = null;
    public static State GetState()
    {
        return state;
    }
    public static void SetState(State _state)
    {
        state = _state;
    }

    protected static HashSet<MonoBehavior> allObjects = new HashSet<>();
    protected static HashSet<MonoBehavior> renderBatch = new HashSet<>();

    public static void ChangeState() throws InstantiationException, IllegalAccessException {
        RunState runState = (RunState)state;
        if(runState != null)
        {
            SetState(new EditorState());
        }
        else
        {
            SetState(new RunState());
        }
        GetState().Init();
    }

    /**
     * Initializes entities when the state starts. Only called once.
     */
    public abstract void Init() throws InstantiationException, IllegalAccessException;

    public abstract void Render();

    /**
     * Performs all calculations to be updated once per frame cycle.
     */
    public abstract void Tick();

    public static <T extends MonoBehavior<T>> MonoBehavior<T> create(Class<T> type)
            throws InstantiationException, IllegalAccessException {
        MonoBehavior mono = MonoBehavior.createObject(type);
        MonoBehavior.setGlobalID(mono);
        allObjects.add(mono);
        mono.awake();
        return mono;
    }

    public static <T extends MonoBehavior<T>> MonoBehavior<T> findObjectWithTag(Tag tag)
    {
        for (MonoBehavior mono: allObjects) {
            if(mono.getTag() == tag) return mono;
        }
        return null;
    }

    public static void setFlagToRender(MonoBehavior mono)
    {
        renderBatch.add(mono);
    }

}
