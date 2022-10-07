package Renderer.Graphics;

import Framework.Engine;
import Framework.States.State;
import Input.InputEvents;
import Input.InputPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class DisplayWindow
{
    private JFrame frame;
    private SimulationCanvas canvas;
    private JMenuBar menuBar;
    private String title;
    private int width, height;

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

        InputPanel inputPanel = new InputPanel();
        frame.setIconImage(new ImageIcon("assets/cell.png").getImage());
        frame.add(inputPanel.getPanel(), BorderLayout.WEST);
        InputEvents input = new InputEvents();
        frame.addKeyListener(input);
        canvas.addMouseListener(input);
        frame.setFocusable(true);
        frame.requestFocus();
        frame.pack();
    }

    private void createJMenuBar(){
        menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem exportItem = new JMenuItem("Export Image", KeyEvent.VK_P);
        exportItem.addActionListener(e-> canvas.exportImage("TEST"));
        menu.add(exportItem);
        menuBar.add(menu);
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
    }

    public SimulationCanvas GetCanvas()
    {
        return canvas;
    }
}
