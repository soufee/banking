package entities;

import entities.enums.Sex;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Client {
    private long id;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private Sex sex;
    private boolean isBlocked;
    private String document;
}
