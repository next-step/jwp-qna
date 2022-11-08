package qna.domain.user.password.factory;

import java.util.Arrays;
import java.util.List;
import qna.domain.user.password.Password;
import qna.domain.validate.string.LengthValidator;
import qna.domain.validate.string.NullAndEmptyValidator;
import qna.domain.validate.string.StringValidator;

public class PasswordFactoryImpl implements PasswordFactory {

    private static final int LIMIT_LENGTH = 20;
    private final List<StringValidator> validators;

    public PasswordFactoryImpl() {
        this.validators = Arrays.asList(new NullAndEmptyValidator(), new LengthValidator(LIMIT_LENGTH));
    }

    @Override
    public Password create(String password) {
        validators.forEach(validator -> validator.validate(password));
        return new Password(password);
    }
}
