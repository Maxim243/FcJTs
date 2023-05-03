package automation.services.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class CurrencyNamesResponseDTO extends BaseResponseDTO {

    private List<String> currencyNames;
}
