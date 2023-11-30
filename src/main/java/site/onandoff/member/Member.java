package site.onandoff.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import site.onandoff.util.EntityHistory;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

	public Long getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

}
