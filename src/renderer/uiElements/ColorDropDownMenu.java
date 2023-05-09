package renderer.uiElements;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

import static java.awt.Color.*;

public class ColorDropDownMenu {
    private final static String[] colorStrings = new String[]
            {"--", "Blue", "Cyan", "Green", "Magenta", "Orange", "Pink", "Red", "White"};
    private final static Color[] colors = new Color[]
            {null, BLUE, CYAN, GREEN, MAGENTA, ORANGE, PINK, RED, WHITE};

    public final static HashMap<String, Color> colorDictionary = new HashMap<>();

    private JComboBox<String> menu;

    public ColorDropDownMenu(Color value){
        setMenu(new JComboBox<>(colorStrings));
        for (int i = 0; i< colorStrings.length; i++) {
            colorDictionary.put(colorStrings[i], colors[i]);
            if(value == colors[i]) menu.setSelectedIndex(i);
        }
    }

    public ColorDropDownMenu(){
        setMenu(new JComboBox<>(colorStrings));
        menu.setSelectedIndex(0);
    }

    public JComboBox<String> getMenu() {
        return menu;
    }

    public void setMenu(JComboBox<String> menu) {
        this.menu = menu;
    }
}
