package pl.edu.pjatk.foodbook.authservice.rest.dto.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.edu.pjatk.foodbook.authservice.rest.dto.annotation.ValidEnum;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumConstraintValidator implements ConstraintValidator<ValidEnum, String> {

    private Set<String> values;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        values = Stream.of(constraintAnnotation.value().getEnumConstants())
                     .map(Enum::name)
                     .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return values.contains(value);
    }
}
