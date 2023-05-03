package automation;

import automation.services.actions.RegistrationActions;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        List<Character> characterList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            char letters = (char) ((Math.random() * (1105 - 1072)) + 1072);
            characterList.add(letters);
        }
        String value = characterList.stream()
                .map(String::valueOf)
                        .reduce("", (integer, s) -> (integer + s));
    }
}
