package qna.domain;

import org.hibernate.annotations.Where;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Where(clause = "deleted='NO'")
public class Answer extends CreatedUpdatedDateEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WRITER_ID", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QUESTION_ID", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @Lob
    private String contents;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeletedType deleted = DeletedType.NO;

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
        this.contents = contents;
        toQuestion(question);
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void toQuestion(Question question) {
        if (Objects.nonNull(question)) {
            question.getAnswers().addAnswer(this);
        }
        this.question = question;
    }

    public Long getId() {
        return id;
    }

    public void setWriter(User writer) {
        this.writer = writer;
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

    public void setContents(String contents) {
        this.contents = contents;
    }

    public boolean isDeleted() {
        return deleted.isDeleted();
    }

    public void setDeleted(final DeletedType state) {
        this.deleted = state;
    }

    public DeleteHistory delete(final User writer) throws CannotDeleteException {
        if (Objects.equals(this.getWriter(), writer)) {
            this.setDeleted(DeletedType.YES);
            return new DeleteHistory(ContentType.ANSWER, this.getId(), this.getWriter(), LocalDateTime.now());
        }
        throw new CannotDeleteException("답변 사용자와 달라서 삭제할 수 없습니다.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return deleted == answer.deleted && Objects.equals(id, answer.id) && Objects.equals(writer, answer.writer) && Objects.equals(question, answer.question) && Objects.equals(contents, answer.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, writer, question, contents, deleted);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writer=" + writer +
                ", question=" + question +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
