package qna.domain;

public enum ErrorCode {

	DELETE_QUESTION_FORBIDDEN("질문을 삭제할 권한이 없습니다."),
	DELETE_QUESTION_OTHER_WRITER_ANSWER("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다"),
	TITLE_INIT_EXCEED_MAX_LENGTH("100자 초과하였습니다");

	private final String message;

	ErrorCode(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

}
