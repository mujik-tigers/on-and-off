package site.onandoff.member.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import site.onandoff.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	boolean existsByEmail(String email);

}
