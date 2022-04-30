package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class LoginInfo {

    @Column(name = "user_id", length = 20, nullable = false)
    private String userId;

    @Column(length = 20, nullable = false)
    private String password;

    @Column(length = 50)
    private String email;

    public LoginInfo() {
    }

    public LoginInfo(String userId, String password, String email) {
        this.userId = userId;
        this.password = password;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public boolean equalsUserIdAndPassword(LoginInfo target) {
        return userId.equals(target.userId) && password.equals(target.password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginInfo)) return false;
        LoginInfo loginInfo = (LoginInfo) o;
        return getUserId().equals(loginInfo.getUserId()) && getEmail().equals(loginInfo.getEmail()) && getPassword().equals(loginInfo.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getPassword(), getEmail());
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
