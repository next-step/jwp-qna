package qna.domain.question.title.factory;

import java.util.Arrays;
import java.util.List;
import qna.domain.question.title.Title;
import qna.domain.validate.string.LengthValidator;
import qna.domain.validate.string.NullAndEmptyValidator;
import qna.domain.validate.string.StringValidator;

public class TitleFactoryImpl implements TitleFactory {

    private static final int LIMIT_LENGTH = 100;
    private final List<StringValidator> validators;

    public TitleFactoryImpl() {
        this.validators = Arrays.asList(new NullAndEmptyValidator(), new LengthValidator(LIMIT_LENGTH));
    }

    @Override
    public Title create(String title) {
        validators.forEach(validator -> validator.validate(title));
        return new Title(title);
    }
}
