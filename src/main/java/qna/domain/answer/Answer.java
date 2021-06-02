package qna.domain.answer;

import org.hibernate.annotations.Where;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.domain.CreateAndUpdateTimeEntity;
import qna.domain.question.Question;
import qna.domain.user.User;

import javax.persistence.*;
import java.util.Objects;

@Table(name = "answer")
@Entity
@Where(clause = "deleted = false")
public class Answer extends CreateAndUpdateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    @Lob
    @Column(name = "contents")
    private String contents;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    protected Answer() {
        // empty
    }

    public Answer(User writer, Question question, String contents) {
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

    public boolean isSameQuestion(final Question question) {
        return this.question.equals(question);
    }

    public boolean isSameContents(final String contents){
        return this.contents.equals(contents);
    }

    public void toQuestion(Question question) {
        this.question = question;
    }

    public Long getId() {
        return id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void delete() {
        this.deleted = true;
    }

    public void updateContents(final String contents) {
        this.contents = contents;
    }

    public void toWriter(User writer) {
        this.writer = writer;
    }

    public User getWriter() {
        return this.writer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Answer answer = (Answer)o;
        return deleted == answer.deleted
               && Objects.equals(id, answer.id)
               && Objects.equals(writer, answer.writer)
               && Objects.equals(question, answer.question)
               && Objects.equals(contents, answer.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, writer, question, contents, deleted);
    }
}
