package Renderer.Graphics;

import Framework.Data.ImageHandler;
import Framework.Object.Annotations.DoNotDestroyInGUI;
import Framework.Object.Annotations.DoNotExposeInGUI;
import Component.*;
import Component.Component;
import Framework.Object.Entity;
import Framework.Object.Tag;

import Framework.Utilities.Debug;
import Input.InputEvents;
import Renderer.UIElements.Panels.*;
import Input.SelectionEvents;
import Renderer.UIElements.Windows.KeyCommandsHelpPopUp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

import static Framework.Data.ImageHandler.loadImage;
import static Framework.Object.Tag.MODEL;
import static Utilities.StringUtils.parseCSV;
import static Utilities.StringUtils.splitPascalCase;

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

    public DisplayWindow(String _title, int _width, int _height)
    {
        this.title = _title;
        this.width = _width;
        this.height = _height;

        CreateDisplay();


        createCanvas();
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        createJMenuBar();
        frame.setJMenuBar(menuBar);




        ObjectPanel obj = new ObjectPanel();
        InputPanel inputPanel = new InputPanel(canvas);
        JPanel objectInputGroup = new JPanel();
        objectInputGroup.setLayout(new BoxLayout(objectInputGroup, BoxLayout.X_AXIS));
        objectInputGroup.add(obj.getPanel());
        objectInputGroup.add(inputPanel.getPanel());
        frame.add(objectInputGroup, BorderLayout.WEST);

        frame.setIconImage(loadImage("cell.png"));


        DebugPanel debug = new DebugPanel();
        frame.add(debug.getPanel(), BorderLayout.SOUTH);


        input = new InputEvents();
        canvas.addKeyListener(input);
        canvas.addMouseListener(input);
        canvas.addMouseMotionListener(input);
        canvas.addMouseWheelListener(input);
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
                break;
            }
        }

        frame.setJMenuBar(null);
        createJMenuBar();
        frame.setJMenuBar(menuBar);

        /*frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                createCanvas();
            }
        });*/
    }

    private void createJMenuBar(){

        menuBar = new JMenuBar();
        menuBar.removeAll();
        JMenu menu = new JMenu("File");

        JMenuItem newSceneItem = new JMenuItem("New Scene");
        newSceneItem.addActionListener(e-> {
            InputEvents.onLoadModel.invoke("");
        });
        menu.add(newSceneItem);

        JMenu loadMenu = new JMenu("Load Preset");
        JMenuItem loadEmbryo = new JMenuItem("Embryo");
        loadEmbryo.addActionListener(e->InputEvents.onLoadModel.invoke("Embryo"));
        JMenuItem loadHex = new JMenuItem("Hexagons");
        loadHex.addActionListener(e-> InputEvents.onLoadModel.invoke("Hexagons"));
        JMenuItem loadDebug = new JMenuItem("Debug");
        loadDebug.addActionListener(e-> InputEvents.onLoadModel.invoke("Debug"));
        loadMenu.add(loadDebug);
        loadMenu.add(loadEmbryo);
        loadMenu.add(loadHex);

        menu.add(loadMenu);

        JMenuItem exportItem = new JMenuItem("Export Image", KeyEvent.VK_P);
        exportItem.addActionListener(e-> captureImageFromMenu());
        menu.add(exportItem);
        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.addActionListener(e-> System.exit(0));
        menu.add(quitMenuItem);
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

        JMenu forceSubMenu = new JMenu("Force");
        JMenu meshSubMenu = new JMenu("Mesh");
        JMenu experimentSubMenu = new JMenu("Experiment");


        List<String[]> args= new ArrayList<>();
        try {
            File file = new File("assets/UserComponents.csv");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                args.add(parseCSV(scanner.nextLine()));
            }

            scanner.close();
            Debug.Log("Loaded batch file to system");
        } catch (FileNotFoundException e) {
            // Alert user if file can't be used
            Debug.LogError("Invalid File: Component Menu");
        }

        for(String[] subMenuValues : args){
            JMenuItem item = new JMenuItem(splitPascalCase(subMenuValues[0]));
            Class itemClass;
            System.out.println("Component." + subMenuValues[0]);
            try {
                itemClass = Class.forName("Component." + subMenuValues[0]);
            } catch (ClassNotFoundException e) {
                Debug.LogWarning("Class " + splitPascalCase(subMenuValues[0]) + " not found, skipping");
                continue;
            }
            item.addActionListener(e-> {
                try {
                    SelectionEvents.addComponentToSelected((Component)itemClass.newInstance());
                } catch (InstantiationException | IllegalAccessException exc) {
                    exc.printStackTrace();
                }
            });
            String type = subMenuValues[1];
            switch(type){
                case "Force":
                    forceSubMenu.add(item);
                    break;
                case "Mesh":
                    meshSubMenu.add(item);
                    break;
                case "Experiment":
                    experimentSubMenu.add(item);
                    break;
                default:
                    break;
            }

        }

        addComponentSubMenu.add(forceSubMenu);
        addComponentSubMenu.add(meshSubMenu);
        addComponentSubMenu.add(experimentSubMenu);
            /*JMenuItem elasticForceOption = new JMenuItem("Elastic Force");
            elasticForceOption.addActionListener(e -> SelectionEvents.addComponentToSelected(new ElasticForce()));
            addComponentSubMenu.add(elasticForceOption);

            JMenuItem basalLossOption = new JMenuItem("Elastic Force Basal Rigidity Loss");
            basalLossOption.addActionListener(e -> SelectionEvents.addComponentToSelected(new BasalRigidityLossSpringForce()));
            addComponentSubMenu.add(basalLossOption);

            JMenuItem osmosisForceOption = new JMenuItem("Osmosis Force");
            osmosisForceOption.addActionListener(e -> SelectionEvents.addComponentToSelected(new OsmosisForce()));
            addComponentSubMenu.add(osmosisForceOption);

            JMenuItem cornerStiffOption = new JMenuItem("Corner Stiffness");
            cornerStiffOption.addActionListener(e -> SelectionEvents.addComponentToSelected(new CornerStiffness2D()));
            addComponentSubMenu.add(cornerStiffOption);

            JMenuItem edgeStiffnessOption = new JMenuItem("Edge Stiffness");
            edgeStiffnessOption.addActionListener(e -> SelectionEvents.addComponentToSelected(new EdgeStiffness2D()));
            addComponentSubMenu.add(edgeStiffnessOption);

            JMenuItem meshStiffnessOption = new JMenuItem("Mesh Stiffness");
            meshStiffnessOption.addActionListener(e -> SelectionEvents.addComponentToSelected(new MeshStiffness2D()));
            addComponentSubMenu.add(meshStiffnessOption);

            JMenuItem apicalConstrictionForce = new JMenuItem("Apical Constricting Force");
            apicalConstrictionForce.addActionListener(e -> SelectionEvents.addComponentToSelected(new ApicalGradient()));
            addComponentSubMenu.add(apicalConstrictionForce);

            JMenuItem lateralConstrictionOption = new JMenuItem("Lateral Constricting Force");
            lateralConstrictionOption.addActionListener(e -> SelectionEvents.addComponentToSelected(new LateralGradient()));
            addComponentSubMenu.add(lateralConstrictionOption);

            JMenuItem laserAblationOption = new JMenuItem("Laser Ablation");
            laserAblationOption.addActionListener(e -> SelectionEvents.addComponentToSelected(new LaserAblation()));
            addComponentSubMenu.add(laserAblationOption);

            JMenuItem apicalGradientOption = new JMenuItem("Apical Gradient");
            apicalGradientOption.addActionListener(e -> SelectionEvents.addComponentToSelected(new ApicalGradient()));
            addComponentSubMenu.add(apicalGradientOption);

            JMenuItem lateralGradientOption = new JMenuItem("Lateral Gradient");
            lateralGradientOption.addActionListener(e -> SelectionEvents.addComponentToSelected(new LateralGradient()));
            addComponentSubMenu.add(lateralGradientOption);*/

        menu3.add(addComponentSubMenu);

        JMenu removeComponentSubMenu = new JMenu("Remove Component");
        for (Component c : getSelectedComponents()) {
            JMenuItem option = new JMenuItem(c.getClass().getSimpleName());
            option.addActionListener(e -> SelectionEvents.removeComponentFromSelected(c.getClass()));
            removeComponentSubMenu.add(option);
        }
        menu3.add(removeComponentSubMenu);


        menuBar.add(menu3);

        JMenu objectMenu = new JMenu("Object");
        JMenuItem addBasicObject = new JMenuItem("Add Basic Mesh");
        addBasicObject.addActionListener(e-> new Entity("Basic Mesh", 0, MODEL).
                with(new BoxDebugMesh().build()));
        JMenuItem addRingMesh = new JMenuItem("Add Ring Mesh");
        addRingMesh.addActionListener(e-> new Entity("Ring Mesh", 0, MODEL).
        with(new RingMesh()));
        JMenuItem addHexMesh = new JMenuItem("Add Hexagon Mesh");
        addHexMesh.addActionListener(e-> new Entity("Hexagon Mesh", 0, MODEL).
                with(new HexMesh()));

        objectMenu.add(addBasicObject);
        objectMenu.add(addRingMesh);
        objectMenu.add(addHexMesh);

        menuBar.add(objectMenu);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem keysHelp = new JMenuItem("Keyboard Shortcuts");
        keysHelp.addActionListener(e-> new KeyCommandsHelpPopUp());
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


    private void CreateDisplay() {
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       //this line is necessary for the code to function.
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false); //Until after debugging
        frame.getContentPane().setBackground( Color.black );

    }

    private void createCanvas() {
        if(canvas!=null){
            frame.remove(canvas);
        }
        canvas = new SimulationCanvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));
        canvas.setMaximumSize(new Dimension(width, height));
        frame.add(canvas);
    }

    public SimulationCanvas GetCanvas()
    {
        if(canvas == null) createCanvas();
        return canvas;
    }
}
