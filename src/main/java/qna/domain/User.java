package qna.domain;

import qna.UnAuthorizedException;
import qna.domain.field.Email;
import qna.domain.field.Name;
import qna.domain.field.Password;
import qna.domain.field.UserId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User extends BaseEntity {
    public static final GuestUser GUEST_USER = new GuestUser();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private Name name;

    @Embedded
    private Password password;

    @Embedded
    private UserId userId;

    @OneToMany(mappedBy = "writer")
    private List<Answer> answers = new ArrayList<Answer>();

    @OneToMany(mappedBy = "writer")
    private List<Question> question = new ArrayList<Question>();

    @OneToMany(mappedBy = "deleteUser")
    private List<DeleteHistory> deleteHistories = new ArrayList<DeleteHistory>();

    // Arguments가 없는 Default Constructor 생성
    protected User() {}

    public User(String userId, String password, String name, String email) {
        this(null, userId, password, name, email);
    }

    public User(Long id, String userId, String password, String name, String email) {
        this.id = id;
        this.userId = new UserId(userId);
        this.password = new Password(password);
        this.name = new Name(name);
        this.email = new Email(email);
    }

    public void update(User loginUser, User target) {
        if (!matchUserId(loginUser.userId)) {
            throw new UnAuthorizedException();
        }

        if (!matchPassword(target.password.getPassword())) {
            throw new UnAuthorizedException();
        }

        this.name = target.name;
        this.email = target.email;
    }

    /**
     * User Password 변경
     * @param loginUser
     * @param newPassword
     */
    public void updatePassword(User loginUser, String newPassword) {
        if (!matchUserId(loginUser.userId)) {
            throw new UnAuthorizedException();
        }

        if (!matchPassword(newPassword)) {
            this.password.setPassword(newPassword);
        }
    }

    private boolean matchUserId(UserId userId) {
        return this.userId.getUserId().equals(userId.getUserId());
    }

    public boolean matchPassword(String targetPassword) {
        return this.password.equals(new Password(targetPassword));
    }

    public boolean equalsNameAndEmail(User target) {
        if (Objects.isNull(target)) {
            return false;
        }

        return name.equals(target.name) &&
                email.equals(target.email);
    }

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
        return userId.getUserId();
    }

    public void setUserId(String userId) {
        this.userId = new UserId(userId);
    }

    public String getPassword() {
        return this.password.getPassword();
    }

    public void setPassword(String password) {
        this.password.setPassword(password);
    }

    public String getName() {
        return this.name.getName();
    }

    public void setName(String name) {
        this.name.setName(name);
    }

    public String getEmail() {
        return this.email.getEmail();
    }

    public void setEmail(String email) {
        this.email.setEmail(email);
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
