package distiya.github.com.customerprofilereactive.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RetailProfile {

    private String cmcpId;
    private String alias;
    private String cashDepositoryNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String familyName;
    private String nameLine1;
    private String nameLine2;
    private String registeredName;
}
