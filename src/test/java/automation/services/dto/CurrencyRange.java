package automation.services.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
public class CurrencyRange {
    private List<CurrencyDTO> currencies;
    private String name;
    private String date;
}
