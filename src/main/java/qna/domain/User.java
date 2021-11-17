package qna.domain;

import qna.UnAuthorizedException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class User extends BaseEntity{
    public static final GuestUser GUEST_USER = new GuestUser();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, updatable = false, unique = true)
    private String userId;
    @Column(nullable = false, updatable = false)
    private String password;
    @Column(nullable = false, updatable = false)
    private String name;
    private String email;
    @OneToMany(mappedBy = "writer")
    private List<Answer> answers = new ArrayList<>();
    @OneToMany(mappedBy = "writer")
    private List<Question> questions = new ArrayList<>();
    @OneToMany(mappedBy = "deletedByUser")
    private List<DeleteHistory> deleteHistories = new ArrayList<>();

    protected User() {
    }

    public User(String userId, String password, String name, String email) {
        this(null, userId, password, name, email);
    }

    public User(Long id, String userId, String password, String name, String email) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public void update(User loginUser, User target) {
        if (!matchUserId(loginUser.userId)) {
            throw new UnAuthorizedException();
        }

        if (!matchPassword(target.password)) {
            throw new UnAuthorizedException();
        }

        this.name = target.name;
        this.email = target.email;
    }

    private boolean matchUserId(String userId) {
        return this.userId.equals(userId);
    }

    public boolean matchPassword(String targetPassword) {
        return this.password.equals(targetPassword);
    }

    public boolean equalsNameAndEmail(User target) {
        if (Objects.isNull(target)) {
            return false;
        }

        return name.equals(target.name) &&
                email.equals(target.email);
    }

    public boolean isGuestUser() {
        return false;
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
        answer.setWriter(this);
    }

    public void addAQuestion(Question question) {
        this.questions.add(question);
        question.setWriter(this);
    }

    public void addDeleteHistory(DeleteHistory deleteHistory) {
        this.deleteHistories.add(deleteHistory);
        deleteHistory.setDeletedByUser(this);
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }

    private static class GuestUser extends User {
        @Override
        public boolean isGuestUser() {
            return true;
        }
    }
}
