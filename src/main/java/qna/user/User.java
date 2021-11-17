package qna.user;

import qna.exception.UnAuthorizedException;
import qna.domain.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User extends BaseEntity {
    public static final GuestUser GUEST_USER = new GuestUser();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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

    public User(String userId, String password, String name, String email) {
        this(null, userId, password, name, email);
    }

    public User(Long id, String userId, String password, String name, String email) {
        this.id = id;
        this.userId = new UserId(userId);
        this.password = new Password(password);
        this.name = new Name(name);
        this.email = new Email(email);
    }

    public void update(User loginUser, User target) {
        if (!userId.equals(loginUser.userId)) {
            throw new UnAuthorizedException();
        }

        if (!password.equals(target.password)) {
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

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.getId())
                && Objects.equals(userId, user.userId)
                && Objects.equals(password, user.password)
                && Objects.equals(name, user.name)
                && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, password, name, email);
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
