package automation.services.dto.PostDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class QueryPostDTO {
    private String datefrom;
    private String dateto;
    private String charcodes;
    private String currencynames;
    private String currencyvaluefrom;
    private String currencyvalueto;
    private String basecurrencycharcode;
}
