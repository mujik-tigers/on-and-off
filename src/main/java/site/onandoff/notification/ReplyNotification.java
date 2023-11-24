package site.onandoff.notification;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import site.onandoff.member.Member;
import site.onandoff.reply.Reply;

@Entity
@DiscriminatorValue("REPLY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyNotification extends Notification {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reply_id")
	private Reply reply;

	public ReplyNotification(Member member, Reply reply) {
		super(member);
		this.reply = reply;
	}

}
