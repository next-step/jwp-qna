package qna.domain;

import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import qna.domain.vo.UserAuthentication;
import qna.domain.vo.UserDetail;

@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private UserAuthentication authentication;

    @Embedded
    private UserDetail userDetail;

    protected User() {
    }

    public User(String userId, String password, String name, String email) {
        this(null, userId, password, name, email);
    }

    public User(Long id, String userId, String password, String name, String email) {
        this.id = id;
        this.authentication = UserAuthentication.of(userId, password);
        this.userDetail = UserDetail.of(name, email);
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return authentication.getUserId();
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", authentication=" + authentication +
            ", userDetail=" + userDetail +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User)o;
        return Objects.equals(getUserId(), user.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId());
    }

    public boolean authenticate(User loginUser) {
        return authentication.equals(loginUser.authentication);
    }
}
