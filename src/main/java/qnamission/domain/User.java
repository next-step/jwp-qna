package qnamission.domain;

import javax.persistence.*;

@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String email;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String password;

    @Column(length = 20, nullable = false, unique = true)
    private String userId;

    protected User() {

    }

    protected User(String name, String password, String userId) {
        this.name = name;
        this.password = password;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

}
