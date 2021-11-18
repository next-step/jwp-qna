package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import qna.CannotDeleteException;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class Question extends AbstractIdEntity {
    @Lob
    private String contents;

    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(length = 100, nullable = false)
    private String title;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private Answers answers;

    protected Question() {
    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.answers = new Answers();
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        this.answers.add(answer);
    }

    public DeleteHistories deleteByWriterAndThenCallbackDeleteHistories(User writer) {
        if (!isOwner(writer)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        this.answers.deleteAllBy(writer);
        this.deleted = true;

        return new DeleteHistories.Builder()
            .addQuestion(this)
            .addAnswers(this.answers)
            .build();
    }

    public void change(final String contents, final String title) {
        this.contents = contents;
        this.title = title;
    }

    public boolean isUpdated() {
        if (Objects.isNull(createdAt)) {
            throw new IllegalStateException("질문 등록일이 없습니다. 질문 정보 변조가 의심됩니다.");
        }

        return Objects.equals(createdAt, updatedAt);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return this.contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public User getWriter() {
        return this.writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public Answers getAnswers() {
        return this.answers;
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", contents='" + contents + '\'' +
            ", writer=" + writer +
            ", deleted=" + deleted +
            '}';
    }
}
