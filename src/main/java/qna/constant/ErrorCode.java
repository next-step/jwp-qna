package qna.constant;

public enum ErrorCode {
    질문_삭제_권한("[ERROR] 질문을 삭제할 권한이 없습니다."),
    질문_삭제_다른사람_답변_존재("[ERROR] 다른 사람이 쓴 답변이 있어 삭제할 수 없습니다."),
    제목_길이_초과("[ERROR] 제목 길이 제한을 초과하였습니다.(한글: 최대:50자)"),
    제목_공백("[ERROR] 제목은 공백이 될 수 없습니다."),
    이메일_공백("[ERROR] 메일주소는 공백이 될 수 없습니다."),
    이름_공백("[ERROR] 이름은 공백이 될 수 없습니다."),
    패스워드_공백("[ERROR] 패스워드는 공백이 될 수 없습니다."),
    패스워드_길이_초과("[ERROR] 패스워드의 길이 제한을 초과하였습니다. (최대: 20)"),
    사용자_수정_권한_예외("[ERROR] 사용자 수정 권한이 부적합 합니다..");

    private final String errorMessage;

    ErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}

