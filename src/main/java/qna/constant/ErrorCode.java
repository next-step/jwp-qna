package qna.constant;

public enum ErrorCode {

    질문_삭제_권한_없음("[ERROR] 질문을 삭제할 권한이 없습니다."),
    답변_중_다른_사람이_쓴_답변_있어_삭제_못함("[ERROR] 다른 사람이 쓴 답변이 있어 삭제할 수 없습니다."),
    질문이_존재하지_않음("[ERROR] 질문이 존재하지 않습니다."),
    삭제된_질문에는_답변할_수_없음("[ERROR] 해당 질문은 삭제된 질문이므로, 답변할 수 없습니다."),
    ;

    private final String errorMessage;

    ErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
