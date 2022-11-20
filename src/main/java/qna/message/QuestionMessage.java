package qna.message;

public enum QuestionMessage {
    ERROR_TITLE_SHOULD_BE_NOT_NULL("제목은 필수입니다."),
    ERROR_CONTENTS_SHOULD_BE_NOT_NULL("내용은 필수입니다."),
    ERROR_ALREADY_IS_DELETED("이미 삭제 된 질문입니다."),
    ERROR_CAN_NOT_DELETE_IF_NOT_OWNER("질문을 삭제할 권한이 없습니다.");

    private final String message;

    QuestionMessage(String message) {
        this.message = message;
    }

    public String message() {
        return this.message;
    }
}
