package qna.domain.content;

import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.common.AuditingEntity;
import qna.domain.User;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Answer extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted = false;

    protected Answer() {}

    public Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    public Answer(Long id, User writer, Question question, String contents) {
        validateForInserts(writer, question);

        this.id = id;
        this.writer = writer;
        updateQuestion(question);
        this.contents = contents;
    }

    private void validateForInserts(User writer, Question question) {
        validateWriter(writer);
        validateQuestion(question);
    }

    private void validateWriter(User writer) {
        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }
    }

    private void validateQuestion(Question question) {
        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }
    }

    public void updateQuestion(Question question) {
        if(this.question != question) {
            this.question = question;
            question.addAnswer(this);
        }
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void update(User loginUser, String contents) {
        matchUser(loginUser);

        this.contents = contents;
    }

    private void matchUser(User loginUser) {
        if(!this.writer.equals(loginUser)) {
            throw new UnAuthorizedException();
        }
    }

    public void updateDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Long getId() {
        return id;
    }

    public User getWriter() {
        return writer;
    }

    public Question getQuestion() {
        return question;
    }

    public String getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
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
        return Objects.equals(id, answer.id) && Objects.equals(writer, answer.writer)
                && Objects.equals(question, answer.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, writer, question);
    }
}
