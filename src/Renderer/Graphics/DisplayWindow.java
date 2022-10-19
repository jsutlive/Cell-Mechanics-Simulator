package Renderer.Graphics;

import Input.InputEvents;
import Input.InputPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DisplayWindow
{
    private JFrame frame;
    private SimulationCanvas canvas;
    private JMenuBar menuBar;
    private final String title;
    private final int width, height;
    static InputEvents input;

    JMenuItem playItem;
    JMenuItem stopItem;

    public DisplayWindow(String _title, int _width, int _height)
    {
        this.title = _title;
        this.width = _width;
        this.height = _height;
        CreateDisplay();
        CreateCanvas();
        createJMenuBar();

        frame.add(canvas);
        frame.setJMenuBar(menuBar);

        InputPanel inputPanel = new InputPanel(canvas);
        frame.setIconImage(new ImageIcon("assets/cell.png").getImage());
        frame.add(inputPanel.getPanel(), BorderLayout.WEST);
        input = new InputEvents();
        canvas.addKeyListener(input);
        canvas.addMouseListener(input);
        canvas.addMouseMotionListener(input);
        frame.setFocusable(true);
        frame.requestFocus();
        frame.pack();

        InputEvents.onPlay.subscribe(this::enableMenuBarOptionsOnPlay);
        InputEvents.onStop.subscribe(this::enableMenuBarOptionsOnStop);
    }

    private void createJMenuBar(){
        menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem exportItem = new JMenuItem("Export Image", KeyEvent.VK_P);
        exportItem.addActionListener(e-> exportImage("TEST3"));
        menu.add(exportItem);
        menuBar.add(menu);

        JMenu menu2 = new JMenu("Test");
        playItem = new JMenuItem("Run", KeyEvent.VK_R);
        playItem.addActionListener(e-> InputEvents.play());

        stopItem = new JMenuItem("Stop", KeyEvent.VK_ESCAPE);
        stopItem.addActionListener(e-> InputEvents.stop());
        menu2.add(playItem);
        menu2.add(stopItem);
        menuBar.add(menu2);
    }

    private void enableMenuBarOptionsOnPlay(boolean b){
        stopItem.setEnabled(true);
        playItem.setEnabled(false);
    }

    private void enableMenuBarOptionsOnStop(boolean b){
        stopItem.setEnabled(false);
        playItem.setEnabled(true);
    }

    public void exportImage(String filename){
        try {
            Robot robot = new Robot();
            int x = frame.getX() + canvas.getX();
            int y = frame.getY() +canvas.getY();
            Rectangle rect = new Rectangle(x, y, width, height);
            BufferedImage screenshot = robot.createScreenCapture(rect);

            ImageIO.write(screenshot, "JPG",
                    new File("Assets/" + filename + ".jpg"));
        } catch (AWTException | IOException e) {
            e.printStackTrace();
        }
    }



    private void CreateDisplay()
    {
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       //this line is necessary for the code to function.
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.getContentPane().setBackground( Color.black );
    }

    private void CreateCanvas() {
        canvas = new SimulationCanvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));

    }

    public SimulationCanvas GetCanvas()
    {
        return canvas;
    }
}
