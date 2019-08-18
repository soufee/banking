package entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentDto {
    String accountFrom;
    String accountTo;
    String amount;
}
