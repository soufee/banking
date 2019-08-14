package entities;

import entities.enums.OperationType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class Operations {
    private long id;
    private OperationType operationType;
    private String from; //accountNumber of payer
    private String to; //accountNumber of payee
    private BigDecimal amount;
    private String currency;
    private LocalDateTime dateTime;

}
