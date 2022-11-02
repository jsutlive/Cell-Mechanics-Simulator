package Framework.Data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageWriter {

    BufferedImage image;
    File file;
    public ImageWriter(BufferedImage image,  File file){
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
}
