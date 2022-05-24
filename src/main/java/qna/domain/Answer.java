package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import qna.common.BaseEntity;
import qna.exception.CannotDeleteException;
import qna.exception.NotFoundException;
import qna.exception.UnAuthorizedException;

@Entity
public class Answer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @Column(nullable = false)
    private boolean deleted = false;

    protected Answer() {
    }

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, String contents) {
        nullCheckUserAndQuestion(writer, question);
        this.id = id;
        this.writer = writer;
        this.question = question;
        this.contents = contents;
    }

    private void validateRemovable(User user) {
        validateDeleted();
        validateWriter(user);
    }

    private void validateDeleted() {
        if (this.deleted) {
            throw new NotFoundException("이미 삭제된 답변입니다.");
        }
    }

    private void validateWriter(User user) {
        if (!this.writer.equals(user)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return this.writer;
    }

    public Question getQuestion() {
        return question;
    }

    public String getContents() {
        return contents;
    }

    public void delete(User loginUser) {
        validateRemovable(loginUser);
        this.deleted = true;
    }

    public boolean isDeleted() {
        return deleted;
    }

    private void nullCheckUserAndQuestion(User writer, Question question) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException("유저 정보가 존재하지 않습니다.");
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException("질문 정보가 존재하지 않습니다.");
        }
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", contents='" + contents + '\'' +
                ", user=" + writer +
                ", question=" + question +
                ", deleted=" + deleted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Answer answer = (Answer) o;
        return deleted == answer.deleted && Objects.equals(id, answer.id)
                && Objects.equals(contents, answer.contents) && Objects.equals(writer.getId(), answer.writer.getId())
                && Objects.equals(question.getId(), answer.question.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contents, writer.getId(), question.getId(), deleted);
    }
}
