package site.onandoff.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import site.onandoff.util.EntityHistory;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends EntityHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String email;

	@Column(unique = true, nullable = false)
	private String nickname;

	private String password;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	private Provider provider;

	public Member(String email, String nickname, String password, Provider provider) {
		this.email = email;
		this.nickname = nickname;
		this.password = password;
		this.provider = provider;
	}

}
