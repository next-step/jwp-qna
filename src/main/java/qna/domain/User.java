package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User extends BaseEntity {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private UserName name;

    @Embedded
    private UserPassword password;

    @Embedded
    private UserId userId;

    protected User() {

    }

    private User(Long id, Email email, UserName name, UserPassword password, UserId userId) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.userId = userId;
    }

    public static User of(String email, String name, String password, String userId) {
        return of(null, email, name, password, userId);
    }

    public static User of(Long id, String email, String name, String password, String userId) {
        return new User(id, Email.of(email), UserName.of(name), UserPassword.of(password), UserId.of(userId));
    }

    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public UserName getName() {
        return name;
    }

    public UserPassword getPassword() {
        return password;
    }

    public UserId getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        User user = (User)obj;
        return id.equals(user.id);
    }
}
