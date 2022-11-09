package qna.domain.validate.string;

public class LengthValidator {

    private static final LengthValidator INSTANCE = new LengthValidator();

    private LengthValidator() {
    }

    public static LengthValidator getInstance() {
        return INSTANCE;
    }

    public void validate(String target, int limitLength) {
        if (target.length() > limitLength) {
            throw new IllegalArgumentException();
        }
    }
}
