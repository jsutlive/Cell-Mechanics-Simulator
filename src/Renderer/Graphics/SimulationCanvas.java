package Renderer.Graphics;

import Renderer.Renderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
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
            FileOutputStream out = new FileOutputStream(new File(".").getAbsolutePath() + imageName + ".png");
            ImageIO.write(image, "png", out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}