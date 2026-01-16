package com.samos.core.exception;

import java.util.List;
import java.util.Set;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ApiError {

    Set<Error> errors;

    @Value
    @Builder
    public static class Error {
        String code;
        String description;
        List<String> parameters;
    }

}
