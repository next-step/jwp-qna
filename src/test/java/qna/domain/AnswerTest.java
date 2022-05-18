package qna.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.NotFoundException;
import qna.UnAuthorizedException;

public class AnswerTest {
	public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
	public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

	@Test
	@DisplayName("생성 실패 테스트")
	void create_throws_test() {
		assertAll(
				() -> assertThrows(UnAuthorizedException.class,
						() -> new Answer(null, QuestionTest.Q1, "Answers Contents1")),
				() -> assertThrows(NotFoundException.class,
						() -> new Answer(UserTest.JAVAJIGI, null, "Answers Contents1")));
	}

	@Test
	@DisplayName("아이디값이 존재하지 않는 생성 테스트")
	void create_none_id_test() {
		assertAll(() -> assertNull(A1.getId()), 
				() -> assertTrue(A1.isOwner(UserTest.JAVAJIGI)),
				() -> assertEquals(A1.getQuestionId(), QuestionTest.Q1.getId()),
				() -> assertEquals(A1.getContents(), "Answers Contents1"), 
				() -> assertNull(A2.getId()),
				() -> assertTrue(A2.isOwner(UserTest.SANJIGI)),
				() -> assertEquals(A2.getQuestionId(), QuestionTest.Q1.getId()),
				() -> assertEquals(A2.getContents(), "Answers Contents2"));
	}

	@Test
	@DisplayName("아이디값이 존재하는 생성 테스트")
	void create_id_test() {
		Answer a3 = new Answer(1L, UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents3");
		assertAll(() -> assertEquals(a3.getId(), 1L), 
				() -> assertTrue(a3.isOwner(UserTest.SANJIGI)),
				() -> assertEquals(a3.getQuestionId(), QuestionTest.Q1.getId()),
				() -> assertEquals(a3.getContents(), "Answers Contents3"));
	}

	@Test
	@DisplayName("답변달기 테스트")
	void question_add_answer_test() {
		Answer A3 = new Answer(1L, UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents3");
		QuestionTest.Q2.addAnswer(A3);
		assertEquals(A3.getQuestionId(), QuestionTest.Q2.getId());
	}
}
