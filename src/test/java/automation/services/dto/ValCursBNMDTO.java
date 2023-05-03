package automation.services.dto;

import MyFirstHttpProgram.DTO.Valute;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@XStreamAlias("ValCurs")
public class ValCursBNMDTO extends BaseResponseDTO {

    @XStreamAlias("Date")
    @XStreamAsAttribute
    private String date;

    @XStreamAlias("name")
    @XStreamAsAttribute
    private String name;

//        @XStreamImplicit(itemFieldName = "valute")
    private List<Valute> valutes = new ArrayList<>();
}

