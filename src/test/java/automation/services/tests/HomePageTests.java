package automation.services.tests;

import MyFirstHttpProgram.DTO.Valute;
import MyFirstHttpProgram.Main;
import automation.core.ExcelDataProvider;
import automation.services.actions.BNMActions;
import automation.services.actions.HomePageActions;
import automation.services.actions.RegistrationActions;
import automation.services.dto.*;
import automation.services.dto.PostDTO.QueryPostDTO;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.*;

import static automation.UtilsMethods.ExchangeRatesMethodes.*;
import static automation.services.actions.HomePageActions.getExchangeRates;

public class HomePageTests {
    private static final Map<String, String> HEADERS = new HashMap<>();
    private static final Map<String, String> QUERYMAP = new HashMap<>();
    private static final Map<String, String> QUERYMAPFORPOST = new HashMap<>();
    private static final Logger logger = Logger.getLogger(HomePageTests.class);

    @BeforeMethod()
    public void login() {
        BaseResponseDTO baseResponseDTO = RegistrationActions.loginUser("John12347", "John12347");
        Optional<Header> authToken = baseResponseDTO.getResponseHeaders().stream().filter(header -> header.getName().equals("AuthToken")).findFirst();
        PropertyConfigurator.configure("log4j.properties");
        logger.info("log in enviroment page");
        if (authToken.isPresent()) {
            Header header = authToken.get();
            String authTokenString = header.getValue();
            HEADERS.put("AuthToken", authTokenString);
            HEADERS.put(HttpHeaders.CONTENT_TYPE, "application/json");
            HEADERS.put(HttpHeaders.ACCEPT, "application/json");
        }
    }


    @Test(dataProvider = "homePageDataProvider", dataProviderClass = ExcelDataProvider.class)
    public void test(Map<String, String> inputMatrix) {
        String testName = inputMatrix.get("TestName");
        QUERYMAP.put("datefrom", inputMatrix.get("date from"));
        QUERYMAP.put("dateto", inputMatrix.get("date to"));
        QUERYMAP.put("currencyvaluefrom", inputMatrix.get("Currency from"));
        QUERYMAP.put("currencyvalueto", inputMatrix.get("Currency to"));
        QUERYMAP.put("charcodes", inputMatrix.get("Currency Char Code"));
        QUERYMAP.put("currencynames", inputMatrix.get("Currency Char Name "));
        QUERYMAP.put("lang", inputMatrix.get("lang"));

        QUERYMAPFORPOST.put("lang", inputMatrix.get("lang"));

        ValCursBNMDTO valCursBNMDTO = BNMActions.getExchangeRatesBNM(inputMatrix.get("lang"));
        ExchangeRatesDTO exchangeRatesDTO = getExchangeRates(QUERYMAP, HEADERS);

        QueryPostDTO queryPostDTO = new QueryPostDTO(inputMatrix.get("date from"),
                inputMatrix.get("date to"), inputMatrix.get("Currency Char Code"),
                inputMatrix.get("Currency Char Name "), inputMatrix.get("Currency from"),
                inputMatrix.get("Currency to"), null);

        ExchangeRatesDTO exchangeRatesDTOPost = HomePageActions.postExchangeRates(QUERYMAPFORPOST, HEADERS, queryPostDTO);

        String errorDescriptionDateFrom = inputMatrix.get("errorDescription").replace("DD.MM.YYYY", inputMatrix.get("date from"));
        String errorDescriptionValue = inputMatrix.get("errorDescription").replace("XXX", inputMatrix.get("Currency from"));
        String errorDescriptionDateTo = inputMatrix.get("errorDescription").replace("DD.MM.YYYY", inputMatrix.get("date to"));

        switch (testName) {
            // a mistake in test case
            case "date older than 01.01.1994" -> {
                Assert.assertEquals(exchangeRatesDTO.getErrorDescription(), errorDescriptionDateFrom);
                Assert.assertEquals(exchangeRatesDTO.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("status code")));
            }

            case "'To date older than 01.01.1994" -> {
                Assert.assertEquals(exchangeRatesDTO.getErrorDescription(), errorDescriptionDateTo);
                Assert.assertEquals(exchangeRatesDTO.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("status code")));
            }

            // should verify 30 days
            case "Valid date currency rates" -> {
                Assert.assertEquals(exchangeRatesDTO.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("status code")));
                Assert.assertEquals(getDateJson(exchangeRatesDTO), inputMatrix.get("date from"));
            }

            case "From date  greater than current date" -> {
                logger.info(errorDescriptionDateFrom);
                Assert.assertEquals(exchangeRatesDTO.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("status code")));
                Assert.assertEquals(exchangeRatesDTO.getErrorDescription(), errorDescriptionDateFrom);
            }

            // bug, CRA does not include today's date
            case "From Date is set by default" -> {
                Assert.assertEquals(exchangeRatesDTO.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("status code")));
                Assert.assertEquals(minus1Month(inputMatrix.get("date to")), getDateJson(exchangeRatesDTO));
            }

            case "To Date is set by default" -> {
                Assert.assertEquals(fromLocalDateToStringDate(LocalDate.now()), getDateJson(exchangeRatesDTO));
                Assert.assertEquals(exchangeRatesDTO.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("status code")));
            }
            case "If only 60 dates are displayed(1 step)", "Char codes are not specified" -> {
                Assert.assertEquals(exchangeRatesDTO.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("status code")));
                Assert.assertTrue(checkRangeIfDataIsPresent(inputMatrix.get("date from"), 1, exchangeRatesDTO));
            }

            case "If only 60 dates are displayed(2 step)" -> {
                Assert.assertTrue(checkRangeIfDataIsPresent(inputMatrix.get("date from"), 4, exchangeRatesDTO));
                Assert.assertEquals(exchangeRatesDTO.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("status code")));
            }

            case "Curency rates for valid char codes(1 step)" -> {
                Assert.assertEquals(exchangeRatesDTO.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("status code")));
                Assert.assertTrue(checkRangeIfDataIsPresent(inputMatrix.get("date from"), 1, exchangeRatesDTO));
                Assert.assertTrue(exchangeRatesDTO.getCurrenciesRange().stream()
                        .map(CurrencyRange::getCurrencies)
                        .flatMap(Collection::stream)
                        .map(CurrencyDTO::getCharCode)
                        .allMatch(charCode -> charCode.contains("EUR")));
            }

            case "Curency rates for valid char codes(2 step)" -> {
                getCharCodeList(exchangeRatesDTO);
                Assert.assertEquals(exchangeRatesDTO.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("status code")));
                Assert.assertTrue(checkRangeIfDataIsPresent(inputMatrix.get("date from"), 1, exchangeRatesDTO));
                Assert.assertTrue(charCodeList.stream()
                        .allMatch(charCode -> charCode.contains("EUR") || charCode.contains("CAD")));
                Assert.assertTrue(charCodeList.contains("EUR") && charCodeList.contains("CAD"));
            }

            case "Invalid char codes(1 step)", "Invalid char codes(2 step)" -> {
                getCharCodeList(exchangeRatesDTO);
                Assert.assertEquals(exchangeRatesDTO.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("status code")));
                Assert.assertTrue(charCodeList.isEmpty());
            }

            case "Use both of cases(char code) (1 step)" -> {
                getCharCodeList(exchangeRatesDTO);
                Assert.assertTrue(charCodeList.stream()
                        .allMatch(charCode -> charCode.contains("EUR")));
                Assert.assertTrue(checkRangeIfDataIsEmpty(exchangeRatesDTO));
                Assert.assertEquals(exchangeRatesDTO.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("status code")));
            }

            case "Use both of cases(char code)(2-3 steps)" -> {
                getCharCodeList(exchangeRatesDTO);
                Assert.assertTrue(charCodeList.stream()
                        .allMatch(charCode -> charCode.contains("EUR") || charCode.contains("CAD")));
                Assert.assertTrue(charCodeList.contains("EUR") && charCodeList.contains("CAD"));
                Assert.assertTrue(checkRangeIfDataIsEmpty(exchangeRatesDTO));
                Assert.assertEquals(exchangeRatesDTO.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("status code")));
            }
            case "Currency names are not specified", "Curency rates for valid currency names(step 1)", "Use both of cases(1step)" -> {
                getCharNameList(exchangeRatesDTO);
                Assert.assertTrue(charNameList.stream()
                        .allMatch(charNameList -> charNameList.contains("Euro")));
                Assert.assertEquals(exchangeRatesDTO.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("status code")));
                Assert.assertTrue(checkRangeIfDataIsEmpty(exchangeRatesDTO));
            }

            // only euro
            case "Curency rates for valid currency names(step 2)", "Use both of cases(2 step)", "Use both of cases(3 step)" -> {
                getCharNameList(exchangeRatesDTO);
                Assert.assertTrue(charNameList.stream()
                        .allMatch(charNameList -> charNameList.contains("Euro") || charNameList.contains("Dollar")));
                Assert.assertTrue(charNameList.contains("Euro") && charNameList.contains("Dollar"));
                Assert.assertEquals(exchangeRatesDTO.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("status code")));
                Assert.assertTrue(checkRangeIfDataIsEmpty(exchangeRatesDTO));
            }

            case "Curency rates for invalid currency names(1 step)", "Curency rates for invalid currency names(2 step)" -> {
                getCharNameList(exchangeRatesDTO);
                Assert.assertTrue(charNameList.isEmpty());
                Assert.assertEquals(exchangeRatesDTO.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("status code")));
            }

            case "Values by specifiying currency values range(value 0)" -> {
                Assert.assertEquals(exchangeRatesDTO.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("status code")));
                Assert.assertTrue(checkRangeIfDataIsPresent(inputMatrix.get("date from"), 1, exchangeRatesDTO));
                Assert.assertTrue(charCodeList.stream()
                        .allMatch(charNameList -> charCodeList.contains("EUR")));
            }

            case "Values by specifiying currency values range(eg. 19.2)" -> {
                Assert.assertEquals(exchangeRatesDTO.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("status code")));
                Assert.assertTrue(checkRangeIfDataIsPresent(inputMatrix.get("date from"), 1, exchangeRatesDTO));
                Assert.assertTrue(charCodeList.stream()
                        .allMatch(charNameList -> charCodeList.contains("EUR")));
                Assert.assertTrue(valueNameList.stream().allMatch(value -> value >= Double.parseDouble(inputMatrix.get("Currency from"))));
            }

// get 500 status code
            case "Values by specifiying currency values range(invalid)", "Char code and currency names does not correspond to each other" -> {
                Assert.assertEquals(exchangeRatesDTO.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("status code")));
                Assert.assertEquals(exchangeRatesDTO.getErrorDescription(), errorDescriptionValue);
            }

// get 500 status code
            case "Only English is entered" -> {
                Assert.assertEquals(exchangeRatesDTO.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("status code")));
                Assert.assertEquals(exchangeRatesDTO.getErrorDescription(), inputMatrix.get("errorDescription"));
                String bnmLang = valCursBNMDTO.getValutes().stream()
                        .map(Valute::getName)
                        .filter(valute -> valute.equals(inputMatrix.get("Currency Char Name ")))
                        .findFirst()
                        .toString();

                String craLangNameValue = exchangeRatesDTO.getCurrenciesRange().stream()
                        .flatMap(exchangeRates -> exchangeRates.getCurrencies().stream())
                        .map(CurrencyDTO::getName)
                        .filter(valute -> valute.equals(inputMatrix.get("Currency Char Name ")))
                        .findFirst()
                        .toString();
                Assert.assertEquals(bnmLang, craLangNameValue);

            }
            case "Result in selected language(en)", "Result in selected language(ru)", "Result in selected language(ro)" -> {
                String bnmLang = valCursBNMDTO.getValutes().stream()
                        .map(Valute::getName)
                        .filter(valute -> valute.equals(inputMatrix.get("Currency Char Name ")))
                        .findFirst()
                        .toString();
                String craLangNameValue = exchangeRatesDTO.getCurrenciesRange().stream()
                        .flatMap(exchangeRates -> exchangeRates.getCurrencies().stream())
                        .map(CurrencyDTO::getName)
                        .filter(valute -> valute.equals(inputMatrix.get("Currency Char Name ")))
                        .findFirst()
                        .toString();
                Assert.assertEquals(bnmLang, craLangNameValue);
                Assert.assertEquals(exchangeRatesDTO.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("status code")));
            }

            case "Result in selected language(en)(post)", "Result in selected language(ro)(post)", "Result in selected language(ru)(post)" -> {
                String bnmLang = valCursBNMDTO.getValutes().stream()
                        .map(Valute::getName)
                        .filter(valute -> valute.equals(inputMatrix.get("Currency Char Name ")))
                        .findFirst()
                        .toString();

                String craLangNameValue = exchangeRatesDTOPost.getCurrenciesRange().stream()
                        .flatMap(exchangeRates -> exchangeRates.getCurrencies().stream())
                        .map(CurrencyDTO::getName)
                        .filter(valute -> valute.equals(inputMatrix.get("Currency Char Name ")))
                        .findFirst()
                        .toString();
                logger.info(craLangNameValue);
                Assert.assertEquals(bnmLang, craLangNameValue);
                Assert.assertEquals(exchangeRatesDTO.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("status code")));
            }
            case "Currency names are not specified (post)" -> {
                List<CurrencyDTO> currencyRanges = exchangeRatesDTOPost.getCurrenciesRange().stream()
                        .flatMap(currencyRange -> currencyRange.getCurrencies().stream())
                        .toList();
                Assert.assertEquals(exchangeRatesDTO.getResponseStatusCode(), Integer.parseInt(inputMatrix.get("status code")));
                Assert.assertFalse(currencyRanges.isEmpty());
            }

            case "Currency value depending on currency nominal(HUF)", "Currency value depending on currency nominal(MKD)" -> {
                ValCursBNMDTO valCursBNMDTOByCharCode = BNMActions.getExchangeRatesBNM(inputMatrix.get("lang"));
                List<Valute> valuteList = valCursBNMDTOByCharCode.getValutes().stream()
                        .filter(valute -> valute.getCharCode().equals(inputMatrix.get("Currency Char Code")))
                        .toList();

                List<CurrencyDTO> craValuteList = exchangeRatesDTO.getCurrenciesRange()
                        .stream()
                        .flatMap(currencyRange -> currencyRange.getCurrencies().stream())
                        .filter(currencyDTO -> currencyDTO.getCharCode().equals(inputMatrix.get("Currency Char Code")))
                        .toList();
                double valueCRA = Math.ceil(craValuteList.get(0).getValue() * 100) / 100;
                double valueBNM = Math.ceil(valuteList.get(0).getValue() /
                        valuteList.get(0).getNominal() * 100) / 100;
                Assert.assertEquals(valueCRA, valueBNM, "different values from CRA and BNM");
            }
        }
    }
}

