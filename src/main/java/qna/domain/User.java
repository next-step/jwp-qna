package qna.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Embedded
    private UserId userId;
    
    @Embedded
    private Password password;
    
    @Embedded
    private Name name;
    
    @Embedded
    private Email email;

    protected User() {
    }

    public User(String userId, String password, String name, String email) {
        this(null, userId, password, name, email);
    }

    public User(Long id, String userId, String password, String name, String email) {
        this.id = id;
        this.userId = UserId.of(userId);
        this.password = Password.of(password);
        this.name = Name.of(name);
        this.email = Email.of(email);
    }

    public Long getId() {
        return id;
    }
}
