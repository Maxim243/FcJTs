package automation.services.actions;

import MyFirstHttpProgram.DTO.Valute;
import automation.core.PropertyReader;
import automation.services.core.BaseAction;
import automation.services.dto.ValCursBNMDTO;
import automation.services.dto.ValuteBNMDTO;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BNMActions extends BaseAction {
    protected static final XStream xStream = new XStream();

    static {
        xStream.addPermission(AnyTypePermission.ANY);
        xStream.addPermission(NullPermission.NULL);   // allow "null"
        xStream.addPermission(PrimitiveTypePermission.PRIMITIVES); // allow primitive types
        xStream.processAnnotations(ValuteBNMDTO.class);
        xStream.processAnnotations(ValCursBNMDTO.class);
        xStream.addImplicitCollection(ValCursBNMDTO.class, "valutes", Valute.class);
    }

    public static ValCursBNMDTO getExchangeRatesBNM(String language) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get;
        switch (language) {
            case "en", "" -> get = new HttpGet(PropertyReader.getProperty("bnm-english"));
            case "ru" -> get = new HttpGet(PropertyReader.getProperty("bnm_russ"));
            case "ro" -> get = new HttpGet(PropertyReader.getProperty("bnm-roman"));
            default -> throw new IllegalStateException("Unexpected value: " + language);
        }

        try {
            HttpResponse response = client.execute(get);
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));

            String line = "";
            String fullResponse = "";
            while ((line = reader.readLine()) != null) {
                fullResponse += line + "\r\n";
            }
            return (ValCursBNMDTO) xStream.fromXML(fullResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public static ValCursBNMDTO getExchangeRatesByCharCode(Map<String, String> queryParameters, Map<String, String> headers) {
//        return BaseAction.get(PropertyReader.getProperty("bnm-english"),
//                queryParameters,
//                headers,
//                ValCursBNMDTO.class);
//    }
}
