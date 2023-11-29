package site.onandoff.member.dto;

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
@Constraint(validatedBy = EmailForm.EmailFormValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EmailForm {

	String message() default "이메일이 양식에 맞지 않습니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class EmailFormValidator implements ConstraintValidator<EmailForm, String> {
		private final String REGEX_EMAIL = "^[0-9a-z-A-z]([\\-.\\w]*[0-9a-zA-Z\\-_+])*" // 소문자 알파벳이나 숫자로 시작하는 local part
			+ "@([0-9a-zA-Z][\\-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9}$";    // 서브 도메인, 도메인, 최상위 도메인

		private Pattern emailRegex = Pattern.compile(REGEX_EMAIL);

		@Override
		public boolean isValid(String emailInput, ConstraintValidatorContext context) {
			return emailRegex.matcher(emailInput).matches();
		}
	}

}
