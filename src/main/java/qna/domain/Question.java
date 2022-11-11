package qna.domain;

import qna.UnAuthorizedException;

import javax.persistence.*;
import java.time.LocalDateTime;

import static qna.constant.ContentType.QUESTION;

@Entity
public class Question extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writerId", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Embedded
    private Answers answers = new Answers();

    @Column(nullable = false)
    private boolean deleted = false;

    protected Question() {}

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public DeleteHistories delete(User loginUser) {
        this.validationDeleteQuestionRequestUser(loginUser);
        answers.validationDeleteAnswersRequestUser(loginUser);

        setDeleted(true);

        return generateDeleteHistories();
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        this.answers.add(answer);
    }

    public boolean isAllDeletedAnswers() {
        return answers.isAllDeleted();
    }

    private void validationDeleteQuestionRequestUser(User loginUser) {
        if (!isOwner(loginUser)) {
            throw new UnAuthorizedException(writer, loginUser);
        }
    }

    private DeleteHistories generateDeleteHistories() {
        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.add(new DeleteHistory(QUESTION, id, writer, LocalDateTime.now()));
        deleteHistories.addAll(answers.generateDeleteHistories());
        return deleteHistories;
    }

    public Long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
