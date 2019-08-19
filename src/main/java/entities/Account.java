package entities;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@Builder
public class Account implements DBEntity {
    @EqualsAndHashCode.Exclude
    private Long id;
    private String accountNumber;
    private String currencyCode;
    private BigDecimal amount;
    private Long owner; //id of Client
    private boolean isBlocked;
}
