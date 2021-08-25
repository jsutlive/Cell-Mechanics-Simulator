package Engine;

import Engine.States.State;
import Engine.Timer.Time;
import Utilities.Geometry.Vector2i;

public class Simulation implements Runnable
{
    Thread render;
    Renderer renderer;
    public static Vector2i bounds;
    public static String title;

    private Thread thread;
    private boolean applicationIsRunning = false;

    public Simulation(String _title, int _width, int _height)
    {
        bounds.x = _width;
        bounds.y  = _height;
        title = _title;
    }

    public Simulation(String _title)
    {
        bounds = new Vector2i(800);
        title = _title;
    }

    private void init()
    {
        renderer = Renderer.getInstance();
        render = new Thread(renderer);
        Time.getInstance();
        State.ChangeState();
    }

    private void Tick()
    {
        if(State.GetState() != null)
        {
            State.GetState().Tick();
        }
    }

    @Override
    public void run()
    {
        init();
        while(applicationIsRunning)
        {
            Time.Advance();
            if(Time.isReadyForNextFrame())
            {
                render.run();
                Tick();
            }
            Time.printFrameRate();
        }
        Stop();
    }

    public synchronized void Start()
    {
        if(applicationIsRunning){return;}
        applicationIsRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void Stop()
    {
        if(!applicationIsRunning){return;}
        applicationIsRunning = false;
        try {
            thread.join();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
