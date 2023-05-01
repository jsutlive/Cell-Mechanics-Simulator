import Framework.Engine;
import Renderer.*;
import Renderer.Renderer;
import javax.swing.*;

/**
 * Cell Mechanics Simulator Entry Point (start here)
 * Creates simulation engine and renderer/GUI backends
 *
 * Information for new users of this codebase:
 *
 * FOR DETAILED API PLEASE VISIT:       https://github.com/jsutlive/Cell-Mechanics-Simulator/wiki/API
 * FOR CODING EXAMPLES PLEASE VISIT:    https://github.com/jsutlive/Cell-Mechanics-Simulator/wiki/Examples
 *
 * Copyright (c) 2023 Joseph Sutlive
 * All rights reserved
 */
public final class Launcher
{
    private static final String PROGRAM_NAME = "Cell Mechanics Simulator";
    public static void main(String[] args) {

        // Build engine
        Engine sim = new Engine();
        Thread physics = new Thread(sim);
        if(args.length > 0) {
            if (args[0].equals("-h")) {


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

    // Application adjustments for OSX systems
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
