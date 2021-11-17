package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import qna.common.exception.InvalidParamException;
import qna.common.exception.UnAuthorizedException;

@Embeddable
public class UserData {

    @Column(name = "user_id", unique = true, length = 20, nullable = false)
    private String userId;

    @Column(name = "password", length = 20, nullable = false)
    private String password;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Embedded
    private Email email;

    protected UserData() {
    }

    public UserData(String userId, String password, String name, Email email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        valid();
    }


    public void update(User loginUser, User target) {
        if (!matchUserId(loginUser.getUserId())) {
            throw new UnAuthorizedException(
                UnAuthorizedException.UNAUTHORIZED_EXCEPTION_USER_ID_NOT_SAME_MESSAGE);
        }

        if (!matchPassword(target.getPassword())) {
            throw new UnAuthorizedException(
                UnAuthorizedException.UNAUTHORIZED_EXCEPTION_MISS_MATCH_PASSWORD_MESSAGE);
        }

        this.name = target.getName();
        this.email = target.getEmail();
    }

    public void changeUserId(String changeUserId) {
        this.userId = changeUserId;
    }

    public boolean equalsNameAndEmail(User target) {
        if (Objects.isNull(target)) {
            return false;
        }

        return name.equals(target.getName()) &&
            email.equals(target.getEmail());
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

    public Email getEmail() {
        return email;
    }


    private void valid() {
        if (Objects.isNull(userId) || Objects.isNull(password) || Objects.isNull(name) ||
            Objects.isNull(email)) {
            throw new InvalidParamException("모든 값이 존재해야 합니다.");
        }
    }

    private boolean matchUserId(String userId) {
        return this.userId.equals(userId);
    }

    private boolean matchPassword(String targetPassword) {
        return this.password.equals(targetPassword);
    }

    @Override
    public String toString() {
        return "UserData{" +
            "userId='" + userId + '\'' +
            ", password='" + password + '\'' +
            ", name='" + name + '\'' +
            ", email=" + email.getEmail() +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserData userData = (UserData) o;
        return Objects.equals(userId, userData.userId)
            && Objects.equals(password, userData.password)
            && Objects.equals(name, userData.name)
            && Objects.equals(email, userData.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, password, name, email);
    }
}
