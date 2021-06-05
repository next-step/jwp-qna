package qna.domain;

import qna.UnAuthorizedException;
import qna.domain.wrappers.UserEmail;
import qna.domain.wrappers.UserId;
import qna.domain.wrappers.UserName;
import qna.domain.wrappers.UserPassword;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user",
        uniqueConstraints = @UniqueConstraint(name = "UK_USER_USER_ID", columnNames = "user_id"))
public class User extends BaseEntity {
    public static final GuestUser GUEST_USER = new GuestUser();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private UserId userId = new UserId();

    @Embedded
    private UserPassword password = new UserPassword();

    @Embedded
    private UserName name = new UserName();

    @Embedded
    private UserEmail email = new UserEmail();

    protected User() {
    }

    public User(String userId, String password, String name, String email) {
        this(null, userId, password, name, email);
    }

    public User(Long id, String userId, String password, String name, String email) {
        this.id = id;
        this.userId = new UserId(userId);
        this.password = new UserPassword(password);
        this.name = new UserName(name);
        this.email = new UserEmail(email);
    }

    public void update(User loginUser, User target) {
        if (!matchUserId(loginUser.userId())) {
            throw new UnAuthorizedException();
        }

        if (!matchPassword(target.password())) {
            throw new UnAuthorizedException();
        }

        this.name = target.name;
        this.email = target.email;
    }

    private boolean matchUserId(UserId userId) {
        return this.userId.equals(userId);
    }

    public boolean matchPassword(UserPassword targetPassword) {
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

    public Long id() {
        return id;
    }

    public void id(Long id) {
        this.id = id;
    }

    public UserId userId() {
        return userId;
    }

    public void userId(String userId) {
        this.userId = new UserId(userId);
    }

    public UserPassword password() {
        return password;
    }

    public void password(String password) {
        this.password = new UserPassword(password);
    }

    public UserName name() {
        return name;
    }

    public void name(String name) {
        this.name = new UserName(name);
    }

    public UserEmail email() {
        return email;
    }

    public void email(String email) {
        this.email = new UserEmail(email);
    }

    public boolean isSameUser(User user) {
        return this.equals(user);
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
