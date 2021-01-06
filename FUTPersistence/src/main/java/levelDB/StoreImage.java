package levelDB;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

import javax.imageio.ImageIO;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;
import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;

public class StoreImage {
    private static final Logger logger = LogManager.getLogger(StoreImage.class);
    private DB db = null;

    public void openDB() {
        Options options= new Options();
        options.createIfMissing(true);

        try {
            db = factory.open(new File("db"), options);
        } catch (IOException ioe) {
            logger.error("Exception happened! ", ioe);
        }
    }

    public void closeDB () {
        try {
            if (db!=null) {
                db.close();
                db = null;
            }
        } catch (IOException ioe) {
            logger.error("Exception happened! ", ioe);
        }
    }

    public DB getDB() { return db; }

    //utility
    public void putValue (String key, String value) { db.put(bytes(key), bytes(value)); }
    public String getValue (String key) {
        return asString(db.get(bytes(key)));
    }
    public void deleteValue(String key) {
        db.delete(bytes(key));
    }

    public boolean checkPresence (String imgUrl) throws IOException{
        openDB();
        if(getValue(imgUrl)==null){
            closeDB();
            return false;
        }
        closeDB();
        return true;
    }

    public BufferedImage findImg (String imgURL) throws IOException {
        if(!checkPresence(imgURL)){
            URL url = new URL(imgURL);
            BufferedImage image = ImageIO.read(url);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imgArray = baos.toByteArray();
            baos.close();
            openDB();
            String encodedImg = Base64.getEncoder().encodeToString(imgArray);
            putValue(imgURL, encodedImg);
            closeDB();
            System.out.println("L'ho messa!");
        }


        openDB();
        String v = getValue(imgURL);
        byte[] c = Base64.getDecoder().decode(v.getBytes());
        ByteArrayInputStream bis = new ByteArrayInputStream(c);
        BufferedImage image2 = ImageIO.read(bis);
        bis.close();
        System.out.println("L'ho presa!");
        closeDB();
        return image2;
    }
}
