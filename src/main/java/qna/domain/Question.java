package qna.domain;

import java.util.Objects;

import javax.persistence.*;

import qna.exceptions.UnAuthorizedException;
import qna.validators.StringValidator;

@Table
@Entity
public class Question extends BaseEntity {

    private static final int TITLE_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = TITLE_LENGTH, nullable = false)
    private String title;

    @Lob
    private String contents;

    @Column
    private Long writerId;

    @Column(nullable = false)
    private boolean deleted = false;

    protected Question() {
    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        StringValidator.validate(title, TITLE_LENGTH);

        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Question writeBy(User writer) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        this.writerId = writer.getId();
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writerId.equals(writer.getId());
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Long getWriterId() {
        return writerId;
    }

    public void setWriterId(Long writerId) {
        this.writerId = writerId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Question{"
            + "id=" + id
            + ", title='" + title + '\''
            + ", contents='" + contents + '\''
            + ", writerId=" + writerId
            + ", deleted=" + deleted
            + ", createdAt=" + getCreatedAt()
            + ", updatedAt=" + getUpdatedAt()
            + '}';
    }
}
