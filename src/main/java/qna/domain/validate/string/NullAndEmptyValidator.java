package qna.domain.validate.string;

public class NullAndEmptyValidator implements StringValidator {

    private static final String EMPTY = "";

    @Override
    public void validate(String target) {
        if (target == null || EMPTY.equals(target)) {
            throw new IllegalArgumentException();
        }
    }
}
