package Framework;

import Framework.States.State;
import Framework.Timer.Time;
import Renderer.Renderer;
import Utilities.Geometry.Vector.Vector2i;

public final class Engine implements Runnable
{
    // rendering system reference
    Renderer renderer;
    // Collecting user input
    // window title
    public static String title;

    // main application thread
    private Thread thread;
    // render thread
    private Thread render;

    private boolean applicationIsRunning = false;

    /**
     * Simplified simulation constructor where the window is automatically set to be 800x800 px
     * @param _title Window title for the simulation
     */
    public Engine(String _title)
    {
        title = _title;
    }

    /**
     * Initialize program
     * @throws InstantiationException fails to create object
     * @throws IllegalAccessException illegally accesses memory while attempting to create objects
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
    private void Tick() throws InstantiationException, IllegalAccessException {
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
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        while(applicationIsRunning)
        {
            Time.Advance();

            // Physics update
            if(Time.isReadyToAdvancePhysics()){
                try {
                    Tick();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
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
