package com.samos.core.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.samos.core.exception.mapper.ApiErrorMapper;

@RestControllerAdvice
@Slf4j
public class ResponseEntityExceptionHandler extends org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler {

    // Seules les FunctionalError peuvent retourner une erreur 4XX à l'appelant, avec le
    // détail de l'erreur dans le body
    @ExceptionHandler({ FunctionalError.class })
    public ResponseEntity<Object> handleFunctionalError(FunctionalError ex,
            WebRequest request) {

        // On commence par tracer l'erreur
        log.error(ex.toString(), ex);

        // On choisi ensuite le bon statut HTTP

        final HttpStatus httpStatus;
        if (FunctionalError.BasicErrorCode.NOT_FOUND.equals(ex.getCode())) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else if (FunctionalError.BasicErrorCode.INVALID_FORMAT.equals(ex.getCode())) {
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (FunctionalError.BasicErrorCode.UNAUTHORIZED.equals(ex.getCode())) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        } else if (FunctionalError.BasicErrorCode.FORBIDDEN.equals(ex.getCode())) {
            httpStatus = HttpStatus.FORBIDDEN;
        } else {
            // Si le type d'erreur n'est pas un des précédents, c'est forcément une erreur
            // 400
            httpStatus = HttpStatus.BAD_REQUEST;
        }

        // On map ensuite la functional error pour respecter le contrat d'interface en
        // sortie, de manière à retourner le détail de l'erreur dans
        // le body

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);

        final Object body = ApiErrorMapper.map(ex);

        // On termine la configuration de la réponse comme Spring (même si dans notre cas,
        // ça n'apporte rien de plus)
        return handleExceptionInternal(ex, body, headers, httpStatus, request);
    }

    // Cette erreur correspond à un non respect de l'appelant, d'une ou plusieurs
    // contraintes d'interface en dehors de la payload de la requête
    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {

        // On commence par tracer l'erreur
    	log.error(ex.toString(), ex);

        // On map ensuite la functional error pour respecter le contrat d'interface en
        // sortie, de manière à retourner le détail de l'erreur dans
        // le body

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);

        final Object body = ApiErrorMapper.map(ex);

        // On retourne une erreur 400, avec le détail de l'erreur
        return handleExceptionInternal(ex, body, headers,
                HttpStatus.BAD_REQUEST, request);
    }

    // Cette erreur correspond à un non respect de l'appelant, d'une ou plusieurs
    // contraintes d'interface dans la payload de la requête
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex, HttpHeaders headers, @NonNull HttpStatus status,
            @NonNull WebRequest request) {

        // On commence par tracer l'erreur
    	log.error(ex.toString(), ex);

        // On map ensuite cette erreur d'interface pour respecter le contrat d'interface
        // en sortie, de manière à retourner le détail de l'erreur dans le body

        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);

        final Object body = ApiErrorMapper.map(ex);

        // On termine la configuration de la réponse comme Spring (même si dans notre cas,
        // ça n'apporte rien de plus)
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @NonNull
    protected ResponseEntity<Object> handleTypeMismatch(@NonNull TypeMismatchException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest request) {
        // On commence par tracer l'erreur
    	log.error(ex.toString(), ex);

        // On map ensuite cette erreur d'interface pour respecter le contrat d'interface
        // en sortie, de manière à retourner le détail de l'erreur dans le body

        headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);

        final Object body = ApiErrorMapper.map(ex);

        // On termine la configuration de la réponse comme Spring (même si dans notre cas,
        // ça n'apporte rien de plus)
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    // Bannette par défaut si l'exception ne correspond à aucun type géré spécifiquement
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleDefaultException(Exception ex,
            WebRequest request) {

        // On commence par tracer l'erreur
    	log.error(ex.toString(), ex);

        // On retourne une erreur 500, sans plus de détails, pour protéger notre
        // application et respecter notre contrat de services
        return handleExceptionInternal(ex, null, HttpHeaders.EMPTY, HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }

    // On surcharge la méthode pour éviter de rajouter un attribut à la requête (en cas
    // d'erreur 500)
    // Le problème avec ce bout de code de Spring, c'est que ça génère un body HTML et que
    // nous, on en veut pas, l'objectif étant de ne jamais publier de body sauf erreur
    // 4XX...
    @NonNull
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex,
            Object body,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatus status,
            @NonNull WebRequest request) {

        // On fait la même chose que Spring, sauf qu'on a viré l'instruction qui rajoute un attribut à la requête

        // On le fait pour prendre la main sur tous les autres types d'erreurs gérés par
        // Spring qui pourraient survenir et qui appellent toujours in-fine cette méthode
        // (cf ResponseEntityExceptionHandler.handleException(Exception ex, WebRequest
        // request))
        return new ResponseEntity<>(body != null ? body : ApiErrorMapper.map(ex), headers, status);
    }

}
