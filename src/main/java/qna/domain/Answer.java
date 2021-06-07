package qna.domain;

import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Answer extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Question question;

    @Lob
    private String contents;

    @Column(nullable = false)
    private boolean deleted;

    protected Answer () { }

    public Answer(User user, Question question, String contents) {
        this(null, user, question, contents);
    }

    public Answer(Long id, User user, Question question, String contents) {
        this.id = id;

        if (Objects.isNull(user)) {
            throw new UnAuthorizedException();
        }

        if (Objects.isNull(question)) {
            throw new NotFoundException();
        }

        this.user = user;
        this.question = question;
        this.contents = contents;
    }

    public boolean isOwner(User user) {
        return this.user.equals(user);
    }

    public void question(Question question) {
        this.question = question;
    }

    public Long id() {
        return id;
    }

    public void id(Long id) {
        this.id = id;
    }

    public User writer() {
        return user;
    }

    public Question question() {
        return question;
    }

    public boolean deleted() {
        return deleted;
    }

    public void deleted(boolean deleted) {
        this.deleted = deleted;
    }

    public DeleteHistory delete(User loginUser) {
        if (!this.isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        this.deleted(true);

        return new DeleteHistory(ContentType.ANSWER, this.id(), this.writer (), LocalDateTime.now ());
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writer=" + user.id() +
                ", questionId=" + question.id() +
                ", contents='" + contents + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
