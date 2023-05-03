package automation.UtilsMethods;

import automation.services.core.BaseAction;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

public class RegistrationActionsPage extends BaseAction {
    private static final List<Character> characterList = new ArrayList<>();


    public static String validDataPositive(int intUpperCaseCount, int intLowerCaseCount, int numbersCount) {
        String upperCase = RandomStringUtils.randomAlphabetic(intUpperCaseCount).toUpperCase();
        String lowerCase = RandomStringUtils.randomAlphabetic(intLowerCaseCount).toLowerCase();
        lowerCase = lowerCase.concat(RandomStringUtils.randomNumeric(numbersCount));
        return lowerCase.concat(upperCase);
    }

    public static String tooShort(int intUpperCaseCount, int intLowerCaseCount, int numbersCount) {
        String upperCase = RandomStringUtils.randomAlphabetic(intUpperCaseCount).toUpperCase();
        String lowerCase = RandomStringUtils.randomAlphabetic(intLowerCaseCount).toLowerCase();
        lowerCase = lowerCase.concat(RandomStringUtils.randomNumeric(numbersCount));
        return lowerCase.concat(upperCase);
    }

    public static String withoutUpperCase(int intLowerCaseCount, int numbersCount) {
        String lowerCase = RandomStringUtils.randomAlphabetic(intLowerCaseCount).toLowerCase();
        return lowerCase.concat(RandomStringUtils.randomNumeric(numbersCount));
    }

    public static String withoutLowerCase(int upperCaseLettersCount, int numbersCount) {
        String upperCase = RandomStringUtils.randomAlphabetic(upperCaseLettersCount).toUpperCase();
        return upperCase.concat(RandomStringUtils.randomNumeric(numbersCount));
    }

    public static String withoutNumber(int intUpperCaseCount, int intLowerCaseCount) {
        String upperCase = RandomStringUtils.randomAlphabetic(intUpperCaseCount).toUpperCase();
        return upperCase.concat(RandomStringUtils.randomAlphabetic(intLowerCaseCount)).toLowerCase();
    }

    public static String leaveEmptyField() {
        return "";
    }

    public static String anotherLanguageWithSymbol(int lettersCount) {
        for (int i = 0; i < lettersCount; i++) {
            char letters = (char) ((Math.random() * ('я' - 'а')) + 'а');
            characterList.add(letters);
        }
        String russianWord =  characterList.stream()
                .map(String::valueOf)
                .reduce("", (integer, s) -> (integer + s));
        int i  = (int) ((Math.random() * (10 - 1)) + 1);
        return russianWord.concat(String.valueOf(i));
    }
}
