package entities;

import entities.enums.Sex;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
public class Client implements DBEntity{
    @EqualsAndHashCode.Exclude
    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private Sex sex;
    private boolean isBlocked;
    private String document;
}
