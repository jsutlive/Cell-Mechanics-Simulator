import Framework.Engine;

/**
 * Entry point for the program.
 * Creates a new Engine object and runs the "Start" function for it.
 */
public class Launcher
{
    public static void main(String[] args)
    {
        Engine sim = new Engine("Morphogenesis Simulator");
        sim.Start();
    }
}
