package by.practice.git.cloudstorage.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PathValidator implements ConstraintValidator<ValidPath, String> {

    @Override
    public boolean isValid(String path, ConstraintValidatorContext context) {
        if(path == null) return true;

        String invalidChars = "<>:\"|?* ";

        for (char c : invalidChars.toCharArray()) {
            if(path.indexOf(c) != -1) {
                return false;
            }
        }

        return !path.contains("..");
    }

}
