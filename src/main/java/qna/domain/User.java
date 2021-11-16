package qna.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String email;

    @Column(length = 20, nullable = false)
    private String name;

    @Embedded
    private Account account;

    protected User() {
    }

    public User(final String userId, final String password, final String name, final String email) {
        this(null, userId, password, name, email);
    }

    public User(final Long id, final String userId, final String password, final String name, final String email) {
        this.id = id;
        this.account = new Account(userId, password);
        this.name = name;
        this.email = email;
    }

    public boolean equalsAccount(final User target) {
        if (Objects.isNull(target)) {
            return false;
        }
        return account.equals(target.getAccount());
    }

    public boolean equalsNameAndEmail(final User target) {
        if (Objects.isNull(target)) {
            return false;
        }

        return name.equals(target.name)
            && email.equals(target.email);
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

    public Account getAccount() {
        return account;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", account='" + account + '\'' +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            '}';
    }
}
