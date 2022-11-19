package qna.domain;

import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class Answer extends BaseDateEntity{
    // test 용
    public static Answer create(User writer, Question question) {
        return new Answer(writer, question, "contents");
    }
    public static Answer create(User writer, Question question, String contents) {
        return new Answer(writer, question, contents);
    }


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Contents contents;
    @Column(nullable = false)
    private boolean deleted = false;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "QUESTION_ID")
    private Question question;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "WRITER_ID")
    private User writer;

    protected Answer() {}

    private Answer(User writer, Question question, String contents) {
        this(null, writer, question, contents);
    }

    private Answer(Long id, User writer, Question question, String contents) {
        this.id = id;

        if (Objects.isNull(writer)) {
            throw new UnAuthorizedException();
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        this.contents = Contents.of(contents);
        this.writer = writer;
        addQuestion(question);
    }


    public void addQuestion(Question question) {
        if(this.question != null) {
            this.question.getAnswers().remove(this);
        }
        this.question = question;
        question.getAnswers().add(this);
    }

    public boolean isOwner(User loginUser) {
        return this.writer.equals(loginUser);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Contents getContents() {
        return contents;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void makeDeleted() {
        this.deleted = true;
        DeleteHistory.create(ContentType.ANSWER, this.id, this.writer);
    }

    public Question getQuestion() {
        return question;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User user) {
        this.writer = user;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writerId=" + writer.getId() +
                ", questionId=" + question.getId() +
                ", contents='" + contents.toString() + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
