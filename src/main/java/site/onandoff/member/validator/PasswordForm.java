package site.onandoff.member.validator;

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
@Constraint(validatedBy = PasswordForm.PasswordFormValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PasswordForm {

	String message() default "비밀번호는 영문, 숫자, 특수기호를 모두 포함하는 8-16자 문자열입니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class PasswordFormValidator implements ConstraintValidator<PasswordForm, String> {

		private final String REGEX_PASSWORD = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,16}$";

		private Pattern passwordRegex = Pattern.compile(REGEX_PASSWORD);

		@Override
		public boolean isValid(String passwordInput, ConstraintValidatorContext context) {
			return passwordRegex.matcher(passwordInput).matches();
		}

	}

}
