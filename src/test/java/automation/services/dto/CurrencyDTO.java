package automation.services.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CurrencyDTO {
    private Integer id;
    private Integer numCode;
    private String charCode;
    private Integer nominal;
    private String name;
    private Double value;
}
