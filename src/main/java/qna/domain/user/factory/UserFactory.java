package qna.domain.user.factory;

import qna.domain.user.User;

public interface UserFactory {

    User create(Long id, String userId, String password, String name, String email);
}
