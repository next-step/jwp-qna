package qna.domain.user.factory;

import qna.domain.user.User;

public interface UserFactory {

    User create(String userId, String password, String name, String email);
}
