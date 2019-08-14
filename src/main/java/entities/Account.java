package entities;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class Account implements DBEntity {
    private long id;
    private String accountNumber;
    private String currencyCode;
    private BigDecimal amount;
    private long owner; //id of Client
    private boolean isBlocked;
}
