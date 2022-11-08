package qna.domain.validate.string;

public class LengthValidator implements StringValidator {

    private final int limitLength;

    public LengthValidator(int limitLength) {
        this.limitLength = limitLength;
    }

    @Override
    public void validate(String target) {
        if (target.length() > limitLength) {
            throw new IllegalArgumentException();
        }
    }
}
