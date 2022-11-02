package Framework.Data;

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
            e.printStackTrace();
        }
    }

    public static Image loadImage(String filename){
        InputStream file = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
        Image image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
