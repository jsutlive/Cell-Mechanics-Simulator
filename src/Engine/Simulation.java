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

    /**
     * Simulation
     * @param _title
     * @param _width
     * @param _height
     */
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

    private void init() throws InstantiationException, IllegalAccessException {
        renderer = Renderer.getInstance();
        render = new Thread(renderer);
        render.setDaemon(true);
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

    /**
     * Main program loop
     */
    @Override
    public void run()
    {
        try {
            init();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        while(applicationIsRunning)
        {
            
            Time.Advance();
            
            // Physics update
            if(Time.isReadyToAdvancePhysics()){
                Tick();
            }
            // Render update
            if(Time.isReadyForNextFrame())
            {
                render.run();
            }
            
            Time.printFrameRate();
        }
        
        // Halt application
        Stop();
    }

    /**
     * Begin application thread
     */
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
