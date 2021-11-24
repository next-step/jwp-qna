package qna.domain;

import qna.UnAuthorizedException;
import qna.domain.commons.Email;
import qna.domain.commons.Name;
import qna.domain.commons.Password;
import qna.domain.commons.UserId;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class User {
  public static final GuestUser GUEST_USER = new GuestUser();

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

  @Embedded
  @AttributeOverride( name = "writer", column = @Column(name = "writer_id"))
  private final Answers answers = new Answers();

  @Embedded
  private final Questions questions = new Questions();

  @Embedded
  private final DeleteHistories deleteHistories = new DeleteHistories();

  protected User() {}

  public User(UserId userId, Password password, Name name, Email email) {
    this(null, userId, password, name, email);
  }

  public User(Long id, UserId userId, Password password, Name name, Email email) {
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

  private boolean matchUserId(UserId userId) {
    return this.userId.equals(userId);
  }

  public boolean matchPassword(Password targetPassword) {
    return this.password.equals(targetPassword);
  }

  public boolean equalsNameAndEmail(User target) {
    if (Objects.isNull(target)) {
      return false;
    }

    return name.equals(target.name) &&
      email.equals(target.email);
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

  public UserId getUserId() {
    return userId;
  }

  public void setUserId(UserId userId) {
    this.userId = userId;
  }

  public Password getPassword() {
    return password;
  }

  public void setPassword(Password password) {
    this.password = password;
  }

  public Name getName() {
    return name;
  }

  public void setName(Name name) {
    this.name = name;
  }

  public Email getEmail() {
    return email;
  }

  public void setEmail(Email email) {
    this.email = email;
  }

  public void addAnswer(Answer answer) {
    answers.add(answer);
  }

  public Answers getAnswers() {
    return answers;
  }

  public void addQuestion(Question question) {
    questions.add(question);
    question.writeBy(this);
  }

  public Questions getQuestions() {
    return questions;
  }

  public void addDeleteHistory(DeleteHistory deleteHistory) {
    deleteHistory.toDeletedBy(this);
    deleteHistories.add(deleteHistory);
  }

  public void addDeleteHistories(DeleteHistories deleteHistories) {
    this.deleteHistories.addAll(deleteHistories);
  }

  public DeleteHistories getDeleteHistories() {
    return deleteHistories;
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
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userId, password, name, email);
  }

  private static class GuestUser extends User {
    @Override
    public boolean isGuestUser() {
      return true;
    }
  }
}
