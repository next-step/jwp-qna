package qna.domain;

import javax.persistence.Column;
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

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "password", nullable = false, length = 20)
    private String password;

    @Column(name = "user_id", unique = true, nullable = false, length = 20)
    private String userId;

    protected User() {

    }

    private User(Long id, String email, String name, String password, String userId) {
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
        return new User(id, email, name, password, userId);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
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
