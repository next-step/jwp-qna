package qna.domain.user.name.factory;

import java.util.Arrays;
import java.util.List;
import qna.domain.user.name.Name;
import qna.domain.validate.string.LengthValidator;
import qna.domain.validate.string.NullAndEmptyValidator;
import qna.domain.validate.string.StringValidator;

public class NameFactoryImpl implements NameFactory {

    private static final int LIMIT_LENGTH = 20;
    private final List<StringValidator> validators;

    public NameFactoryImpl() {
        this.validators = Arrays.asList(new NullAndEmptyValidator(), new LengthValidator(LIMIT_LENGTH));
    }

    @Override
    public Name create(String name) {
        validators.forEach(validator -> validator.validate(name));
        return new Name(name);
    }
}
