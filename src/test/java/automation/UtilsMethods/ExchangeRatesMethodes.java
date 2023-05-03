package automation.UtilsMethods;

import automation.services.dto.CurrencyDTO;
import automation.services.dto.CurrencyRange;
import automation.services.dto.ExchangeRatesDTO;
import automation.services.dto.ValuteBNMDTO;
import org.testng.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExchangeRatesMethodes {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static List<String> charCodeList = new ArrayList<>();
    public static List<String> charNameList = new ArrayList<>();
    public static List<Double> valueNameList = new ArrayList<>();

    public static void putQuery(Map<String, String> inputMatrix, Map<String, String> queryMap) {
        queryMap.put("datefrom", inputMatrix.get("date from"));
        queryMap.put("dateto", inputMatrix.get("date to"));
        queryMap.put("currencyvaluefrom", inputMatrix.get("Currency from"));
        queryMap.put("currencyvalueto", inputMatrix.get("Currency to"));
        queryMap.put("charcodes", inputMatrix.get("Currency Char Code"));
        queryMap.put("currencynames", inputMatrix.get("Currency Char Name "));
        queryMap.put("lang", inputMatrix.get("lang"));
    }

    public static String minus1Month(String inputMatrixGetDate) {
        LocalDate localDate = LocalDate.parse(inputMatrixGetDate, FORMATTER);
        return FORMATTER.format(localDate.minusMonths(1));
    }

    public static String plusMonth(String inputMatrixGetDate, int month) {
        if (!(inputMatrixGetDate.equals(""))) {
            LocalDate localDate = LocalDate.parse(inputMatrixGetDate, FORMATTER);
            return FORMATTER.format(localDate.plusMonths(month));
        }
        return "";
    }

    public static String fromLocalDateToStringDate(LocalDate localDate) {
        return FORMATTER.format(localDate);
    }

    public static String getDateJson(ExchangeRatesDTO exchangeRatesDTO) {
        return exchangeRatesDTO.getCurrenciesRange().stream()
                .map(CurrencyRange::getDate)
                .findFirst()
                .get();
    }

    public static boolean checkRangeIfDataIsPresent(String dateFrom, int months, ExchangeRatesDTO exchangeRatesDTO) {

        String plusMonthVariable = plusMonth(dateFrom, months);
        LocalDate plus = LocalDate.parse(plusMonthVariable, FORMATTER);
        List<String> datalist = exchangeRatesDTO.getCurrenciesRange().stream()
                .map(CurrencyRange::getDate).toList();
        LocalDate localDateToList = LocalDate.parse(datalist.get(datalist.size() - 1), FORMATTER);
        LocalDate localDateFromList = LocalDate.parse(datalist.get(0), FORMATTER);
        LocalDate myLocalDateFrom = LocalDate.parse(dateFrom, FORMATTER);
        if (months < 2) {
            plusMonthVariable = plusMonth(dateFrom, months);

        } else {
            plusMonthVariable = plusMonth(dateFrom, 2);
        }

        plus = LocalDate.parse(plusMonthVariable, FORMATTER);
        return (localDateToList.isBefore(plus) || localDateToList.equals(plus)) && (localDateFromList.isAfter(myLocalDateFrom)
                || localDateFromList.equals(myLocalDateFrom));

    }

    public static boolean checkRangeIfDataIsEmpty(ExchangeRatesDTO exchangeRatesDTO) {
        List<String> datalist = exchangeRatesDTO.getCurrenciesRange().stream().map(CurrencyRange::getDate).toList();
        LocalDate localDateToList = LocalDate.parse(datalist.get(datalist.size() - 1), FORMATTER);
        LocalDate localDateNow = LocalDate.now();
        LocalDate localDateNowPlusMonths = localDateNow.plusMonths(1);
        LocalDate localDateFromList = LocalDate.parse(datalist.get(0), FORMATTER);
        return (localDateFromList.equals(localDateNow) || localDateFromList.isAfter(localDateNow)) && localDateNowPlusMonths.equals(localDateToList)
                || localDateToList.isBefore(localDateNowPlusMonths);
    }


    public static void getCharCodeList(ExchangeRatesDTO exchangeRatesDTO) {

        charCodeList = exchangeRatesDTO.getCurrenciesRange().stream()
                .flatMap(exchangeRates -> exchangeRates.getCurrencies().stream())
                .map(CurrencyDTO::getCharCode)
                .toList();
    }

    public static void getCharNameList(ExchangeRatesDTO exchangeRatesDTO) {
        charNameList = exchangeRatesDTO.getCurrenciesRange().stream()
                .flatMap(exchangeRates -> exchangeRates.getCurrencies().stream())
                .map(CurrencyDTO::getName)
                .toList();
    }

    public static void getCharNameValue(ExchangeRatesDTO exchangeRatesDTO) {
        valueNameList = exchangeRatesDTO.getCurrenciesRange().stream()
                .flatMap(exchangeRates -> exchangeRates.getCurrencies().stream())
                .map(CurrencyDTO::getValue)
                .toList();
    }
}
