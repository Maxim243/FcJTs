package automation.services.tests;

import automation.UtilsMethods.RegistrationActionsPage;
import automation.core.ExcelDataProvider;
import automation.services.actions.RegistrationActions;
import automation.services.dto.BaseResponseDTO;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class RegistrationPageTests {

    @Test(dataProvider = "RegistrationPageDataProvider", dataProviderClass = ExcelDataProvider.class)
    public void verifyUsernameAndPassword(Map<String, String> inputMatrix) {
        String userName;
        String password;
        String testName = inputMatrix.get("TestName");
        password = RegistrationActionsPage.validDataPositive(3, 3, 3);
        userName = RegistrationActionsPage.validDataPositive(3, 3, 3);

        int statusCode = Integer.parseInt(inputMatrix.get("statusCode"));

        switch (testName) {
            case "Valid username, valid password" -> {
                userName = RegistrationActionsPage.validDataPositive(3, 3, 3);
            }
            case "Too Short username and valid password" -> userName = RegistrationActionsPage.tooShort(1, 1, 1);
            case "Username without Upper Case" -> userName = RegistrationActionsPage.withoutUpperCase(3, 1);
            case "Username without Lower Case" -> userName = RegistrationActionsPage.withoutLowerCase(3, 1);
            case "Username without number" -> userName = RegistrationActionsPage.withoutNumber(3, 1);
            case "Empty username and valid password" -> userName = RegistrationActionsPage.leaveEmptyField();
            case "Another language for username and valid password" ->
                    userName = RegistrationActionsPage.anotherLanguageWithSymbol(3);
            case "Too Short password and valid username" -> password = RegistrationActionsPage.tooShort(1, 3, 1);
            case "Password without upper case characters and valid username" ->
                    password = RegistrationActionsPage.withoutUpperCase(5, 1);
            case "Password without lower case and valid username" ->
                    password = RegistrationActionsPage.withoutLowerCase(5, 1);
            case "Password without number and valid username" -> password = RegistrationActionsPage.withoutNumber(1, 5);
            case "Empty password and valid username" -> password = RegistrationActionsPage.leaveEmptyField();
            case "Another language for password and valid username" ->
                    password = RegistrationActionsPage.anotherLanguageWithSymbol(7);
            case "Registration based on identical username" -> {
                RegistrationActions.registerNewUser(userName, password);
                RegistrationActions.registerNewUser(userName, password);
            }
        }

        BaseResponseDTO registrationResult = RegistrationActions.registerNewUser(userName, password);
        if (testName.equals("Registration based on identical username")) {
            String errorDescription = inputMatrix.get("errorDescription").replace("username", userName);
            Assert.assertEquals(registrationResult.getErrorDescription(), errorDescription);
        } else if (statusCode == 400) {
            Assert.assertEquals(registrationResult.getErrorDescription(), inputMatrix.get("errorDescription"));
        }
        Assert.assertEquals(registrationResult.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("statusCode")));
        System.out.println(registrationResult.getResponseHeaders());

//        registrationResult.getResponseHeaders().forEach(System.out::println);
//        if (registrationResult.getResponseStatusCode() == 400) {
//            Assert.assertEquals(registrationResult.getErrorDescription(), inputMatrix.get("errorDescription"),
//                    "Validate that actual and expected error descriptions are correct");
//        } else if (registrationResult.getResponseStatusCode() == 201) {
//            Assert.assertEquals(registrationResult.getResponseStatusCode() + "", inputMatrix.get("statusCode"),
//                    "Validate that actual and expected response status codes are equal");
//        }
    }
}



