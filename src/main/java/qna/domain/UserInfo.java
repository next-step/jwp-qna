package qna.domain;

import qna.exception.UnAuthorizedException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserInfo {

    @Column(length = 20, nullable = false)
    private String name;

    @Column(name = "user_id", length = 20, nullable = false)
    private String userId;

    @Column(length = 20, nullable = false)
    private String password;

    @Column(length = 50)
    private String email;

    public UserInfo() {
    }

    public UserInfo(String name, String userId, String password, String email) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.email = email;
    }

    public String getEmail() {
        return email;
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

    public void update(UserInfo loginUser, UserInfo target) {
        if (!matchUserId(loginUser.userId)) {
            throw new UnAuthorizedException();
        }

        if (!matchPassword(target.password)) {
            throw new UnAuthorizedException();
        }

        this.name = target.name;
        this.email = target.email;
    }

    public boolean matchUserId(String userId) {
        return this.userId.equals(userId);
    }

    public boolean matchPassword(String targetPassword) {
        return this.password.equals(targetPassword);
    }

    public boolean equalsNameAndEmail(UserInfo target) {
        return name.equals(target.name) && email.equals(target.email);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserInfo)) return false;
        UserInfo userInfo = (UserInfo) o;
        return getEmail().equals(userInfo.getEmail()) && getUserId().equals(userInfo.getUserId()) && getPassword().equals(userInfo.getPassword()) && getName().equals(userInfo.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getUserId(), getPassword(), getName());
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
