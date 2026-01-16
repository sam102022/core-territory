package com.samos.core.exception.mapper;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.validation.ConstraintViolationException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.samos.core.exception.ApiError;
import com.samos.core.exception.ApiError.Error.ErrorBuilder;
import com.samos.core.exception.FunctionalError;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiErrorMapper {

    // Mapping des erreurs fonctionnelles
    public static ApiError map(FunctionalError sourceError) {

        //
        // Une erreur fonctionnelle possède toujours à minima un code
        //
        final ErrorBuilder errorBuilder = ApiError.Error.builder() //
                // Une erreur fonctionnelle possède toujours à minima un code
                .code(sourceError.getCode().name()) //
                .description(sourceError.getMessageTemplate());

        // Une erreur fonctionnelle peut avoir des paramètres
        if (sourceError.getParameters() != null) {
            errorBuilder.parameters(sourceError.getParameters().stream() //
                    .map(Object::toString) //
                    .collect(Collectors.toList()) //
            ); //
        }

        return ApiError.builder() //
                .errors(Collections.singleton(errorBuilder.build()))//
                .build();
    }

    // Mapping des erreurs fonctionnelles qui ne sont pas levées à la main (=qui ne sont
    // donc pas de type FunctionalError) => Il n'y a que les erreurs de type non-respect
    // d'interface qui sortent comme ça)
    // MethodArgumentNotValidException = l'erreur a été détectée dans la payload
    public static ApiError map(MethodArgumentNotValidException sourceError) {
        final Set<ApiError.Error> apiErrors = sourceError.getBindingResult()
                .getFieldErrors().stream() //
                .map(error -> ApiError.Error.builder() //
                        .code(FunctionalError.BasicErrorCode.INVALID_FORMAT.name()) //
                        .description(error.getField() + ": " + error.getDefaultMessage())
                        .build() //
                ) //
                .collect(Collectors.toSet());

        return ApiError.builder() //
                .errors(apiErrors)//
                .build();
    }

    // Mapping des erreurs fonctionnelles qui ne sont pas levées à la main (=qui ne sont
    // donc pas de type FunctionalError) => Il n'y a que les erreurs de type non-respect
    // d'interface qui sortent comme ça)
    // ConstraintViolationException = l'erreur a été détectée en dehors de la payload
    public static ApiError map(ConstraintViolationException sourceError) {

        final Set<ApiError.Error> apiErrors = sourceError.getConstraintViolations()
                .stream() //
                .map(error -> ApiError.Error.builder() //
                        .code(FunctionalError.BasicErrorCode.INVALID_FORMAT.name()) //
                        .description(error.getPropertyPath() + ": " + error.getMessage()) //
                        .build() //
                ) //
                .collect(Collectors.toSet());

        return ApiError.builder() //
                .errors(apiErrors)//
                .build();
    }

    // Mapping des erreurs fonctionnelles qui ne sont pas levées à la main (=qui ne sont
    // donc pas de type FunctionalError) => Il n'y a que les erreurs de type non-respect
    // d'interface qui sortent comme ça)
    // TypeMismatchException = l'erreur a été détectée en dehors de la payload
    public static ApiError map(TypeMismatchException sourceError) {

        final Set<ApiError.Error> apiErrors = Stream.of(ApiError.Error.builder() //
                .code(FunctionalError.BasicErrorCode.INVALID_FORMAT.name()) //
                .description(sourceError.getMessage()) //
                .build()).collect(Collectors.toSet());

        return ApiError.builder().errors(apiErrors).build();
    }

    public static ApiError map(Exception sourceError) {

        final Set<ApiError.Error> apiErrors = Stream.of(ApiError.Error.builder() //
                .code(FunctionalError.BasicErrorCode.INVALID_FORMAT.name()) //
                .description(sourceError.getMessage()) //
                .build()).collect(Collectors.toSet());

        return ApiError.builder().errors(apiErrors).build();
    }

}
