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
@Constraint(validatedBy = NicknameFormatCheck.NicknameFormatValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NicknameFormatCheck {

	String message() default "닉네임은 1~10자의 한글, 영문, 숫자만 사용 가능합니다";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class NicknameFormatValidator implements ConstraintValidator<NicknameFormatCheck, String> {

		private final String REGEX_NICKNAME = "^[a-zA-Z0-9가-힣]{1,10}$";

		private Pattern nicknameRegex = Pattern.compile(REGEX_NICKNAME);

		@Override
		public boolean isValid(String nicknameInput, ConstraintValidatorContext context) {
			return nicknameInput != null && nicknameRegex.matcher(nicknameInput).matches();
		}

	}
}
