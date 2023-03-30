import Framework.Engine;
import Renderer.*;

/**
 * Entry point for the program.
 * Creates a new Engine object and runs the "Start" function for it.
 */
public final class Launcher
{
    public static void main(String[] args) {

        // Build engine
        Engine sim = new Engine();
        Thread physics = new Thread(sim);
        if(args.length > 0) {
            if (args[0] == "-h") {


                // Have engine start threads
                sim.start(new Thread[]{physics}, args);
            }
        }
        else{
            // Build renderer
            Renderer renderer = new Renderer2D("Morphogenesis Simulator");

            // Separate render thread and set as a background process
            Thread render = new Thread(renderer);
            render.setDaemon(true);
            sim.start(new Thread[]{physics, render}, new String[]{""});
        }
    }
}
