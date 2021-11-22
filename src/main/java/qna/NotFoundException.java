package qna;

public class NotFoundException extends RuntimeException {

    public static final String NOT_FOUND_QUESTION = "질문을 찾을 수 없습니다.";

    public NotFoundException() {
        super(NOT_FOUND_QUESTION);
    }
}
