package qna.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import qna.UnAuthorizedException;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
public class User {
    public static final GuestUser GUEST_USER = new GuestUser();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    @NotNull
    private LocalDateTime createdAt;
    @Column(length = 50)
    private String email;
    @Column(length = 20)
    @NotNull
    private String name;
    @Column(length = 20)
    @NotNull
    private String password;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @Column(length = 20)
    @NotNull
    private String userId;

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

    public boolean matchPassword(String targetPassword) {
        return this.password.equals(targetPassword);
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

    @NoArgsConstructor
    private static class GuestUser extends User {
        public GuestUser(String userId, String password, String name, String email) {
            super(userId, password, name, email);
        }

        public GuestUser(Long id, String userId, String password, String name, String email) {
            super(id, userId, password, name, email);
        }

        @Override
        public boolean isGuestUser() {
            return true;
        }
    }
}
