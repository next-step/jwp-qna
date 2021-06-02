package qna.domain.entity;

import lombok.*;
import qna.domain.entity.common.TraceDate;
import qna.UnAuthorizedException;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * create table user
 * (
 *     id         bigint generated by default as identity,
 *     created_at timestamp   not null,
 *     email      varchar(50),
 *     name       varchar(20) not null,
 *     password   varchar(20) not null,
 *     updated_at timestamp,
 *     user_id    varchar(20) not null,
 *     primary key (id)
 * )
 *
 * alter table user
 *     add constraint UK_a3imlf41l37utmxiquukk8ajc unique (user_id)
 */

@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "unique_user_id", columnNames={"userId"}))
@EqualsAndHashCode(of = "id")
@Getter(AccessLevel.PACKAGE)
@ToString(of = {"id", "userId", "email", "name"})
public class User extends TraceDate {
    public static final GuestUser GUEST_USER = new GuestUser();

    @Getter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String email;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String password;

    @Column(length = 20, nullable = false)
    private String userId;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Answer> answers = new ArrayList<>();

    public boolean isGuestUser() {
        return false;
    }

    @Builder
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

    public boolean matchUserId(String userId) {
        return this.userId.equals(userId);
    }

    public boolean matchPassword(String targetPassword) {
        return this.password.equals(targetPassword);
    }

    public boolean equalsNameAndEmail(User target) {
        if (Objects.isNull(target)) {
            return false;
        }

        return this.name.equals(target.name) && this.email.equals(target.email);
    }

    private static class GuestUser extends User {

        @Override
        public boolean isGuestUser() {
            return true;
        }
    }
}
