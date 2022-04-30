package qna.domain;

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

    @Embedded
    private UserInfo userInfo;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Question> question = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Answer> answer = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<DeleteHistory> deleteHistories = new ArrayList<>();

    public User() {
    }

    public User(UserInfo userInfo) {
        this(null, userInfo);
    }

    public User(Long id, UserInfo userInfo) {
        this.id = id;
        this.userInfo = userInfo;
    }

    public void update(User loginUser, User target) {
        userInfo.update(target.userInfo);
    }

    public boolean equalsNameAndEmail(User target) {
        if (Objects.isNull(target)) {
            return false;
        }
        return userInfo.equalsNameAndEmail(target.userInfo);
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

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
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
                ", userInfo=" + userInfo +
                '}';
    }

    private static class GuestUser extends User {
        @Override
        public boolean isGuestUser() {
            return true;
        }
    }
}
