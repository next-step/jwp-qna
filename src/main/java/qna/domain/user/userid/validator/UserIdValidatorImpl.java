package qna.domain.user.userid.validator;

import java.util.Arrays;
import java.util.List;
import qna.domain.validate.string.LengthValidator;
import qna.domain.validate.string.NullAndEmptyValidator;
import qna.domain.validate.string.StringValidator;

public class UserIdValidatorImpl implements UserIdValidator {

    private static final int LIMIT_LENGTH = 20;
    private List<StringValidator> validators;

    public UserIdValidatorImpl() {
        this.validators = Arrays.asList(new NullAndEmptyValidator(), new LengthValidator(LIMIT_LENGTH));
    }

    @Override
    public void validate(String userId) {
        validators.forEach(validator -> validator.validate(userId));
    }
}
