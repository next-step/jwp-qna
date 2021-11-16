package qna;

public class TypeNotFoundException extends RuntimeException {
    public TypeNotFoundException() {
        super("contentType은 필수 입력입니다");
    }
}
