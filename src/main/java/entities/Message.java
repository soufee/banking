package entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class Message {
    private Client client;
    private List<Account> accounts;
    private Set<Operation> operations;
}
