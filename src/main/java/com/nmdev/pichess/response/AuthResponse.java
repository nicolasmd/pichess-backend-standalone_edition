package com.nmdev.pichess.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Authentication response
 */
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String jwt;
    private String message;
    private String username;
    private Boolean status;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}