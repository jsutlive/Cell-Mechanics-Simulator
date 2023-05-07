package framework.data;

import framework.utilities.Debug;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageHandler {

    BufferedImage image;
    File file;
    public ImageHandler(BufferedImage image, File file){
       this.image = image;
       this.file = file;
    }

    public void write(){
        try {
            ImageIO.write(image, "JPG",
                    file);
        } catch (IOException e) {
            Debug.LogError("Failed to save image");
            e.printStackTrace();
        }
    }

    public static Image loadImage(String filename){
        InputStream file = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
        Image image = null;
        try {
            assert file != null;
            image = ImageIO.read(file);
        } catch (IOException e) {
            Debug.LogError("Failed to load image");
            e.printStackTrace();
        }
        return image;
    }
}
