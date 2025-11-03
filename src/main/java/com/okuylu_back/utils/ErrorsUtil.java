package com.okuylu_back.utils;
import com.okuylu_back.utils.exceptions.PaymentException;
import com.okuylu_back.utils.exceptions.PersonException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;


public class ErrorsUtil {

    public static void returnPersonError(String generalMessage, BindingResult bindingResult, HttpStatus httpStatus) {

        throw new PersonException(buildErrorMessage(generalMessage, bindingResult), httpStatus);
    }

    public static void returnPaymentError(String message, HttpStatus httpStatus) {

        throw new PaymentException(message, httpStatus);
    }

    private static String buildErrorMessage(String generalMessage, BindingResult bindingResult) {

        StringBuilder errorMessage = new StringBuilder();

        if (bindingResult != null) {

            List<FieldError> errors = bindingResult.getFieldErrors();

            for (FieldError error : errors) {
                errorMessage.append(error.getField())
                        .append(": ")
                        .append(error.getDefaultMessage() == null ? error.getCode() : error.getDefaultMessage())
                        .append("; ");
            }

        }

        return generalMessage + " " + errorMessage;
    }
}
