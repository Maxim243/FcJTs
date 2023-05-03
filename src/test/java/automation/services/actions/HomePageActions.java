package automation.services.actions;

import automation.core.PropertyReader;
import automation.services.core.BaseAction;
import automation.services.dto.ExchangeRatesDTO;

import java.util.Map;

public class HomePageActions extends BaseAction {
    public static ExchangeRatesDTO getExchangeRates(Map<String, String> queryParameters, Map<String, String> headers) {
        return BaseAction.get(PropertyReader.getProperty("exchangeRates"),
                queryParameters,
                headers,
                ExchangeRatesDTO.class);
    }

    public static ExchangeRatesDTO postExchangeRates(Map<String, String> queryParameters, Map<String, String> headers, Object object) {
        return BaseAction.post(PropertyReader.getProperty("exchangeRates"),
                queryParameters,
                headers,
                object,
                ExchangeRatesDTO.class);
    }
}
