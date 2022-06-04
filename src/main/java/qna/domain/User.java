package qna.domain;

import qna.UnAuthorizedException;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class User extends BaseTimeEntity {
    public static final GuestUser GUEST_USER = new GuestUser();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false, unique = true)
    private String userId;
    @Column(length = 20, nullable = false, unique = true)
    private String password;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 50)
    private String email;
    @Embedded
  
    private final Questions questions = new Questions();

    protected User() {
    }
  
    public User(final Long id, final String userId, final String password, final String name, final String email) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User(final String userId, final String password, final String name, final String email) {
        this(null, userId, password, name, email);
    }

    public User(final String password, final String name, final String email) {
        this(null, null, password, name, email);
    }

    public void addQuestion(final Question question) {
        this.questions.add(question);

        if (question.getWriter() != this) {
            question.updateWriter(this);
        }
    }

    public boolean containQuestion(final Question question) {
        return this.questions.contains(question);
    }

    public void updateNameAndEmail(final User loginUser, final User target) {
        if (!matchUserId(loginUser.userId)) {
            throw new UnAuthorizedException();
        }

        if (!matchPassword(target.password)) {
            throw new UnAuthorizedException();
        }

        this.name = target.name;
        this.email = target.email;
    }

    private boolean matchUserId(final String userId) {
        return this.userId.equals(userId);
    }

    public boolean matchPassword(final String targetPassword) {
        return this.password.equals(targetPassword);
    }

    public boolean equalsNameAndEmail(final User target) {
        if (Objects.isNull(target)) {
            return false;
        }

        return name.equals(target.name) &&
                email.equals(target.email);
    }

    public boolean isOwner(final User writer) {
        return this.equals(writer);
    }

    public boolean isGuestUser() {
        return false;
    }

    public List<Question> getQuestions() {
        return questions.getQuestion();

    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", question=" + questions +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(userId, user.userId) && Objects.equals(password, user.password) && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(questions, user.questions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, password, name, email, questions);

    }

    private static class GuestUser extends User {
        @Override
        public boolean isGuestUser() {
            return true;
        }
    }
}
