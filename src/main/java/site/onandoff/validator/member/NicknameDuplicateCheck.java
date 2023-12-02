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

@Documented
@Constraint(validatedBy = NicknameDuplicateCheck.NicknameDuplicateValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NicknameDuplicateCheck {

	String message() default "이미 존재하는 닉네임입니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class NicknameDuplicateValidator implements ConstraintValidator<NicknameDuplicateCheck, String> {
		private MemberRepository memberRepository;

		@Autowired
		public void setMemberRepository(MemberRepository memberRepository) {
			this.memberRepository = memberRepository;
		}

		@Override
		public boolean isValid(String nicknameInput, ConstraintValidatorContext context) {
			return !memberRepository.existsByNickname(nicknameInput);
		}

	}
}
