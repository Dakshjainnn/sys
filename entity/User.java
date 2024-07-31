package banksys.sys.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@Table(name="user")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String First_Name;
    private String Last_Name;
    private String Gender;
    @Column(name="account_number")
    private String accountNumber;
    private String email;
    private BigDecimal Account_Balance;
    private String Phone_Number;
    private String Status;
    @CreationTimestamp
    private LocalDateTime Created;
    @UpdateTimestamp
    private LocalDateTime Modified;
}
