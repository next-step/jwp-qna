package qna;

public class ExceedLengthExcpetion extends IllegalArgumentException{
    public static final String ERROR_EXCEEDED_MAX_LENGTH = "크기를 초과했습니다.";

    public ExceedLengthExcpetion(int maxLength, int length) {
        super(ERROR_EXCEEDED_MAX_LENGTH + "기준길이 : " + maxLength + ", 입력길이 : " + length);
    }
}
