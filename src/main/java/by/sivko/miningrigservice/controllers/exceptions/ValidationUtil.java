package by.sivko.miningrigservice.controllers.exceptions;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;


public abstract class ValidationUtil {

    public static List<String> fromBindingErrors(Errors errors) {
        List<String> validErrors = new ArrayList<>();
        for (ObjectError objectError : errors.getAllErrors()) {
            validErrors.add(objectError.getDefaultMessage());
        }
        return validErrors;
    }
}
