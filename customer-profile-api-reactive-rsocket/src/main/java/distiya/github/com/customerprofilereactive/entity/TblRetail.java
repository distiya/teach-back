package distiya.github.com.customerprofilereactive.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("tbl_retail")
public class TblRetail {

    @Id
    @Column("cmcp_id")
    private String cmcpId;

    @Column("alias")
    private String alias;

    @Column("cash_depository_number")
    private String cashDepositoryNumber;

    @Column("date_of_birth")
    private LocalDate dateOfBirth;

    @Column("family_name")
    private String familyName;

    @Column("name_line_1")
    private String nameLine1;

    @Column("name_line_2")
    private String nameLine2;

    @Column("registered_name")
    private String registeredName;

    @Column("registered_name_lower")
    private String registeredNameLower;
}
