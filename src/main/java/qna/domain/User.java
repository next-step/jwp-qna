package qna.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false, unique = true)
    private String userId;
    @Column(length = 20, nullable = false)
    private String password;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 50)
    private String email;

    protected User() {
    }

    private User(UserBuilder userBuilder) {
        this.id = userBuilder.id;
        this.userId = userBuilder.userId;
        this.password = userBuilder.password;
        this.name = userBuilder.name;
        this.email = userBuilder.email;
    }

    public static UserBuilder builder(String userId, String password, String name) {
        return new UserBuilder(userId, password, name);
    }

    public static class UserBuilder {
        private Long id;
        private final String userId;
        private final String password;
        private final String name;
        private String email;

        private UserBuilder(String userId, String password, String name) {
            this.userId = userId;
            this.password = password;
            this.name = name;
        }

        public UserBuilder id(long id) {
            this.id = id;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
