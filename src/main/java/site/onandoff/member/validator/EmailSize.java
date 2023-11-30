package site.onandoff.member.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = EmailSize.EmailSizeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EmailSize {

	String message() default "이메일 길이는 62자 이하입니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class EmailSizeValidator implements ConstraintValidator<EmailSize, String> {

		@Override
		public boolean isValid(String emailInput, ConstraintValidatorContext context) {
			return emailInput.length() <= 62;
		}
	}
}
