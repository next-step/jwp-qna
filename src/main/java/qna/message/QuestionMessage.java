package qna.message;

public enum QuestionMessage {
    ERROR_TITLE_SHOULD_BE_NOT_NULL("제목은 필수입니다."),
    ERROR_CONTENTS_SHOULD_BE_NOT_NULL("내용은 필수입니다.");

    private final String message;

    QuestionMessage(String message) {
        this.message = message;
    }

    public String message() {
        return this.message;
    }
}
