package com.jobportal.backend.exception;

public record ApiErrorResponse(String message, int status) {
}
