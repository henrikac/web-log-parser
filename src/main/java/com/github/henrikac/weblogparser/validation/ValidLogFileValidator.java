package com.github.henrikac.weblogparser.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ValidLogFileValidator implements ConstraintValidator<ValidLogFile, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }

        String filename = value.getOriginalFilename();

        return filename != null && filename.toLowerCase().endsWith(".log");
    }
}
