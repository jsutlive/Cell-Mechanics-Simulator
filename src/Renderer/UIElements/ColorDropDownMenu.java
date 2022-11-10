package Renderer.UIElements;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

import static java.awt.Color.*;

public class ColorDropDownMenu {
    static String[] colorStrings = new String[]{"Blue", "Cyan", "Green", "Magenta", "Orange", "Red", "Pink" };
    static Color[] colors = new Color[]{BLUE, CYAN, GREEN, MAGENTA, ORANGE, RED, PINK};

    public static HashMap<String, Color> colorDictionary = new HashMap<>();

    private JComboBox menu;

    public ColorDropDownMenu(Color value){
        setMenu(new JComboBox(colorStrings));
        for (int i = 0; i< colorStrings.length; i++) {
            colorDictionary.put(colorStrings[i], colors[i]);
            if(value == colors[i]) menu.setSelectedIndex(i);
        }
    }

    public JComboBox getMenu() {
        return menu;
    }

    public void setMenu(JComboBox menu) {
        this.menu = menu;
    }
}
