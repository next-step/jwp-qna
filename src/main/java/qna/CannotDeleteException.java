package qna;

import org.springframework.util.StringUtils;

public class CannotDeleteException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_MESSAGE = "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.";

    public CannotDeleteException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        if (StringUtils.hasText(super.getMessage())) {
            return super.getMessage();
        }
        return DEFAULT_MESSAGE;
    }
}
