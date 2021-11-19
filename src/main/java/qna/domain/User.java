package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToMany;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import qna.UnAuthorizedException;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class User extends BaseEntity {

    public static final GuestUser GUEST_USER = new GuestUser();

    @Embedded
    private UserId userId;

    @Embedded
    private Password password;

    @Embedded
    private Name name;

    @Column(length = 50)
    private String email;

    @OneToMany(mappedBy = "writer")
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "deleter")
    private List<DeleteHistory> deleteHistories = new ArrayList<>();

    protected User() {
    }

    public User(UserId userId, Password password, Name name, String email) {
        this(null, userId, password, name, email);
    }

    public User(Long id, UserId userId, Password password, Name name, String email) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public void update(User loginUser, User target) {
        if (!userId.match(loginUser.userId)) {
            throw new UnAuthorizedException();
        }

        if (!password.match(target.password)) {
            throw new UnAuthorizedException();
        }

        this.name = target.name;
        this.email = target.email;
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

    public UserId getUserId() {
        return userId;
    }

    public Name getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
        answer.toWriter(this);
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void addQuestion(Question question) {
        questions.add(question);
        question.toWriter(this);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void addDeleteHistory(DeleteHistory deleteHistory) {
        deleteHistories.add(deleteHistory);
        deleteHistory.toDeleter(this);
    }

    public List<DeleteHistory> getDeleteHistories() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return userId.equals(user.userId) && name.equals(user.name) && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name, email);
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", userId='" + userId + '\'' +
            ", password='" + password + '\'' +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            '}';
    }

    private static class GuestUser extends User {

        @Override
        public boolean isGuestUser() {
            return true;
        }
    }
}
