package qna.question;

import qna.CannotDeleteException;
import qna.answer.Answer;
import qna.domain.DateTimeEntity;
import qna.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
public class Question extends DateTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User user;

    @Lob
    @Column(name = "contests")
    private String contents;

    @Column(name = "delete", nullable = false)
    private boolean deleted = false;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    public Question(String title, String contents, User user) {
        this(null, title, contents, user);
    }

    public Question(Long id, String title, String contents, User user) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.user = user;
    }

    protected Question() {
    }

    public void throwExceptionNotDeletableUser(final User loginUser) throws CannotDeleteException {
        if (!this.user.equals(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
    }

    public void addAnswer(final Answer answer) {
        answers.add(answer);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public List<Answer> getAnswers() {
        return new ArrayList<>(answers);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void deleteQuestion() {
        this.deleted = true;
    }

    public void deleteAnswers() {
        for (Answer answer : answers) {
            answer.deleteAnswer();
        }
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", user=" + user +
                ", deleted=" + deleted +
                '}';
    }

    public void throwExceptionNotDeletableAnswers(User loginUser) throws CannotDeleteException {
        for (Answer answer : answers) {
            answer.throwExceptionNotDeletableUser(loginUser);
        }
    }
}
