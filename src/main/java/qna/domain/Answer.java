package qna.domain;

import lombok.*;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Answer extends BaseEntity {

    @ManyToOne
    @JoinColumn(name="writer_id")
    private User writer;

    @ManyToOne
    @JoinColumn(name ="question_id")
    private Question question;
    @Lob
    @Column
    private String contents;
    @Column(nullable = false)
    private boolean deleted = false;

    @Column
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, String contents) {
        this.id = id;

        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        setWriter(writer);
        this.contents = contents;
        this.question = question;
    }

    public boolean isOwner(User writer) {
        return this.writer.equalsNameAndEmail(writer);
    }

    public void toQuestion(Question question) {
        this.question = question;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setWriter(User writer) {
        this.writer = writer;
        this.writer.addAnswer(this);
    }

    public void setQuestion(Question question) {
        this.question = question;
        this.question.addAnswer(this);
    }
    @Override
    public String toString() {
        return "Answer{" +
                "writer=" + writer +
                ", question=" + question +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                ", updatedAt=" + updatedAt +
                ", id=" + id +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return Objects.equals(id, answer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void delete(User loginUser) throws CannotDeleteException {
        verifyDeletable(loginUser);
        createHistory(loginUser);
    }

    private void verifyDeletable(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
        deleted = true;
    }

    private DeleteHistory createHistory(User loginUser) {
        return DeleteHistory.addHistory(ContentType.ANSWER,id,loginUser);
    }
}
