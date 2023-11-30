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
@Constraint(validatedBy = NicknameForm.NicknameFormValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NicknameForm {

	String message() default "닉네임은 한글, 영문, 숫자로 이루어진 1-10자 문자열입니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class NicknameFormValidator implements ConstraintValidator<NicknameForm, String> {

		private final String REGEX_NICKNAME = "^[a-zA-Z0-9가-힣]{1,10}$";

		private Pattern nicknameRegex = Pattern.compile(REGEX_NICKNAME);

		@Override
		public boolean isValid(String nicknameInput, ConstraintValidatorContext context) {
			return nicknameRegex.matcher(nicknameInput).matches();
		}

	}
}
