package qna.domain;

import qna.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User extends BaseEntity {
    public static final GuestUser GUEST_USER = new GuestUser();

    @Column(name = "user_id", length = 20, nullable = false, unique = true)
    private String userId;
    @Column(name = "password", length = 20, nullable = false)
    private String password;
    @Column(name = "name", length = 20, nullable = false)
    private String name;
    @Column(name = "email", length = 50)
    private String email;

    protected User() {}

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

        return this.name.equals(target.name) &&
                this.email.equals(target.email);
    }

    public boolean isGuestUser() {
        return false;
    }

    public Long getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + this.id +
                ", userId='" + this.userId + '\'' +
                ", password='" + this.password + '\'' +
                ", name='" + this.name + '\'' +
                ", email='" + this.email + '\'' +
                '}';
    }

    private static class GuestUser extends User {
        @Override
        public boolean isGuestUser() {
            return true;
        }
    }
}
