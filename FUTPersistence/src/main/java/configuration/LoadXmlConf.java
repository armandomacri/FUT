package configuration;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        XStream xs = new XStream(new DomDriver());
        xs.alias("ConfigurationService", MongoConfig.class);
        /*
        xs.useAttributeFor(ConfigurationService.class, "mongoIp");
        xs.useAttributeFor(ConfigurationService.class, "mongoPort");
         */
        return xs;
    }

    public static void main(String[] args) {
        LoadXmlConf lcf = new LoadXmlConf();
        System.out.println(lcf.getConfigIstance("mongoConfig"));
    }
}
