package MyFirstHttpProgram.DTO;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAliasType;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@XStreamAlias("Valute")
public class Valute {

    @XStreamAlias("id")
    @XStreamAsAttribute
    private Integer id;
    @XStreamAlias("NumCode")
    @XStreamAsAttribute
    private String numCode;
    @XStreamAlias("CharCode")
    @XStreamAsAttribute
    private String charCode;
    @XStreamAlias("Nominal")
    @XStreamAsAttribute
    private Integer nominal;
    @XStreamAlias("Name")
    @XStreamAsAttribute
    private String name;
    @XStreamAlias("Value")
    @XStreamAsAttribute
    private Double value;
}
