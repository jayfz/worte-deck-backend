package co.harborbytes.wortedeck.shared;

import jakarta.validation.ConstraintViolation;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ApiValidationErrors {

    private List<ApiValidationError> errors;

    public ApiValidationErrors add(List<? extends ObjectError> objectErrors) {
        if (errors == null)
            errors = new ArrayList<>();

        objectErrors.forEach((objectError -> {
            final ApiValidationError error = new ApiValidationError(objectError.getObjectName(), objectError.getDefaultMessage());
            if (objectError instanceof FieldError fieldError) {
                error.setField(fieldError.getField());
                error.setRejectedValue(fieldError.getRejectedValue());
            }
            errors.add(error);
        }));

        return this;
    }

    public ApiValidationErrors add(Set<ConstraintViolation<?>> constraintViolations){

        constraintViolations.forEach(cv ->{
            final ApiValidationError error = new ApiValidationError(
                    cv.getRootBeanClass().getSimpleName(),
                    ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
                    cv.getInvalidValue(),
                    cv.getMessage());

            this.errors.add(error);
        });

        return this;
    }

    public List<ApiValidationError> get() {
        return errors;
    }
}
