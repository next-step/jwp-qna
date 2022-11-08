package qna.domain.user.password.factory;

import qna.domain.user.password.Password;

public interface PasswordFactory {

    Password create(String password);
}
