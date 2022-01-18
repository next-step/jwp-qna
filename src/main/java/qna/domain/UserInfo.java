package qna.domain;

import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import qna.UnAuthorizedException;

@Embeddable
public class UserInfo {

    @Embedded
    private UserId userId;

    @Embedded
    private Password password;

    @Embedded
    private Name name;

    @Embedded
    private Email email;

    public UserInfo(String userId, String password, String name, String email) {
        this.userId = new UserId(userId);
        this.password = new Password(password);
        this.name = new Name(name);
        this.email = new Email(email);
    }

    public void update(UserInfo loginUser, UserInfo target) {
        checkAuthorization(loginUser.getUserId(), target.getPassword());
        this.name = target.getName();
        this.email = target.getEmail();
    }

    private void checkAuthorization(UserId userId, Password password) {
        if (!this.userId.equals(userId) || !this.password.equals(password)) {
            throw new UnAuthorizedException();
        }
    }

    public String userId() {
        return userId.getUserId();
    }

    private UserId getUserId() {
        return userId;
    }

    private Password getPassword() {
        return password;
    }

    private Name getName() {
        return name;
    }

    private Email getEmail() {
        return email;
    }

    public boolean equalsNameAndEmail(UserInfo target) {
        if (Objects.isNull(target)) {
            return false;
        }

        return name.equals(target.getName()) &&
            email.equals(target.getEmail());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInfo userInfo = (UserInfo) o;
        return userId.equals(userInfo.userId) && password.equals(userInfo.password) && name
            .equals(userInfo.name) && Objects.equals(email, userInfo.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, password, name, email);
    }

    @Override
    public String toString() {
        return "UserInfo{" +
            "userId=" + userId +
            ", password=" + password +
            ", name=" + name +
            ", email=" + email +
            '}';
    }
}
