import Framework.Engine;
import Renderer.*;
import Renderer.Renderer;

import javax.swing.*;

/**
 * Entry point for the program.
 * Creates a new Engine object and runs the "Start" function for it.
 */
public final class Launcher
{
    private static final String PROGRAM_NAME = "Cell Mechanics Simulator";
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
            // Set application window style prior to window creation
            setApplicationWindowLookAndFeel();
            // Build renderer
            Renderer renderer = new Renderer2D(PROGRAM_NAME);

            // Separate render thread and set as a background process
            Thread render = new Thread(renderer);
            render.setDaemon(true);
            sim.start(new Thread[]{physics, render}, new String[]{""});
        }
    }

    private static void setApplicationWindowLookAndFeel() {
        System.setProperty( "apple.laf.useScreenMenuBar", "true" );
        System.setProperty("apple.awt.application.name", PROGRAM_NAME);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
}
