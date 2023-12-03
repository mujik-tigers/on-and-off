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
import site.onandoff.exception.member.MemberNotFoundException;
import site.onandoff.member.Member;
import site.onandoff.member.dto.ValidPasswordChangeForm;
import site.onandoff.member.infrastructure.MemberRepository;
import site.onandoff.util.encryption.BCryptManager;

@Documented
@Constraint(validatedBy = PasswordMatchCheck.PasswordMatchValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PasswordMatchCheck {

	String message() default "비밀번호가 일치하지 않습니다";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class PasswordMatchValidator implements ConstraintValidator<PasswordMatchCheck, ValidPasswordChangeForm> {

		private MemberRepository memberRepository;

		@Autowired
		public void setMemberRepository(MemberRepository memberRepository) {
			this.memberRepository = memberRepository;
		}

		@Override
		public boolean isValid(ValidPasswordChangeForm passwordChangeForm, ConstraintValidatorContext context) {
			Member member = memberRepository.findById(passwordChangeForm.getId())
				.orElseThrow(MemberNotFoundException::new);

			return BCryptManager.isMatch(passwordChangeForm.getPassword(), member.getPassword());
		}

	}

}
