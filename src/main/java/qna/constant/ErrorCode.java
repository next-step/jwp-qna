package qna.constant;

public enum ErrorCode {

    질문_삭제_권한_없음("[ERROR] 질문을 삭제할 권한이 없습니다."),
    답변_중_다른_사람이_쓴_답변_있어_삭제_못함("[ERROR] 다른 사람이 쓴 답변이 있어 삭제할 수 없습니다."),
    질문이_존재하지_않음("[ERROR] 질문이 존재하지 않습니다."),
    삭제된_질문에는_답변할_수_없음("[ERROR] 해당 질문은 삭제된 질문이므로, 답변할 수 없습니다."),
    이메일의_길이가_너무_김("[ERROR] 이메일의 길이 제한을 초과하였습니다.(최대 50자)"),
    이름의_길이가_너무_김("[ERROR] 이름의 길이 제한을 초과하였습니다. (최대 20자)"),
    비밀번호의_길이가_너무_김("[ERROR] 비밀번호의 길이 제한을 초과하였습니다. (최대 20자)"),
    제목의_길이가_너무_김("[ERROR] 제목의 길이 제한을 초과하였습니다. (최대 100자)"),
    ;

    private final String errorMessage;

    ErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
