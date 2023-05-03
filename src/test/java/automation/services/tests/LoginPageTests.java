package automation.services.tests;

import automation.services.actions.RegistrationActions;
import automation.services.dto.BaseResponseDTO;
import org.apache.http.Header;
import org.testng.annotations.Test;

import java.util.Optional;

public class LoginPageTests {
    @Test
    public void login(){
        BaseResponseDTO baseResponseDTO = RegistrationActions.loginUser("John12347","John12347");
        Optional <Header> authToken = baseResponseDTO.getResponseHeaders().stream().filter(header -> header.getName().equals("AuthToken")).findFirst();
        if (authToken.isPresent()) {
            Header header = authToken.get();
            String authTokenString = header.getValue();
//            authToken.addHeader("EnvironmentToken", environmentToken);
            System.out.println(authTokenString);
        }
    }
}
