package qna.domain;

import qna.CannotDeleteException;
import qna.UnAuthorizedException;
import qna.constant.ContentType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

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

    public DeleteHistories delete(User loginUser) throws CannotDeleteException {
        validationDeleteQuestionRequestUser(loginUser);
        validationDeleteAnswersRequestUser(loginUser);

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

    public boolean isDeletedStatusAnswers() {
        for (Answer answer : answers) {
            if (!answer.isDeleted()) {
                return false;
            }
        }
        return true;
    }

    private void validationDeleteQuestionRequestUser(User loginUser) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new UnAuthorizedException(writer, loginUser);
        }
    }

    private void validationDeleteAnswersRequestUser(User loginUser) throws CannotDeleteException {
        for (Answer answer : answers) {
            answer.isSameOwner(loginUser);
        }
    }

    private DeleteHistories generateDeleteHistories() {
        DeleteHistories deleteHistories = new DeleteHistories();
        deleteHistories.add(new DeleteHistory(QUESTION, id, writer, LocalDateTime.now()));
        for (Answer answer : answers) {
            answer.setDeleted(true);
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
        }
        return deleteHistories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
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

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
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
