package site.onandoff.notification;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import site.onandoff.bookmark.Bookmark;
import site.onandoff.member.Member;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("BOOKMARK")
public class BookmarkNotification extends Notification {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_id")
    private Bookmark bookmark;

    public BookmarkNotification(Member member, Bookmark bookmark) {
        super(member);
        this.bookmark = bookmark;
    }

}
