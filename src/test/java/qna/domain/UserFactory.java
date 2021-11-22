package qna.domain;

import qna.domain.commons.Email;
import qna.domain.commons.Name;
import qna.domain.commons.Password;
import qna.domain.commons.UserId;

public class UserFactory {
  private UserFactory() {}

  static User create(Long id, UserId userId, Password password, Name name, Email email) {
    return new User(id, userId, password, name, email);
  }

  static User create(UserId userId, Password password, Name name, Email email) {
    return new User(userId, password, name, email);
  }
}
