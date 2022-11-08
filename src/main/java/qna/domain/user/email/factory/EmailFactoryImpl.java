package qna.domain.user.email.factory;

import qna.domain.user.email.Email;
import qna.domain.validate.string.LengthValidator;
import qna.domain.validate.string.StringValidator;

public class EmailFactoryImpl implements EmailFactory {

    private static final int LIMIT_LENGTH = 50;
    private final StringValidator validator;

    public EmailFactoryImpl() {
        this.validator = new LengthValidator(LIMIT_LENGTH);
    }

    @Override
    public Email create(String email) {
        validator.validate(email);
        return new Email(email);
    }
}
