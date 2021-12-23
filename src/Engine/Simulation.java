package Engine;

import Engine.States.State;
import Engine.Timer.Time;
import Utilities.Geometry.Vector2i;

public class Simulation implements Runnable
{
    // rendering system reference
    Renderer renderer;
    // window boundary, in px
    public static Vector2i bounds;
    // window title
    public static String title;

    // main application thread
    private Thread thread;
    // render thread
    private Thread render;
    private boolean applicationIsRunning = false;

    /**
     * Simulation object, with window parameters to send to rendering system
     * @param _title title of window
     * @param _width width of window
     * @param _height height of window
     */
    public Simulation(String _title, int _width, int _height)
    {
        bounds.x = _width;
        bounds.y  = _height;
        title = _title;
    }

    /**
     * Simplified simulation constructor where the window is automatically set to be 800x800 px
     * @param _title
     */
    public Simulation(String _title)
    {
        bounds = new Vector2i(800);
        title = _title;
    }

    /**
     * Initialize program
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void init() throws InstantiationException, IllegalAccessException {
        // Get current/ create new renderer instance
        renderer = Renderer.getInstance();
        // Separate render thread and set as a background process
        render = new Thread(renderer);
        render.setDaemon(true);
        // Prepare state loading and timer system
        Time.getInstance();
        State.ChangeState();
    }

    /**
     * Base level simulation object to advance physics
     */
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

    /**
     * Halt program/ application thread
     */
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
