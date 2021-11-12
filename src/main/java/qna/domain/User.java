package qna.domain;

import qna.UnAuthorizedException;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User extends BaseEntity {
    public static final GuestUser GUEST_USER = new GuestUser();

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "password", length = 20, nullable = false)
    private String password;

    @Column(name = "user_id", length = 20, nullable = false, unique = true)
    private String userId;

    protected User() {
    }

    public User(String userId, String password, String name, String email) {
        this(null, userId, password, name, email);
    }

    public User(Long id, String userId, String password, String name, String email) {
        super(id);
        validate(userId, password, name);
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    private void validate(String userId, String password, String name) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("userId를 입력하세요");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("password를 입력하세요");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name를 입력하세요");
        }
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

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    private static class GuestUser extends User {
        @Override
        public boolean isGuestUser() {
            return true;
        }
    }
}
