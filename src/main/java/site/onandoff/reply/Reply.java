package site.onandoff.reply;

import jakarta.persistence.*;
import site.onandoff.comment.Comment;
import site.onandoff.member.Member;
import site.onandoff.util.EntityHistory;

@Entity
public class Reply extends EntityHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

}
