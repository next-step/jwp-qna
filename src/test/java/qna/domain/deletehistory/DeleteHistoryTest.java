package qna.domain.deletehistory;

import java.time.LocalDateTime;

import qna.domain.ContentType;

class DeleteHistoryTest {
	public static final DeleteHistory D1 =
		new DeleteHistory(ContentType.QUESTION, 1L, 1L, LocalDateTime.now());
	public static final DeleteHistory D2 =
		new DeleteHistory(ContentType.ANSWER, 2L, 1L, LocalDateTime.now());


}