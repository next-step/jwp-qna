package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserLogin {

    @Column(name = "user_id", length = 20, nullable = false)
    private String userId;

    @Column(length = 20, nullable = false)
    private String password;

    @Column(length = 50)
    private String email;

    public UserLogin() {
    }

    public UserLogin(String userId, String password, String email) {
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

    public boolean equalsUserIdAndPassword(UserLogin target) {
        return userId.equals(target.userId) && password.equals(target.password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserLogin)) return false;
        UserLogin userLogin = (UserLogin) o;
        return getUserId().equals(userLogin.getUserId()) && getEmail().equals(userLogin.getEmail()) && getPassword().equals(userLogin.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getPassword(), getEmail());
    }

    @Override
    public String toString() {
        return "UserLogin{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
