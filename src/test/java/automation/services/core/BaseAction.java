package automation.services.core;

import automation.services.dto.BaseResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import automation.core.PropertyReader;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;


public class BaseAction {

    private static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    protected static final <R extends BaseResponseDTO> R get(final String url,
                                                              final Map<String, String> queryParameters, final Map<String, String> headers,
                                                              final Class<R> responseType) {
// url validation
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("url '" + url + "' is not valid");
        }
        URIBuilder uriBuilder = null;

        try {
            uriBuilder = new URIBuilder(url);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("URL '" + url + "' is not valid", e);
        }

// add query parameters
        if (queryParameters != null) {
            for (Map.Entry<String, String> queryParameter : queryParameters.entrySet()) {
                uriBuilder.addParameter(queryParameter.getKey(), queryParameter.getValue());
            }
        }

        HttpGet get;

        try {
            URI uri = uriBuilder.build();

            get = new HttpGet(uri);
        } catch (URISyntaxException e1) {
            throw new IllegalStateException(e1);
        }

// add headers
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                get.addHeader(header.getKey(), header.getValue());
            }
        }

// add environment token
        if (get.getFirstHeader("EnvironmentToken") == null) {
            BaseResponseDTO environmentTokenResponse = getEnvironmentToken();
            Optional<Header> optionalEnvironmentTokenHeader = environmentTokenResponse.getResponseHeaders().stream()
                    .filter(header -> header.getName().equals("EnvironmentToken")).findFirst();
            if (optionalEnvironmentTokenHeader.isPresent()) {
                Header header = optionalEnvironmentTokenHeader.get();
                String environmentToken = header.getValue();
                get.addHeader("EnvironmentToken", environmentToken);
            }
        }

        return doRequest(get, responseType);
    }

    protected static final <R extends BaseResponseDTO> R post(final String url,
                                                              final Map<String, String> queryParameters, final Map<String, String> headers, final Object requestBody,
                                                              final Class<R> responseType) {
// url validation
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("url '" + url + "' is not valid");
        }
        URIBuilder uriBuilder = null;

        try {
            uriBuilder = new URIBuilder(url);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("URL '" + url + "' is not valid", e);
        }

// add query parameters
        if (queryParameters != null) {
            for (Map.Entry<String, String> queryParameter : queryParameters.entrySet()) {
                uriBuilder.addParameter(queryParameter.getKey(), queryParameter.getValue());
            }
        }

        HttpPost post;

        try {
            URI uri = uriBuilder.build();

            post = new HttpPost(uri);
        } catch (URISyntaxException e1) {
            throw new IllegalStateException(e1);
        }

// add headers
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                post.addHeader(header.getKey(), header.getValue());
            }
        }

// add environment token
        if (post.getFirstHeader("EnvironmentToken") == null) {
            BaseResponseDTO environmentTokenResponse = getEnvironmentToken();
            Optional<Header> optionalEnvironmentTokenHeader = environmentTokenResponse.getResponseHeaders().stream()
                    .filter(header -> header.getName().equals("EnvironmentToken")).findFirst();
            if (optionalEnvironmentTokenHeader.isPresent()) {
                Header header = optionalEnvironmentTokenHeader.get();
                String environmentToken = header.getValue();
                post.addHeader("EnvironmentToken", environmentToken);
            }
        }

// add request body
        if (requestBody != null) {
            try {
                OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
                post.setEntity(new StringEntity(OBJECT_MAPPER.writeValueAsString(requestBody)));
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        return doRequest(post, responseType);
    }

    private static BaseResponseDTO getEnvironmentToken() {
        try {
            URIBuilder uriBuilder = new URIBuilder(PropertyReader.getProperty("environmentLoginUrl"));

            uriBuilder.addParameter("environmentname", PropertyReader.getProperty("environmentName"));
            uriBuilder.addParameter("password", PropertyReader.getProperty("environmentPassword"));

            HttpPost post = new HttpPost(uriBuilder.build());

            return doRequest(post, BaseResponseDTO.class);
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <R extends BaseResponseDTO> R doRequest(final HttpRequestBase post, final Class<R> responseType) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(post.getMethod() + " " + post.getURI().toASCIIString() + "\r\n\r\n");

        Header[] allHeaders = post.getAllHeaders();

        for (Header header : allHeaders) {
            stringBuilder.append(header.getName() + ": " + header.getValue());
        }
        System.out.println("Request:\r\n\r\n" + stringBuilder.toString());

        try (CloseableHttpResponse httpResponse = HTTP_CLIENT.execute(post)) {
            HttpEntity entity = httpResponse.getEntity();
            R response = null;

            if (entity != null) {
                String rawHttpResponseBody = EntityUtils.toString(entity);
                response = OBJECT_MAPPER.readValue(rawHttpResponseBody, responseType);
            } else {
                response = (R) new BaseResponseDTO();
            }

            response.setResponseHeaders(Arrays.asList(httpResponse.getAllHeaders()));
            response.setResponseStatusCode(httpResponse.getStatusLine().getStatusCode());

            return response;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}

