package qna.domain.deletehistory;

import java.time.LocalDateTime;

import qna.domain.ContentType;
import qna.domain.generator.UserGenerator;

class DeleteHistoryTest {
	public static final DeleteHistory D1 =
		new DeleteHistory(ContentType.QUESTION, 1L, UserGenerator.questionWriter(), LocalDateTime.now());
	public static final DeleteHistory D2 =
		new DeleteHistory(ContentType.ANSWER, 2L, UserGenerator.questionWriter(), LocalDateTime.now());


}