package qnamission.domain;

import javax.persistence.*;

@Entity
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(length = 100, nullable = false)
    private String title;

    private Long writerId;

    protected Question() {

    }

    public Long getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
