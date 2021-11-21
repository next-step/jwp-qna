package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User extends BaseEntity {
    @Id
    private Long id;

    @Column(length = 50)
    private String email;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 20)
    private String password;

    @Column(nullable = false, unique = true, length = 20)
    private String userId;

    protected User() {
    }

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

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User)o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email)
            && Objects.equals(name, user.name) && Objects.equals(password, user.password)
            && Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, name, password, userId);
    }
}
