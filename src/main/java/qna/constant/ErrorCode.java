package qna.constant;

public enum ErrorCode {

    질문_삭제_권한_없음("질문을 삭제할 권한이 없습니다."),
    답변_중_다른_사람이_쓴_답변_있어_삭제_못함("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다."),
    ;

    private final String errorMessage;

    ErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
