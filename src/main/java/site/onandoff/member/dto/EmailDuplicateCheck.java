package site.onandoff.member.dto;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import site.onandoff.member.infrastructure.MemberRepository;

@Documented
@Constraint(validatedBy = EmailDuplicateCheck.EmailDuplicateValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EmailDuplicateCheck {

	String message() default "이미 존재하는 이메일입니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@Component
	class EmailDuplicateValidator implements ConstraintValidator<EmailDuplicateCheck, String> {
		private MemberRepository memberRepository;

		@Autowired
		public void setMemberRepository(MemberRepository memberRepository) {
			this.memberRepository = memberRepository;
		}

		@Override
		public boolean isValid(String emailInput, ConstraintValidatorContext context) {
			return !memberRepository.existsByEmail(emailInput);
		}

	}

}
