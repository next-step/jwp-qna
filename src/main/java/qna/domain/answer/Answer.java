package qna.domain.answer;

import org.hibernate.annotations.Where;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.domain.CreateAndUpdateTimeEntity;
import qna.domain.Deleted;
import qna.domain.history.ContentType;
import qna.domain.history.DeleteHistory;
import qna.domain.question.Question;
import qna.domain.user.User;

import javax.persistence.*;
import java.util.Objects;
import java.util.Optional;

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

    @Embedded
    private Deleted deleted = new Deleted();

    protected Answer() {
        // empty
    }

    public Answer(User writer, Question question, String contents) {
        this.writer = Optional.ofNullable(writer)
                              .orElseThrow(() -> new UnAuthorizedException());
        this.question = Optional.ofNullable(question)
                                .orElseThrow(() -> new NotFoundException());;
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
        return deleted.isDeleted();
    }

    public DeleteHistory delete() {
        this.deleted.delete();
        return DeleteHistory.newInstance(ContentType.ANSWER, id, writer);
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
