package automation.services.actions;

import automation.UtilsMethods.RegistrationActionsPage;
import automation.core.PropertyReader;
import automation.services.core.BaseAction;
import automation.services.dto.BaseResponseDTO;
import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class RegistrationActions extends BaseAction {
    private static final String registerNewUserUrl = PropertyReader.getProperty("registrationUrl");

    public static BaseResponseDTO registerNewUser(String username, String password) {
        Map<String, String> queryParameters = new HashMap<>();
        queryParameters.put("username", username);
        queryParameters.put("password", password);
        return BaseAction.post(registerNewUserUrl,
                queryParameters,
                null,
                null,
                BaseResponseDTO.class);
    }

    public static BaseResponseDTO loginUser(String username, String password) {
        Map<String, String> queryParameters = new HashMap<>();
        queryParameters.put("username", username);
        queryParameters.put("password", password);
        return BaseAction.post(PropertyReader.getProperty("loginUrl"),
                queryParameters,
                null,
                null,
                BaseResponseDTO.class);
    }
}


