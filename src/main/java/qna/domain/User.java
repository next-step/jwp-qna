package qna.domain;

import qna.ForbiddenException;
import qna.UnAuthorizedException;
import qna.constant.ErrorCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class User extends BaseDateEntity{
    public static final GuestUser GUEST_USER = new GuestUser();

    public static User create(String userId, String password, String name, String email) {
        return new User(userId, password, name, email);
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Email email;
    @Embedded
    private Name name;
    @Embedded
    private Password password;
    @Embedded
    private UserId userId;

    protected User() {}

    private User(String userId, String password, String name, String email) {
        this(null, userId, password, name, email);
    }

    private User(Long id, String userId, String password, String name, String email) {
        this.id = id;
        this.userId = UserId.of(userId);
        this.password = Password.of(password);
        this.name = Name.of(name);
        this.email = Email.of(email);
    }

    public void update(User loginUser, User target) {
        if (!userId.matchUserId(loginUser.userId)) {
            throw new UnAuthorizedException(ErrorCode.사용자_수정_권한_예외.getErrorMessage());
        }

        if (!password.matchPassword(target.password)) {
            throw new UnAuthorizedException(ErrorCode.사용자_수정_권한_예외.getErrorMessage());
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email.toString() + '\'' +
                '}';
    }

    private static class GuestUser extends User {
        @Override
        public boolean isGuestUser() {
            return true;
        }
    }
}
