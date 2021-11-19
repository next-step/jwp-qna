package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import qna.CannotDeleteException;

@Entity
public class Question extends BaseEntity {
    public static final String ERROR_PERMISSION_TO_DELETE = "질문을 삭제할 권한이 없습니다.";
    public static final boolean DELETE = true;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(nullable = false, length = 100)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    protected Question() {
    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public void delete(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException(ERROR_PERMISSION_TO_DELETE);
        }

        setDeleted(DELETE);
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answer.setQuestion(this);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public User getWriter() {
        return writer;
    }

    public String getContents() {
        return contents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Question question = (Question)o;
        return deleted == question.deleted && Objects.equals(id, question.id) && Objects.equals(
            contents, question.contents) && Objects.equals(title, question.title) && Objects.equals(
            writer, question.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contents, deleted, title, writer);
    }
}
