package io.ennov.ticket_management.shared.handlerException;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationError {
    private String property;
    private String message;
}
