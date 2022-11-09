package qna.domain.user;

import qna.UnAuthorizedException;
import qna.domain.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

import static qna.constant.Message.NOT_VALID_EQUAL_PASSWORD;
import static qna.constant.Message.NOT_VALID_UPDATE_USER_ID;

@Entity
@Table(name = "user")
public class User extends BaseEntity {
    public static final GuestUser GUEST_USER = new GuestUser();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Embedded
    private UserId userId;
    @Embedded
    private Password password;
    @Embedded
    private Name name;
    @Embedded
    private Email email;

    protected User() {
    }

    public User(UserId userId, Password password, Name name, Email email) {
        this(null, userId, password, name, email);
    }

    public User(Long id, UserId userId, Password password, Name name, Email email) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public void update(User loginUser, User target) {
        if (!isMatchUserId(loginUser.userId)) {
            throw new UnAuthorizedException(NOT_VALID_UPDATE_USER_ID);
        }

        if (!isMatchPassword(target.password)) {
            throw new UnAuthorizedException(NOT_VALID_EQUAL_PASSWORD);
        }

        this.name = target.name;
        this.email = target.email;
    }

    private boolean isMatchUserId(UserId userId) {
        return this.userId.equals(userId);
    }

    public boolean isMatchPassword(Password targetPassword) {
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

    public UserId getUserId() {
        return userId;
    }

    public Password getPassword() {
        return password;
    }

    public Name getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public void changeEmail(Email email) {
        this.email = email;
    }

    public void changeName(Name name) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(userId, user.userId) && Objects.equals(password, user.password) && Objects.equals(name, user.name) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, password, name, email);
    }

    private static class GuestUser extends User {
        @Override
        public boolean isGuestUser() {
            return true;
        }
    }
}
