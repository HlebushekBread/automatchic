package net.softloaf.automatchic.common.dto.response;

import lombok.Data;

@Data
public class ErrorResponse {
    private Integer status;
    private String message;
    private Long timestamp;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }
}
