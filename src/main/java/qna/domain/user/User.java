package qna.domain.user;

import qna.domain.BaseTimeEntity;
import qna.exception.UnAuthenticationException;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class User extends BaseTimeEntity {
    public static final GuestUser GUEST_USER = new GuestUser();

    @Id
    @GeneratedValue(strategy = IDENTITY)
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

    public User(Long id, String userId, String password, String name, String email) {
        this.id = id;
        this.userId = new UserId(userId);
        this.password = new Password(password);
        this.name = new Name(name);
        this.email = new Email(email);
    }

    public void update(User target) throws UnAuthenticationException {
        isAuthentication(target);
        userId.isMatch(target.userId);
        password.isMatch(target.password);

        this.name = target.name;
        this.email = target.email;
    }

    public void isAuthentication(User target) throws UnAuthenticationException {
        if (Objects.isNull(target)) {
            throw new UnAuthenticationException("사용자 정보를 확인할 수 없습니다.");
        }
    }

    public boolean equalsNameAndEmail(User target) {
        return name.equals(target.name) && email.equals(target.email);
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

    public Email getEmail() {
        return email;
    }

    private static class GuestUser extends User {
        @Override
        public boolean isGuestUser() {
            return true;
        }
    }
}
