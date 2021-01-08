package configuration;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoadXmlConf {
    private static final Logger logger = LogManager.getLogger(LoadXmlConf.class);

    public static MongoConfig getConfigIstance(String filename){

        try {
            InputStream in = LoadXmlConf.class.getResource("/conf/"+filename+".xml").openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String xml = "";
            for (String line; (line = reader.readLine()) != null; xml += line+"\n");

            return (MongoConfig) createXStream().fromXML(xml);
        } catch (IOException ioe) {
            logger.error("Exception happened! ", ioe);
        }
        return null;
    }

    private static XStream createXStream(){
        XStream xstream = new XStream(new StaxDriver());
        xstream.processAnnotations(MongoConfig.class);
        xstream.processAnnotations(MongoConfig.Replica.class);
        xstream.useAttributeFor(MongoConfig.class, "mode");
        xstream.useAttributeFor(MongoConfig.Replica.class, "ip");
        xstream.useAttributeFor(MongoConfig.Replica.class, "port");
        //xstream.alias("ConfigurationService", MongoConfig.class);
        /*
        xs.useAttributeFor(ConfigurationService.class, "mongoIp");
        xs.useAttributeFor(ConfigurationService.class, "mongoPort");
         */

        xstream.addPermission(NoTypePermission.NONE);
        // allow some basics
        xstream.addPermission(NullPermission.NULL);
        xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
        xstream.allowTypeHierarchy(Collection.class);
        // allow any type from the same package
        xstream.allowTypesByWildcard(new String[] {"configuration.*"});
        return xstream;
    }

    public static void main(String[] args) {
        System.out.println(LoadXmlConf.getConfigIstance("mongoConfig"));
    }
}
