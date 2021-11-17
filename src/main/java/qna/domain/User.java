package qna.domain;

import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User extends BaseEntity {

    public static final GuestUser GUEST_USER = new GuestUser();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private UserData userData;

    protected User() {
    }

    public User(String userId, String password, String name, Email email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public void update(User loginUser, User target) {
        userData.update(loginUser, target);
    }

    public boolean equalsNameAndEmail(User target) {
        return userData.equalsNameAndEmail(target);
    }

    public void changeUserId(String changeUserId) {
        userData.changeUserId(changeUserId);
    }

    public boolean isMine(User writer) {
        return this.equals(writer);
    }

    public boolean isGuestUser() {
        return false;
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userData.getUserId();
    }

    public String getPassword() {
        return userData.getPassword();
    }

    public String getName() {
        return userData.getName();
    }

    public Email getEmail() {
        return userData.getEmail();
    }

    private static class GuestUser extends User {

        @Override
        public boolean isGuestUser() {
            return true;
        }
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", userData=" + userData +
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
        return Objects.equals(id, user.id)
            && Objects.equals(userData, user.userData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userData);
    }
}
