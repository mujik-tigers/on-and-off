package site.onandoff.image;

import jakarta.persistence.*;
import site.onandoff.post.Post;
import site.onandoff.util.EntityHistory;

@Entity
public class Image extends EntityHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    private boolean isDeleted;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

}
