package org.okky.reply.resource;

import lombok.extern.slf4j.Slf4j;
import org.okky.share.execption.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@ResponseBody
@Slf4j
class ErrorCatcher {
    @Value("${app.name}")
    String appName;

    @ExceptionHandler(ResourceNotFound.class)
    @ResponseStatus(NOT_FOUND)
    void handle(ResourceNotFound e) {
        // Just return status code
    }

    @ExceptionHandler(ModelNotExists.class)
    @ResponseStatus(NOT_FOUND)
    APIError handle(ModelNotExists e) {
        logger.error(e.getMessage(), e);
        return new APIError(appName, "1001", e.getMessage());
    }

    @ExceptionHandler(ModelConflicted.class)
    @ResponseStatus(CONFLICT)
    APIError handle(ModelConflicted e) {
        logger.error(e.getMessage(), e);
        return new APIError(appName, "1002", e.getMessage());
    }

    @ExceptionHandler(BadArgument.class)
    @ResponseStatus(BAD_REQUEST)
    APIError handle(BadArgument e) {
        logger.error(e.getMessage(), e);
        return new APIError(appName, "1003", e.getMessage());
    }

    @ExceptionHandler(ExternalServiceError.class)
    @ResponseStatus(BAD_GATEWAY)
    APIError handle(ExternalServiceError e) {
        logger.error(e.getMessage(), e);
        return new APIError(appName, "1004", e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    APIError handle(Exception e) {
        logger.error(e.getMessage(), e);
        return new APIError(appName, "1005", e.getMessage());
    }
}
