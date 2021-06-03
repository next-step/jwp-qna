package qna.domain.question;

import qna.CannotDeleteException;
import qna.domain.CreateAndUpdateTimeEntity;
import qna.domain.Deleted;
import qna.domain.QnAExceptionMessage;
import qna.domain.history.ContentType;
import qna.domain.history.DeleteHistory;
import qna.domain.history.DeleteHistorys;
import qna.domain.user.User;
import qna.domain.answer.Answer;

import javax.persistence.*;
import java.util.Objects;

@Table(name = "question")
@Entity
public class Question extends CreateAndUpdateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 100)
    private String title;

    @Lob
    @Column(name = "contents")
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private Deleted deleted = new Deleted();

    @Embedded
    private AnswersInQuestion answers = new AnswersInQuestion();

    protected Question() {
        // empty
    }

    public Question(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public DeleteHistorys delete(final User loginUser) throws CannotDeleteException {
        checkOwnerToQuestionAndAnswer(loginUser);
        this.deleted.delete();
        return makeDeleteHistorys();
    }

    private DeleteHistorys makeDeleteHistorys() {
        DeleteHistorys deleteHistorys = DeleteHistorys.of(DeleteHistory.newInstance(ContentType.QUESTION, id, writer));
        deleteHistorys.addAll(answers.deleteAll());
        return deleteHistorys;
    }

    private void checkOwnerToQuestionAndAnswer(final User loginUser) throws CannotDeleteException {
        checkOwner(loginUser);
        this.answers.checkOwner(loginUser);
    }

    private void checkOwner(final User loginUser) throws CannotDeleteException {
        if (isOwner(loginUser) == false) {
            throw new CannotDeleteException(QnAExceptionMessage.NO_AUTH_QUESTION);
        }
    }

    public Long getId() {
        return id;
    }

    public boolean isDeleted() {
        return deleted.isDeleted();
    }

    public void addAnswer(Answer answer) {
        if(this.answers.add(answer) == false){
            return;
        }
        answer.toQuestion(this);
    }

    public boolean isSameTitle(String title) {
        return this.title.equals(title);
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public boolean isSameContents(String contents) {
        return this.contents.equals(contents);
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public User getWriter() {
        return this.writer;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question)o;
        return deleted == question.deleted
               && Objects.equals(id, question.id)
               && Objects.equals(title, question.title)
               && Objects.equals(contents, question.contents)
               && Objects.equals(writer, question.writer)
               && Objects.equals(answers, question.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, contents, writer, deleted, answers);
    }
}
