package qna.domain;

import com.sun.istack.NotNull;
import qna.UnAuthorizedException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 사용자.
 */
@Entity
public class User {
    public static final GuestUser GUEST_USER = new GuestUser();

    /**
     * 사용자 식별자.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 식별키.
     */
    @Column(nullable = false, unique = true, length = 20)
    private String userId;
    /**
     * 비밀 번호.
     */
    @Column(nullable = false, length = 20)
    private String password;
    /**
     * 사용자 이름.
     */
    @Column(nullable = false, length = 20)
    private String name;
    /**
     * 이메일.
     */
    @Column(length = 50)
    private String email;

    /**
     * 생성시간.
     */
    @NotNull
    private LocalDateTime createAt = LocalDateTime.now();

    /**
     * 수정시간.
     */
    private LocalDateTime updatedAt;

    protected User() {
        this(null, null, null, null);
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

    /**
     * 사용자 정보를 갱신한다.
     *
     * @param loginUser 로그인한 사용자.
     * @param target    갱신할 사용자 정보.
     */
    public void update(User loginUser, User target) {
        if (!matchUserId(loginUser.userId)) {
            throw new UnAuthorizedException();
        }

        if (!matchPassword(target.password)) {
            throw new UnAuthorizedException();
        }

        this.name = target.name;
        this.email = target.email;
    }

    private boolean matchUserId(String userId) {
        return this.userId.equals(userId);
    }

    /**
     * 주어진 비밀번호와 입력받은 비밀번호를 비교하여 일치하면 true, 일치하지 않으면 false를 리턴한다.
     *
     * @param targetPassword 입력받은 비밃번호
     * @return 비밀번호 일치 여부
     */
    public boolean matchPassword(String targetPassword) {
        return this.password.equals(targetPassword);
    }

    /**
     * 이름과 이메일을 비교하여 일치하면 true, 아니라면 false를 리턴한다.
     *
     * @param target 입력받은 사용자 정보
     * @return 이름과 이메일 일치여부
     */
    public boolean equalsNameAndEmail(User target) {
        if (Objects.isNull(target)) {
            return false;
        }

        return name.equals(target.name) &&
                email.equals(target.email);
    }

    /**
     * 게스트 사용자라면 true, 아니라면 false를 리턴한다.
     */
    public boolean isGuestUser() {
        return false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    private static class GuestUser extends User {
        @Override
        public boolean isGuestUser() {
            return true;
        }
    }
}
