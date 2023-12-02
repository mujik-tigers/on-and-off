package site.onandoff.validator.member;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = EmailFormatCheck.EmailFormatValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EmailFormatCheck {

	String message() default "이메일이 양식에 맞지 않습니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class EmailFormatValidator implements ConstraintValidator<EmailFormatCheck, String> {
		private final String REGEX_EMAIL = "^(?=.{1,62}$)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$";

		private Pattern emailRegex = Pattern.compile(REGEX_EMAIL);

		@Override
		public boolean isValid(String emailInput, ConstraintValidatorContext context) {
			return emailInput != null && emailRegex.matcher(emailInput).matches();
		}
	}

}
