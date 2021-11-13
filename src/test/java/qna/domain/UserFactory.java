package qna.domain;

public class UserFactory {
  private UserFactory() {}

  static User create(String userId, String password, String name, String email) {
    return new User(userId, password, name, email);
  }
}
