package MyFirstHttpProgram;

import MyFirstHttpProgram.DTO.ValCurs;
import MyFirstHttpProgram.DTO.Valute;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

//@Setter
//@Getter
public class Main {


    public static void main(String[] args) throws IOException {
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        HttpGet httpRequest = new HttpGet(PropertyReader.getProperty("bnmUrl"));
//        HttpResponse httpResponse = httpClient.execute(httpRequest);
//
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));
//
//        String line = "";
//        String fullResponse = "";
//
//        while ((line = bufferedReader.readLine()) != null) {
//            fullResponse += line + "\r\n";
//        }
//        XStream xStream = new XStream();
//        xStream.addPermission(AnyTypePermission.ANY);
//        xStream.processAnnotations(ValCurs.class);
//        xStream.processAnnotations(Valute.class);
//        xStream.addImplicitCollection(ValCurs.class, "valutes", Valute.class);
//        ValCurs valCurs = (ValCurs) xStream.fromXML(fullResponse);
//
//
////      valCurs.getValutes().forEach(s -> System.out.println(s.getName()));
//        String toXML = xStream.toXML(valCurs);
//        FileWriter fileWriter = new FileWriter("target/myFile.xml");
//        fileWriter.write(toXML);
//        fileWriter.close();
//
//        HttpPost post = null;
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
//        String JsonString = objectMapper.writeValueAsString(valCurs);
//        ValCurs valCurs1 = objectMapper.readValue();
//        post.setEntity(new StringEntity(objectMapper.writeValueAsString(JsonString)));
//        System.out.println(JsonString);
//        objectMapper.writeValue(new File("target/Valute.json"), valCurs);

                // Log some messages

        Logger logger = Logger.getLogger(Main.class);
//        PropertyConfigurator.configure("log4j.properties");
                logger.debug("Debug message");
                logger.info("Info message");
                logger.warn("Warning message");
                logger.error("Error message");
                logger.fatal("Fatal message");
        System.out.println(
        );
        
            }

    }

