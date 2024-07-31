package banksys.sys.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfo {
    private String AccountName;
    private BigDecimal AccountBalance;
    private String Account_Number;
    private String Phone_Number;
    @Column(unique = true)
    private String email;
    private String gender;




}
