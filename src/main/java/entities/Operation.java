package entities;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class Operation implements DBEntity {
    @EqualsAndHashCode.Exclude
    private Long id;
    private String from; //accountNumber of payer
    private String to; //accountNumber of payee
    private BigDecimal amount;
    private String currency;
    private LocalDateTime dateTime;

    @Override
    public String toString() {
        return dateTime + ": from " + from + " to " + to + " " + amount + " " + currency;
    }

}
