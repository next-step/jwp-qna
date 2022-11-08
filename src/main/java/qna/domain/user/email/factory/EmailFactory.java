package qna.domain.user.email.factory;

import qna.domain.user.email.Email;

public interface EmailFactory {

    Email create(String email);
}
