package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private UserId userId;

    @Embedded
    private Password password;

    @Embedded
    private Name name;

    @Column(length = 50)
    @Embedded
    private Email email;

    protected User() {
    }

    public User(final String userId, final String password, final String name) {
        this(null, userId, password, name, null);
    }

    public User(final String userId, final String password, final String name, final String email) {
        this(null, userId, password, name, email);
    }

    public User(final Long id, final String userId, final String password, final String name, final String email) {
        this.id = id;
        this.userId = new UserId(userId);
        this.password = new Password(password);
        this.name = new Name(name);
        this.email = new Email(email);
    }

    public Long getId() {
        return id;
    }

    public UserId getUserId() {
        return userId;
    }

    public Name getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final User user = (User)o;
        return Objects.equals(id, user.id) && Objects.equals(userId, user.userId) && Objects.equals(password,
            user.password) && Objects.equals(name, user.name) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, password, name, email);
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
