package Renderer.Graphics;

import Framework.Data.ImageHandler;
import Framework.Object.Annotations.DoNotDestroyInGUI;
import Framework.Object.Annotations.DoNotExposeInGUI;
import Framework.Object.Component;
import Framework.Object.Entity;
import Framework.Object.Tag;
import Input.InputEvents;
import Morphogenesis.Components.Meshing.RingMesh;
import Morphogenesis.Components.MouseSelector;
import Morphogenesis.Components.Physics.Collision.CellRingCollider;
import Morphogenesis.Components.Physics.Collision.RigidBoundary;
import Morphogenesis.Components.Physics.OsmosisForce;
import Morphogenesis.Components.Physics.Spring.ApicalConstrictingSpringForce;
import Morphogenesis.Components.Physics.Spring.LateralShorteningSpringForce;
import Morphogenesis.Components.Yolk;
import Renderer.UIElements.Panels.*;
import Input.SelectionEvents;
import Morphogenesis.Components.Physics.CellGroups.ApicalGradient;
import Morphogenesis.Components.Physics.CellGroups.LateralGradient;
import Morphogenesis.Components.Physics.Spring.ElasticForce;
import Renderer.UIElements.Windows.KeyCommandsHelpPopUp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
    JMenuItem clearItem;

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

        JMenu loadMenu = new JMenu("Load Preset");
        JMenuItem loadEmbryo = new JMenuItem("Embryo");
        loadEmbryo.addActionListener(e->InputEvents.onLoadModel.invoke("Embryo"));
        JMenuItem loadHex = new JMenuItem("Hexagons");
        loadHex.addActionListener(e-> InputEvents.onLoadModel.invoke("Hexagons"));
        loadMenu.add(loadEmbryo);
        loadMenu.add(loadHex);

        menu.add(loadMenu);

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

        clearItem = new JMenuItem("Clear Simulation");
        clearItem.addActionListener(e->{
            InputEvents.clear();
            SelectionEvents.clearSelection();
        });
        menu2.add(clearItem);
        menuBar.add(menu2);

        JMenu menu3 = new JMenu("Selection");
        JMenu addComponentSubMenu = new JMenu("Add Component");

        if(!modelSelected) {
            JMenuItem elasticForceOption = new JMenuItem("Elastic Force");
            elasticForceOption.addActionListener(e -> SelectionEvents.addComponentToSelected(new ElasticForce()));
            addComponentSubMenu.add(elasticForceOption);

            JMenuItem osmosisForceOption = new JMenuItem("Osmosis Force");
            osmosisForceOption.addActionListener(e -> SelectionEvents.addComponentToSelected(new OsmosisForce()));
            addComponentSubMenu.add(osmosisForceOption);

        }else{
            JMenuItem apicalGradientOption = new JMenuItem("Apical Gradient");
            apicalGradientOption.addActionListener(e -> SelectionEvents.addComponentToSelected(new ApicalGradient()));
            addComponentSubMenu.add(apicalGradientOption);

            JMenuItem lateralGradientOption = new JMenuItem("Lateral Gradient");
            lateralGradientOption.addActionListener(e -> SelectionEvents.addComponentToSelected(new LateralGradient()));
            addComponentSubMenu.add(lateralGradientOption);
        }
        menu3.add(addComponentSubMenu);

        JMenu removeComponentSubMenu = new JMenu("Remove Component");
        for (Component c : getSelectedComponents()) {
            JMenuItem option = new JMenuItem(c.getClass().getSimpleName());
            option.addActionListener(e -> SelectionEvents.removeComponentFromSelected(c.getClass()));
            removeComponentSubMenu.add(option);
        }
        menu3.add(removeComponentSubMenu);


        menuBar.add(menu3);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem keysHelp = new JMenuItem("Keyboard Shortcuts");
        keysHelp.addActionListener(e-> {
            new KeyCommandsHelpPopUp();
        });
        helpMenu.add(keysHelp);
        menuBar.add(helpMenu);
    }

    public List<Component> getSelectedComponents(){
        List<Entity> e = new ArrayList<>(SelectionEvents.getSelectedEntities());
        List<Component> componentsToMenu = new ArrayList<>();
        if(e.size() == 0) return componentsToMenu;
        for(Component c: e.get(0).getComponents()) {
            boolean addToMenu = true;
            if (c.getClass().getAnnotation(DoNotExposeInGUI.class) != null) continue;
            if (c.getClass().getAnnotation(DoNotDestroyInGUI.class) != null) continue;
            for (int i = 1; i < e.size(); i++) {
                if (e.get(i).getComponent(c.getClass()) == null) {
                    addToMenu = false;
                    break;
                }
            }
            if (addToMenu) {
                componentsToMenu.add(c);
            }
        }
        return componentsToMenu;
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
