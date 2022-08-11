package Engine;

import Engine.States.State;
import Engine.Timer.Time;
import Input.Input;
import Physics.Rigidbodies.Node;
import Utilities.Geometry.Vector2f;
import Utilities.Geometry.Vector2i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Simulation implements Runnable
{
    //TODO: Method that is NOT THIS for recording forces
    public static HashMap<Node, Vector2f> FORCE_HISTORY = new HashMap<>();

    public static float TIMESTEP = 1e-1f;
    // rendering system reference
    Renderer renderer;
    // Collecting user input
    Input inputHandler;
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

        // Get input system
        inputHandler = Input.getInstance();
        //System.out.println(inputHandler);

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
                try {
                    Tick();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
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
