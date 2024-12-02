package com.example.alamnibackend.payload.response;

public class EmailVerificationResponse {
    private String message;
    private boolean isEnabled;

    public EmailVerificationResponse(String message, boolean isEnabled) {
        this.message = message;
        this.isEnabled = isEnabled;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}