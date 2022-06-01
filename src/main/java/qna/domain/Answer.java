package qna.domain;

import java.time.LocalDateTime;
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
import qna.CannotDeleteException;
import qna.UnAuthorizedException;
import qna.base.BaseTimeEntity;

@Entity
public class Answer extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @Column(name = "question_id")
    private Long questionId;
    @Lob
    private String contents;
    @Column(nullable = false)
    private boolean deleted = false;

    protected Answer() {
    }

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, String contents) {
        this.id = id;

        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        this.writer = writer;
        this.questionId = question.getId();
        this.contents = contents;
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public Answer writeBy(final User writer) {
        this.writer = writer;
        return this;
    }

    public String getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public DeleteHistory delete(final User loginUser) {
        validateWriter(loginUser);
        this.deleted = true;
        return new DeleteHistory(ContentType.ANSWER, id, writer, LocalDateTime.now());
    }

    public void changeContents(final String contents) {
        this.contents = contents;
    }

    private void validateWriter(final User loginUser) {
        if (!isWriter(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    private boolean isWriter(final User loginUser) {
        return writer.equals(loginUser);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writer=" + writer +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
