package automation.services.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.http.Header;

import java.util.List;

@Setter
@Getter
@ToString
public class BaseResponseDTO {
    private Integer responseStatusCode;
    private String responseString;
    private List<Header> responseHeaders;
    private String errorDescription;

}

