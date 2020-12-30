package levelDB;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

import javax.imageio.ImageIO;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;
import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;

public class StoreImage {
    private static DB db= null;

    public static void openDB() {
        Options options= new Options();
        options.createIfMissing(true);

        //check if it is already initialized
        if(db!=null) return;
        try {
            db = factory.open(new File("db"), options);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static void closeDB () {
        try {
            if (db!=null) {
                db.close();
                db= null;
            }
        } catch (IOException io) { io.printStackTrace(); }
    }

    public static DB getDB() { return db; }

    //utility
    public static void putValue (String key, String value) { db.put(bytes(key), bytes(value)); }
    public static String getValue (String key) {
        return asString(db.get(bytes(key)));
    }
    public static void deleteValue(String key) {
        db.delete(bytes(key));
    }

    public BufferedImage findImg (String imgURL) throws IOException {
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

        openDB();
        String v = getValue(imgURL);
        byte[] c = Base64.getDecoder().decode(v.getBytes());
        ByteArrayInputStream bis = new ByteArrayInputStream(c);
        BufferedImage image2 = ImageIO.read(bis);

        ImageIO.write(image2, "png", new File("prova.png"));
        bis.close();

        return image2;
    }
}
