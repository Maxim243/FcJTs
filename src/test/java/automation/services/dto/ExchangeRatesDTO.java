package automation.services.dto;

import lombok.*;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ExchangeRatesDTO extends BaseResponseDTO {
    private List<CurrencyRange> currenciesRange;
    public ExchangeRatesDTO(List<CurrencyRange> currenciesRange) {
        this.currenciesRange = currenciesRange;
    }
}
