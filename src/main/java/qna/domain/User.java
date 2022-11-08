package qna.domain;

import qna.exception.UnAuthorizedException;
import qna.common.AuditingEntity;
import qna.exception.message.UserExceptionCode;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(
        name = "user_id_unique",
        columnNames = {"user_id"}
)})
public class User extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", length = 20, nullable = false)
    private String userId;

    @Column(length = 20, nullable = false)
    private String password;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 50)
    private String email;

    protected User() {}

    public User(String userId, String password, String name, String email) {
        this(null, userId, password, name, email);
    }

    public User(Long id, String userId, String password, String name, String email) {
        validateForInserts(userId, password, name);

        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    private void validateForInserts(String userId, String password, String name) {
        validateUserId(userId);
        validatePassword(password);
        validateName(name);
    }

    private void validateUserId(String userId) {
        if(Objects.isNull(userId)) {
            throw new IllegalArgumentException(UserExceptionCode.REQUIRED_USER_ID.getMessage());
        }
    }

    private void validatePassword(String password) {
        if(Objects.isNull(password)) {
            throw new IllegalArgumentException(UserExceptionCode.REQUIRED_PASSWORD.getMessage());
        }
    }

    private void validateName(String name) {
        if(Objects.isNull(name)) {
            throw new IllegalArgumentException(UserExceptionCode.REQUIRED_NAME.getMessage());
        }
    }

    public void update(User loginUser, User target) {
        validateForUpdates(loginUser);

        this.name = target.name;
        this.email = target.email;
    }

    private void validateForUpdates(User loginUser) {
        matchUserId(loginUser.userId);
        matchPassword(loginUser.password);
    }

    private void matchUserId(String userId) {
        if(!this.userId.equals(userId)) {
            throw new UnAuthorizedException(UserExceptionCode.NOT_MATCH_USER_ID.getMessage());
        }
    }

    private void matchPassword(String targetPassword) {
        if(!this.password.equals(targetPassword)) {
            throw new UnAuthorizedException(UserExceptionCode.NOT_MATCH_PASSWORD.getMessage());
        }
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
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
        return Objects.equals(id, user.id) && Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }
}
