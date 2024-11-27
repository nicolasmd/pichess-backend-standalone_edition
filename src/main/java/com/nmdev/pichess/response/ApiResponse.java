package com.nmdev.pichess.response;
import lombok.Data;

/**
 * Generic API response (unique parameter)
 */
@Data
public class ApiResponse {
	
    private String message;
    private boolean status;
    
    /**
     * ApiResponse constructor
     * @param message
     * @param status
     */
    public ApiResponse(String message, boolean status) {
        setMessage(message);
        setStatus(status);
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isStatus() {
        return status;
    }
    
    public void setStatus(boolean status) {
        this.status = status;
    }

}