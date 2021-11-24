package qna.domain;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "answer")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "contents", columnDefinition = "LONGTEXT")
    private String contents;

    @Column(name = "deleted")
    private boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private User writer;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

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

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        this.writer = writer;
        this.question = question;
        this.contents = contents;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void toQuestion(Question question) {
        this.question = question;
        question.getAnswers().add(this);
    }

    public DeleteHistory deleteAnswer(User user) throws CannotDeleteException {

        if (!this.writer.equals(user)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        this.deleted = true;
        return DeleteHistory.ofAnswer(id, writer);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public User getWriter() {
        return writer;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                ", writer=" + writer +
                ", question=" + question +
                '}';
    }
}
