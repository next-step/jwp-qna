package qna;

import qna.domain.User;

public class UnAuthorizedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnAuthorizedException() {
        super();
    }

    public UnAuthorizedException(User writer, User requester) {
        super(String.format(
                "질문을 삭제할 권한이 없습니다. [질문작성자: %s] [삭제요청자: %s]",
                writer.getUserId(), requester.getUserId()));

    }

}
