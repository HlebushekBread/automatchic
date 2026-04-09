package net.softloaf.automatchic.app.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ErrorResponse {
    private int status;
    private String message;
    private long timestamp;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }
}
