package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User extends AuditTimeBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String password;

    @Column(length = 20, nullable = false, unique = true)
    private String userId;

    public User(String userId, String password, String name, String email) {
        this(null, userId, password, name, email);
    }

    protected User(Long id, String userId, String password, String name, String email) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    protected User() {
    }

    public static User from(User user) {
        if (Objects.isNull(user)) {
            return null;
        }
        return new User(user.id, user.userId, user.password, user.name, user.email);
    }

    public Long getId() {
        return id;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", getCreatedAt()=" + getCreatedAt() +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", getUpdatedAt()=" + getUpdatedAt() +
                ", userId='" + userId + '\'' +
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
        return Objects.equals(id, user.id) && Objects.equals(email, user.email)
                && Objects.equals(name, user.name) && Objects.equals(password, user.password)
                && Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, password, userId);
    }
}
