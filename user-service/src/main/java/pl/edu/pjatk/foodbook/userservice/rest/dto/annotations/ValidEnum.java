package pl.edu.pjatk.foodbook.userservice.rest.dto.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pl.edu.pjatk.foodbook.userservice.rest.dto.validator.EnumConstraintValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = EnumConstraintValidator.class)
@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface ValidEnum {

    Class<? extends Enum<?>> value();

    String message() default "Enum value must be any of \"{value}\"";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
