package entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PaymentResponseDto {
    private Integer status;
    private String message;
    private Long operationId;
}
