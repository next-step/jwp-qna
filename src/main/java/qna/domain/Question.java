package qna.domain;

import javax.persistence.*;

@Entity
@Table(name = "T_question")
public class Question extends BaseEntity {
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    private Long writerId;

    @Column(nullable = false)
    private boolean deleted = false;

    protected Question () { }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Question writeBy(Long writerId) {
        this.writerId = writerId;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writerId.equals(writer.id());
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
    }

    public Long id() {
        return id;
    }

    public void id(Long id) {
        this.id = id;
    }

    public String title() {
        return title;
    }

    public void title(String title) {
        this.title = title;
    }

    public String contents() {
        return contents;
    }

    public void contents(String contents) {
        this.contents = contents;
    }

    public Long writerId() {
        return writerId;
    }

    public void writer(Long writerId) {
        this.writerId = writerId;
    }

    public boolean deleted() {
        return deleted;
    }

    public void deleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writerId=" + writerId +
                ", deleted=" + deleted +
                '}';
    }
}
