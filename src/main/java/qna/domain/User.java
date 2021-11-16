package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String email;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String password;

    @Column(length = 20, nullable = false, unique = true)
    private String userId;

    protected User() {
    }

    public User(final String userId, final String password, final String name, final String email) {
        this(null, userId, password, name, email);
    }

    public User(final Long id, final String userId, final String password, final String name, final String email) {
        this.id = id;
        this.userId = Objects.requireNonNull(userId);
        this.password = Objects.requireNonNull(password);
        this.name = Objects.requireNonNull(name);
        this.email = email;
    }

    public boolean equalsNameAndEmail(final User target) {
        if (Objects.isNull(target)) {
            return false;
        }

        return name.equals(target.name) &&
            email.equals(target.email);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
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
}
