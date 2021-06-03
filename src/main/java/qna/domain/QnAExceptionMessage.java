package qna.domain;

public enum QnAExceptionMessage {

	NO_AUTH_QUESTION("질문을 삭제할 권한이 없습니다."),
	NO_AUTH_ANSWER("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다."),
	;

	private String message;

	QnAExceptionMessage(final String message) {
		this.message = message;
	}

	public String message() {
		return this.message;
	}
}

