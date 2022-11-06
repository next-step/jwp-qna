package qna.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.Objects;

@Entity
public class User extends BaseEntity {
    public static final GuestUser GUEST_USER = new GuestUser();

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        this.userId = UserId.of(userId);
        this.password = Password.of(password);
        this.name = Name.of(name);
        this.email = Email.of(email);
    }

    public void update(User loginUser, User target) {
        this.userId.validateMatchUserId(loginUser.userId);
        this.password.validateMatchPassword(target.password);

        this.name = target.name;
        this.email = target.email;
    }

    public boolean equalsNameAndEmail(User target) {
        if (Objects.isNull(target)) {
            return false;
        }

        return isEqualName(target) &&
                isEqualEmail(target);
    }

    private boolean isEqualName(User user) {
        return name.equals(user.name);
    }

    private boolean isEqualEmail(User user) {
        return email.equals(user.email);
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

    public Email getEmail() {
        return email;
    }

    public Name getName() {
        return name;
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
        return Objects.equals(getUserId(), user.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userId=" + userId +
                ", password=" + password +
                ", name=" + name +
                ", email=" + email +
                '}';
    }

    private static class GuestUser extends User {
        @Override
        public boolean isGuestUser() {
            return true;
        }
    }
}
