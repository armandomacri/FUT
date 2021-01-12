package player;

import levelDB.StoreImage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

public class ImageService {
    private static final Logger logger = LogManager.getLogger(ImageService.class);
    private static StoreImage storeImage = new StoreImage();

    public BufferedImage get(String imgUrl){

        //image not present on leveldb
        if (!storeImage.isPresent(imgUrl)){
            try {
                URL url = new URL(imgUrl);
                BufferedImage image = ImageIO.read(url);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "png", baos);
                byte[] imgArray = baos.toByteArray();
                baos.close();
                String encodedImg = Base64.getEncoder().encodeToString(imgArray);
                storeImage.saveImage(imgUrl, encodedImg);
                return image;

            } catch (IOException ioe) {
                logger.error("Exception happened! ", ioe);
                return null;
            }
        }
        else {
            byte[] c = Base64.getDecoder().decode(storeImage.getImage(imgUrl).getBytes());
            ByteArrayInputStream bis = new ByteArrayInputStream(c);
            BufferedImage image = null;
            try {
                image = ImageIO.read(bis);
                bis.close();
            } catch (IOException ioe) {
                logger.error("Exception happened! ", ioe);
            }

            return image;
        }
    }
}
