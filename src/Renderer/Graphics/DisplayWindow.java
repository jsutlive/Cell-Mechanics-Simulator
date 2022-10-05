package Renderer.Graphics;

import Framework.Engine;
import Framework.States.State;
import Input.InputEvents;
import Input.InputPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DisplayWindow
{
    private JFrame frame;
    private Canvas canvas;
    private String title;
    private int width, height;

    public DisplayWindow(String _title, int _width, int _height)
    {
        this.title = _title;
        this.width = _width;
        this.height = _height;
        CreateDisplay();
        CreateCanvas();
        frame.add(canvas);

        InputPanel inputPanel = new InputPanel();
        frame.add(inputPanel.getPanel(), BorderLayout.WEST);
        frame.addKeyListener(new InputEvents());
        frame.setFocusable(true);
        frame.requestFocus();
        frame.pack();
        frame.setIconImage(new ImageIcon("assets/cell.png").getImage());
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
        canvas = new Canvas();
        canvas.setSize(new Dimension(width, height));
        canvas.setMinimumSize(new Dimension(width, height));
    }

    public Canvas GetCanvas()
    {
        return canvas;
    }
}
