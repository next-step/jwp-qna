package qna.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User extends BaseTimeEntity {

    public static final GuestUser GUEST_USER = new GuestUser();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private UserInfo userInfo;

    private User() {
    }

    public User(String userId, String password, String name, String email) {
        this(null, userId, password, name, email);
    }

    public User(Long id, String userId, String password, String name, String email) {
        this.id = id;
        this.userInfo = new UserInfo(userId, password, name, email);
    }

    public void update(User loginUser, User target) {
        this.userInfo.update(loginUser.getUserInfo(), target.getUserInfo());
    }

    public boolean isGuestUser() {
        return false;
    }

    public Long getId() {
        return id;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public String getUserId() {
        return this.userInfo.userId();
    }


    private static class GuestUser extends User {

        @Override
        public boolean isGuestUser() {
            return true;
        }
    }
}