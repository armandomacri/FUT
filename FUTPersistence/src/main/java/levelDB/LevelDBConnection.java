package levelDB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

import java.io.File;
import java.io.IOException;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;
import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;

abstract class LevelDBConnection {
    private static final Logger logger = LogManager.getLogger(LevelDBConnection.class);
    protected DB db = null;

    public void openDB() {
        Options options= new Options();
        options.createIfMissing(true);

        try {
            db = factory.open(new File("cache"), options);
        } catch (IOException ioe) {
            logger.error("Exception happened! ", ioe);
        }
    }

    //public DB getDB() { return db; }
    public void putValue (String key, String value) { db.put(bytes(key), bytes(value)); }
    public String getValue (String key) {
        return asString(db.get(bytes(key)));
    }
    //public void deleteValue(String key) { db.delete(bytes(key)); }

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
}
