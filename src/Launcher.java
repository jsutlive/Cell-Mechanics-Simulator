import Engine.Simulation;

/**
 * Entry point for the program.
 * Creates a new Simulation object and runs the "Start" function for it.
 */
public class Launcher
{
    public static void main(String[] args)
    {
        Simulation sim = new Simulation("Simulation");
        sim.Start();
    }
}
