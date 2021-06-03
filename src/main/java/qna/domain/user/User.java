package qna.domain.user;

import qna.UnAuthorizedException;
import qna.domain.CreateAndUpdateTimeEntity;

import javax.persistence.*;
import java.util.Objects;
import java.util.StringJoiner;

@Table(name = "user")
@Entity
public class User extends CreateAndUpdateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true, length = 20)
    private String userId;

    @Column(name = "password", nullable = false, length = 20)
    private String password;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "email", length = 50)
    private String email;

    protected User() {
        // empty
    }

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public void update(User loginUser, User target) {
        checkOwner(loginUser);
        this.name = target.name;
        this.email = target.email;
    }

    public void checkOwner(final User loginUser) {
        if (matchUserId(loginUser.userId) == false) {
            throw new UnAuthorizedException();
        }

        if (matchPassword(loginUser.password) == false) {
            throw new UnAuthorizedException();
        }
    }

    private boolean matchUserId(String userId) {
        return this.userId.equals(userId);
    }

    private boolean matchPassword(String targetPassword) {
        return this.password.equals(targetPassword);
    }

    public boolean equalsNameAndEmail(User target) {
        if (Objects.isNull(target)) {
            return false;
        }

        return name.equals(target.name) &&
                email.equals(target.email);
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return this.userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User)o;
        return Objects.equals(id, user.id)
               && Objects.equals(userId, user.userId)
               && Objects.equals(password, user.password)
               && Objects.equals(name, user.name)
               && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, password, name, email);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
            .add("id=" + id)
            .add("userId='" + userId + "'")
            .add("password='" + password + "'")
            .add("name='" + name + "'")
            .add("email='" + email + "'")
            .toString();
    }
}
