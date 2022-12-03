import Framework.Engine;
import Renderer.*;
/**
 * Entry point for the program.
 * Creates a new Engine object and runs the "Start" function for it.
 */
public final class Launcher
{
    public static void main(String[] args)
    {
        // Build engine
        Engine sim = new Engine();
        // Build renderer
        Renderer renderer = new RendererStdout();

        // Separate render thread and set as a background process
        Thread physics = new Thread(sim);
        Thread render = new Thread(renderer);
        render.setDaemon(true);


        // Have engine start threads
        sim.start(new Thread[]{physics, render});
    }
}
