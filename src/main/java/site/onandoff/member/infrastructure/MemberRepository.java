package site.onandoff.member.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import site.onandoff.member.Member;
import site.onandoff.member.Provider;

public interface MemberRepository extends JpaRepository<Member, Long> {

	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);

	Optional<Member> findByEmailAndProvider(String email, Provider provider);

}
