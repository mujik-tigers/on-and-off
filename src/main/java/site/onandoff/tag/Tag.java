package site.onandoff.tag;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import site.onandoff.util.EntityHistory;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends EntityHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

}
