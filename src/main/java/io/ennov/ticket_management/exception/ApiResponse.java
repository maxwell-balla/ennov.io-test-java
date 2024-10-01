package io.ennov.ticket_management.exception;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ApiResponse {

    private Integer status;
    private Boolean success;
    private Date timestamp;
    private String message;
    private String details;
    private Object data;
    private List<ValidationError> errors;

    public ApiResponse(Integer status, Boolean success, Date timestamp, String message, String details) {
        this.status = status;
        this.success = success;
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public ApiResponse(Integer status, Boolean success, Date timestamp, String message, String details, List<ValidationError> errors) {
        this(status, success, timestamp, message, details);
        this.errors = errors;
    }
}
