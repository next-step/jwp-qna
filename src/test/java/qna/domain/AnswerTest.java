package qna.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.NotFoundException;
import qna.UnAuthorizedException;

public class AnswerTest {
	public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
	public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
	
	private static User user;
	private static Question question;
	
	@BeforeAll
	static void setup() {
		user = new User(1L, "eastStar1129", "password", "JangDongGyu", "eaststar1129@abcd.com");
		question = new Question(2L, "title2", "contents2").writeBy(user);
	}
	
	@Test
	@DisplayName("생성 실패 테스트")
	void create_throws_test() {
		assertAll(
				() -> assertThrows(UnAuthorizedException.class,
						() -> new Answer(null, question, "Answers Contents1")),
				() -> assertThrows(NotFoundException.class,
						() -> new Answer(user, null, "Answers Contents1")));
	}

	@Test
	@DisplayName("아이디값이 존재하지 않는 생성 테스트")
	void create_none_id_test() {
		Answer answer = new Answer(user, question, "Answers Contents");
		assertAll(() -> assertNull(answer.getId()), 
				() -> assertTrue(answer.isOwner(user)),
				() -> assertEquals(answer.getQuestionId(), question.getId()),
				() -> assertEquals(answer.getContents(), "Answers Contents"));
	}

	@Test
	@DisplayName("아이디값이 존재하는 생성 테스트")
	void create_id_test() {
		Answer answer = new Answer(3L, user, question, "Answers Contents");
		assertAll(() -> assertEquals(answer.getId(), 3L), 
				() -> assertTrue(answer.isOwner(user)),
				() -> assertEquals(answer.getQuestionId(), question.getId()),
				() -> assertEquals(answer.getContents(), "Answers Contents"));
	}

	@Test
	@DisplayName("답변달기 테스트")
	void question_add_answer_test() {
		Answer answer = new Answer(user, question, "Answers Contents");
		question.addAnswer(answer);
		assertEquals(answer.getQuestionId(), question.getId());
	}
}
