package configuration;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;

import java.io.*;
import java.util.Collection;

public class LoadXmlConf {

    public MongoConfig getConfigIstance(String filename){

        try {

            InputStream in = LoadXmlConf.class.getResource("/conf/"+filename+".xml").openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String xml = new String();
            for (String line; (line = reader.readLine()) != null; xml += line+"\n");

            return (MongoConfig) createXStream().fromXML(xml);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final XStream createXStream(){
        XStream xstream = new XStream(new StaxDriver());
        xstream.alias("ConfigurationService", MongoConfig.class);
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
        LoadXmlConf lcf = new LoadXmlConf();
        System.out.println(lcf.getConfigIstance("mongoConfig"));
    }
}
