package site.onandoff.validator.member;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import site.onandoff.member.infrastructure.MemberRepository;
import site.onandoff.util.encryption.AES256Manager;

@Documented
@Constraint(validatedBy = EmailDuplicateCheck.EmailDuplicateValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EmailDuplicateCheck {

	String message() default "이미 사용 중인 이메일입니다";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class EmailDuplicateValidator implements ConstraintValidator<EmailDuplicateCheck, String> {
		private MemberRepository memberRepository;
		private AES256Manager aes256Manager;

		@Autowired
		public void setMemberRepository(MemberRepository memberRepository, AES256Manager aes256Manager) {
			this.memberRepository = memberRepository;
			this.aes256Manager = aes256Manager;
		}

		@Override
		public boolean isValid(String emailInput, ConstraintValidatorContext context) {
			String encryptedEmailInput = aes256Manager.encrypt(emailInput);
			return !memberRepository.existsByEmail(encryptedEmailInput);
		}

	}

}
