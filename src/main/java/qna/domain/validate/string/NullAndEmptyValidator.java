package qna.domain.validate.string;

public class NullAndEmptyValidator {

    private static final String EMPTY = "";
    private static final NullAndEmptyValidator INSTANCE = new NullAndEmptyValidator();

    private NullAndEmptyValidator() {
    }

    public static NullAndEmptyValidator getInstance() {
        return INSTANCE;
    }

    public void validate(String target) {
        if (target == null || EMPTY.equals(target)) {
            throw new IllegalArgumentException();
        }
    }
}
