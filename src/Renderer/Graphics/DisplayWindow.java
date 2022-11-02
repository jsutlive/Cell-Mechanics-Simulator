package Renderer.Graphics;

import Framework.Data.ImageWriter;
import Input.InputEvents;
import Input.InputPanel;
import Input.SelectionEvents;
import Morphogenesis.Components.Physics.Spring.ElasticForce;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

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

    int count  = 0;
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

        HierarchyPanel hierarchyPanel = new HierarchyPanel();
        JScrollPane hierarchyScrollPlane= new JScrollPane(hierarchyPanel.getPanel());
        hierarchyPanel.getPanel().setAutoscrolls(true);
        //frame.add(hierarchyScrollPlane);
        //frame.add(hierarchyPanel.getPanel(), BorderLayout.EAST);

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
        exportItem.addActionListener(e-> captureImageFromMenu());
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

        JMenu menu3 = new JMenu("Selection");
        JMenu addComponentSubMenu = new JMenu("Add Component");

        JMenuItem elasticForceOption = new JMenuItem("Elastic Force");
        elasticForceOption.addActionListener(e-> SelectionEvents.addComponentToSelected(new ElasticForce()));
        addComponentSubMenu.add(elasticForceOption);

        menu3.add(addComponentSubMenu);
        menuBar.add(menu3);
    }

    private void enableMenuBarOptionsOnPlay(boolean b){
        stopItem.setEnabled(true);
        playItem.setEnabled(false);
    }

    private void enableMenuBarOptionsOnStop(boolean b){
        stopItem.setEnabled(false);
        playItem.setEnabled(true);
    }

    public void exportImage(){
        BufferedImage screenshot = captureImage();
        ImageWriter writer = new ImageWriter(screenshot,
                new File("I://Documents//Harvard//MorphogenesisSimulatorV2//MorphogenesisSimulationV2//assets/no_cornerv2_"+count*500 + ".jpg"));
        writer.write();
        count++;

    }

    private void captureImageFromMenu(){
        BufferedImage screenshot = captureImage();
        JFileChooser chooser = new JFileChooser();
        int choice = chooser.showSaveDialog(null);
        if (choice == JFileChooser.APPROVE_OPTION) {
            ImageWriter writer = new ImageWriter(screenshot, chooser.getSelectedFile());
            writer.write();
        }
    }

    public BufferedImage captureImage(){
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        int x = frame.getX() + canvas.getX();
        int y = frame.getY() +canvas.getY();
        Rectangle rect = new Rectangle(x, y, width, height);
        return robot.createScreenCapture(rect);
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
