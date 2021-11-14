package qna;

public enum Message {

	CAN_NOT_DELETE_QUESTION_WITHOUT_OWNERSHIP("질문을 삭제할 권한이 없습니다."),
	CAN_NOT_DELETE_QUESTION_HAVING_ANSWER_WRITTEN_BY_OTHER("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다."),
	CAN_NOT_DELETE_ANSWER_WITHOUT_OWNERSHIP("답변을 삭제할 권한이 없습니다.");

	private final String content;

	Message(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
}
