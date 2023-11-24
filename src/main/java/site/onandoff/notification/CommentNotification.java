package site.onandoff.notification;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import site.onandoff.comment.Comment;
import site.onandoff.member.Member;

@Entity
@DiscriminatorValue("COMMENT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentNotification extends Notification {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "comment_id")
	private Comment comment;

	public CommentNotification(Member member, Comment comment) {
		super(member);
		this.comment = comment;
	}

}
