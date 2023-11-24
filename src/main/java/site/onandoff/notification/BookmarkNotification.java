package site.onandoff.notification;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import site.onandoff.bookmark.Bookmark;
import site.onandoff.member.Member;

@Entity
@DiscriminatorValue("BOOKMARK")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkNotification extends Notification {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bookmark_id")
	private Bookmark bookmark;

	public BookmarkNotification(Member member, Bookmark bookmark) {
		super(member);
		this.bookmark = bookmark;
	}

}
