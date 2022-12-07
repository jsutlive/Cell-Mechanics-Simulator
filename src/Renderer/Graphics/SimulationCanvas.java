package Renderer.Graphics;

import Framework.Object.Tag;
import Input.SelectionEvents;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SimulationCanvas extends Canvas {

    BufferedImage image;

    public void prepareImageForExport(){
        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        this.paintAll(graphics);
    }

    public void exportImage(String imageName) {
        try {
            System.out.println("Exporting image: " + imageName);
            FileOutputStream out = new FileOutputStream("Assets/" + imageName + ".png");
            ImageIO.write(image, "png", out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}