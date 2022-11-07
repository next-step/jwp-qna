package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class User extends BaseTimeEntity {
    public static final GuestUser GUEST_USER = new GuestUser();

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private UserAuth userAuth;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(length = 50)
    private String email;

    protected User() {
    }

    public User(UserAuth userAuth, String name, String email) {
        this.userAuth = userAuth;
        this.name = name;
        this.email = email;
    }

    public void update(User login, User target) {
        userAuth.validUpdate(login.userAuth, target.userAuth);

        this.name = target.name;
        this.email = target.email;
    }

    public boolean equalsNameAndEmail(User target) {
        if (Objects.isNull(target)) {
            return false;
        }

        return name.equals(target.name) &&
                email.equals(target.email);
    }

    public boolean isNot(User loginUser) {
        return !this.equals(loginUser);
    }

    public boolean isGuestUser() {
        return false;
    }

    public Long getId() {
        return id;
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
        return Objects.equals(id, user.id)
                && Objects.equals(userAuth, user.userAuth)
                && Objects.equals(name, user.name)
                && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userAuth, name, email);
    }

    private static class GuestUser extends User {
        @Override
        public boolean isGuestUser() {
            return true;
        }
    }
}
