package qna.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import qna.UnAuthorizedException;

import java.util.Objects;

@Entity
@Table(name = "user")
public class User extends BaseDateTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    public Long id() {
        return id;
    }

    public String userId() {
        return this.userId;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
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
        User user = (User) o;
        return Objects.equals(id(), user.id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id());
    }
}
