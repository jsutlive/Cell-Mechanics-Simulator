package Renderer.Graphics;

import Framework.Data.ImageHandler;
import Framework.Object.Entity;
import Framework.Object.Tag;
import Input.InputEvents;
import Morphogenesis.Components.Meshing.RingMesh;
import Morphogenesis.Components.MouseSelector;
import Morphogenesis.Components.Physics.Collision.CellRingCollider;
import Morphogenesis.Components.Physics.Collision.RigidBoundary;
import Morphogenesis.Components.Yolk;
import Renderer.UIElements.Panels.InputPanel;
import Input.SelectionEvents;
import Morphogenesis.Components.Physics.CellGroups.ApicalGradient;
import Morphogenesis.Components.Physics.CellGroups.LateralGradient;
import Morphogenesis.Components.Physics.Spring.ElasticForce;
import Renderer.UIElements.Panels.HierarchyPanel;
import Renderer.UIElements.Panels.PlayPanel;
import Renderer.UIElements.Windows.KeyCommandsHelpPopUp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;

import static Framework.Data.ImageHandler.loadImage;
import static Framework.Object.Tag.MODEL;

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

    boolean modelSelected = false;

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

        frame.setIconImage(loadImage("cell.png"));
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

        InputEvents.onToggleSimulation.subscribe(this::enableMenuBarOptionsOnToggle);
        SelectionEvents.onEntitySelected.subscribe(this::checkForSelectionMenuChange);
    }

    private void checkForSelectionMenuChange(HashSet<Entity> entities){
        if(entities.size() > 1 && modelSelected) {
            modelSelected = false;
            return;
        }
        else if(entities.size() > 1){
            return;
        }
        else if (entities.size() ==1 && modelSelected) {
            modelSelected = false;
        }
        for (Entity e : entities) {
            if (e.getTag() == Tag.MODEL) {
                modelSelected = true;
            }
        }

        frame.setJMenuBar(null);
        createJMenuBar();
        frame.setJMenuBar(menuBar);
    }

    private void createJMenuBar(){
        menuBar = new JMenuBar();
        menuBar.removeAll();
        JMenu menu = new JMenu("File");
        JMenuItem exportItem = new JMenuItem("Export Image", KeyEvent.VK_P);

        exportItem.addActionListener(e-> captureImageFromMenu());
        menu.add(exportItem);
        menuBar.add(menu);

        JMenu menu2 = new JMenu("Test");
        playItem = new JMenuItem("Run", KeyEvent.VK_R);
        playItem.addActionListener(e-> InputEvents.toggleSimulation(true));

        stopItem = new JMenuItem("Stop", KeyEvent.VK_ESCAPE);
        stopItem.addActionListener(e-> InputEvents.toggleSimulation(false));
        menu2.add(playItem);
        menu2.add(stopItem);
        menuBar.add(menu2);

        JMenu menu3 = new JMenu("Selection");
        JMenu addComponentSubMenu = new JMenu("Add Component");

        if(!modelSelected) {
            JMenuItem elasticForceOption = new JMenuItem("Elastic Force");
            elasticForceOption.addActionListener(e -> SelectionEvents.addComponentToSelected(new ElasticForce()));
            addComponentSubMenu.add(elasticForceOption);
        }else{
            JMenuItem apicalGradientOption = new JMenuItem("Apical Gradient");
            apicalGradientOption.addActionListener(e -> SelectionEvents.addComponentToSelected(new ApicalGradient()));
            addComponentSubMenu.add(apicalGradientOption);

            JMenuItem lateralGradientOption = new JMenuItem("Lateral Gradient");
            lateralGradientOption.addActionListener(e -> SelectionEvents.addComponentToSelected(new LateralGradient()));
            addComponentSubMenu.add(lateralGradientOption);
        }
        menu3.add(addComponentSubMenu);
        menuBar.add(menu3);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem keysHelp = new JMenuItem("Keyboard Shortcuts");
        keysHelp.addActionListener(e-> {
            new KeyCommandsHelpPopUp();
        });
        helpMenu.add(keysHelp);
        menuBar.add(helpMenu);
    }

    private void enableMenuBarOptionsOnToggle(boolean b){
        stopItem.setEnabled(b);
        playItem.setEnabled(!b);
    }

    public void exportImage(){
        BufferedImage screenshot = captureImage();
        ImageHandler writer = new ImageHandler(screenshot,
                new File("I://Documents//Harvard//MorphogenesisSimulatorV2//MorphogenesisSimulationV2//assets/no_cornerv2_"+count*500 + ".jpg"));
        writer.write();
        count++;

    }

    private void captureImageFromMenu(){
        BufferedImage screenshot = captureImage();
        JFileChooser chooser = new JFileChooser();
        int choice = chooser.showSaveDialog(null);
        if (choice == JFileChooser.APPROVE_OPTION) {
            ImageHandler writer = new ImageHandler(screenshot, chooser.getSelectedFile());
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
