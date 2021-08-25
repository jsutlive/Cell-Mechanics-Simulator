package Engine.States;

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

    public static void ChangeState()
    {
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
    public abstract void Init();

    public abstract void Render();

    /**
     * Performs all calculations to be updated once per frame cycle.
     */
    public abstract void Tick();

}
