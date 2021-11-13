package qna.domain;

import qna.NotFoundException;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

import static qna.utils.ValidationUtils.isEmpty;

@Entity
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", length = 20, unique = true, nullable = false)
    private String userId;

    @Column(name = "password", length = 20, nullable = false)
    private String password;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "email", length = 50)
    private String email;

    @OneToMany(mappedBy = "writer")
    private List<Answer> answers;

    @OneToMany(mappedBy = "deletedByUser")
    private List<DeleteHistory> deleteHistories;

    @OneToMany(mappedBy = "writer")
    private List<Question> questions;

    public User(String userId, String password, String name, String email) {
        validation(userId, password);
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    protected User() {
    }

    private void validation(String userId, String password) {
        if (isEmpty(userId)) {
            throw new NotFoundException("아이디 입력은 필수입니다.");
        }
        if (isEmpty(password)) {
            throw new NotFoundException("패스워드 입력은 필수입니다.");
        }
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id)
                && Objects.equals(userId, user.userId)
                && Objects.equals(password, user.password)
                && Objects.equals(name, user.name)
                && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, password, name, email);
    }

}
