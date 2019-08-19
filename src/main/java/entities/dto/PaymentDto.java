package entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentDto {
    String accountFrom;
    String accountTo;
    String amount;
}
