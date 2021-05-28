package qna.domain;

import qna.UnAuthorizedException;
import qna.domain.wrap.Email;
import qna.domain.wrap.Name;
import qna.domain.wrap.Password;
import qna.domain.wrap.UserId;

import javax.persistence.*;
import java.util.Objects;

@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_a3imlf41l37utmxiquukk8ajc", columnNames = {"userId"})
        }
)
@Entity
public class User extends BaseEntity {
    public static final GuestUser GUEST_USER = new GuestUser();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UserId userId;

    private Password password;

    private Name name;

    private Email email;

    protected User() {
    }

    public User(String userId, String password, String name, String email) {
        this(null, userId, password, name, email);
    }

    public User(Long id, String userId, String password, String name, String email) {
        this(id, new UserId(userId), new Password(password), new Name(name), new Email(email));
    }

    public User(Long id, UserId userId, Password password, Name name, Email email) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public void update(User loginUser, User target) {
        if (!matchUserId(loginUser.userId) || !matchPassword(target.password)) {
            throw new UnAuthorizedException();
        }

        this.name = target.name;
        this.email = target.email;
    }

    private boolean matchUserId(UserId userId) {
        return this.userId.equals(userId);
    }

    public boolean matchPassword(Password targetPassword) {
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
