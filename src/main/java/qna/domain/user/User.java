package qna.domain.user;

import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import qna.domain.BaseEntity;

@Entity
@Table(name = "user")
public class User extends BaseEntity {

    public static final GuestUser GUEST_USER = new GuestUser();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private UserAuth userAuth;

    @Embedded
    private UserData userData;

    protected User() {
    }

    public User(UserAuth userAuth, UserData userData) {
        this.userAuth = userAuth;
        this.userData = userData;
    }

    public void update(User loginUser, User changeUser) {
        this.userAuth.updateValid(loginUser.getUserAuth(), changeUser.getUserAuth());

        this.userData.update(changeUser.getUserData());
    }

    public void changeUserId(String changeUserId) {
        this.userAuth.changeUserId(changeUserId);
    }

    public boolean equalsNameAndEmail(User target) {
        return userData.equalsNameAndEmail(target.getUserData());
    }

    public boolean isMe(User writer) {
        return this.equals(writer);
    }

    public boolean isGuestUser() {
        return false;
    }

    public Long getId() {
        return id;
    }

    public UserAuth getUserAuth() {
        return userAuth;
    }

    public UserData getUserData() {
        return userData;
    }

    public String getUserId() {
        return userAuth.getUserId();
    }

    public String getName() {
        return userData.getName();
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
            ", userAuth=" + userAuth +
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
        return Objects.equals(id, user.id) && Objects.equals(userAuth.getUserId(),
            user.userAuth.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userAuth, userData);
    }
}
