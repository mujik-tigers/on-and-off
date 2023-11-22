package site.onandoff.notification;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import site.onandoff.comment.Comment;
import site.onandoff.member.Member;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("COMMENT")
public class CommentNotification extends Notification {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public CommentNotification(Member member, Comment comment) {
        super(member);
        this.comment = comment;
    }

}
