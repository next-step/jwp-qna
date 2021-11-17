package qna.common.exception;

import qna.domain.ContentType;

public class CannotDeleteException extends BaseException {

    private static final String CONTENT_DELETE_NOT_PERMISSION_EXCEPTION_MESSAGE = "%s을 삭제할 권한이 없습니다.";

    private static final long serialVersionUID = 1L;

    public CannotDeleteException(String message) {
        super(message);
    }

    public CannotDeleteException(ContentType contentType) {
        super(String.format(CONTENT_DELETE_NOT_PERMISSION_EXCEPTION_MESSAGE,
            contentType.getContentTypeName()));
    }
}
