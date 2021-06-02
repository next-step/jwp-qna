package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.*;

import qna.exceptions.UnAuthorizedException;
import qna.validators.StringValidator;

@Table
@Entity
public class User extends BaseEntity {

    public static final GuestUser GUEST_USER = new GuestUser();

    private static final int USER_ID_LENGTH = 20;
    private static final int PASSWORD_LENGTH = 20;
    private static final int NAME_LENGTH = 20;
    private static final int EMAIL_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = USER_ID_LENGTH, nullable = false)
    private String userId;

    @Column(length = PASSWORD_LENGTH, nullable = false)
    private String password;

    @Column(length = NAME_LENGTH, nullable = false)
    private String name;

    @Column(length = EMAIL_LENGTH)
    private String email;

    @Embedded
    private Questions questions = new Questions();

    @Embedded
    private Answers answers = new Answers();

    protected User() {
    }

    public User(String userId, String password, String name, String email) {
        this(null, userId, password, name, email);
    }

    public User(Long id, String userId, String password, String name, String email) {
        StringValidator.validate(userId, USER_ID_LENGTH);
        StringValidator.validate(password, PASSWORD_LENGTH);
        StringValidator.validate(name, NAME_LENGTH);
        StringValidator.validateNullable(email, EMAIL_LENGTH);

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

        return name.equals(target.name) && email.equals(target.email);
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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public List<Question> getQuestions(Status status) {
        return questions.getQuestions(status);
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public List<Answer> getAnswers(Status status) {
        return answers.getAnswers(status);
    }

    @Override
    public String toString() {
        return "User{"
            + "id=" + id
            + ", userId='" + userId + '\''
            + ", password='" + password + '\''
            + ", name='" + name + '\''
            + ", email='" + email + '\''
            + ", createdAt='" + getCreatedAt() + '\''
            + ", updatedAt='" + getUpdatedAt() + '\''
            + '}';
    }

    private static class GuestUser extends User {
        @Override
        public boolean isGuestUser() {
            return true;
        }
    }
}
