package qna.domain;

import qna.exception.UnAuthorizedException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class User extends AbstractDate {
    public static final GuestUser GUEST_USER = new GuestUser();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Embedded
    private UserLogin userLogin;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Question> question = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Answer> answer = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<DeleteHistory> deleteHistories = new ArrayList<>();

    public User() {
    }

    public User(String name, UserLogin userLogin) {
        this(null, name, userLogin);
    }

    public User(Long id, String name, UserLogin userLogin) {
        this.id = id;
        this.name = name;
        this.userLogin = userLogin;
    }

    public void update(User loginUser, User target) {
        if (!userLogin.equalsUserIdAndPassword(target.userLogin)) {
            throw new UnAuthorizedException("로그인 정보가 맞지 않아 개인 정보를 변경할 수 없습니다.");
        }
        this.name = target.name;
        this.userLogin = target.userLogin;
    }

    public boolean equalsNameAndEmail(User target) {
        if (Objects.isNull(target)) {
            return false;
        }
        return name.equals(target.name)
                && userLogin.getEmail().equals(target.userLogin.getEmail());
    }

    public boolean isGuestUser() {
        return false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public UserLogin getUserLogin() {
        return userLogin;
    }

    public List<Question> getQuestion() {
        return question;
    }

    public List<Answer> getAnswer() {
        return answer;
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }

    public void setQuestion(List<Question> question) {
        this.question = question;
    }

    public void setAnswer(List<Answer> answer) {
        this.answer = answer;
    }

    public void setDeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    public void addQuestion(Question newQuestion) {
        question.add(newQuestion);
    }

    public void addAnswer(Answer newAnswer) {
        answer.add(newAnswer);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userLogin=" + userLogin +
                '}';
    }

    private static class GuestUser extends User {
        @Override
        public boolean isGuestUser() {
            return true;
        }
    }
}
