package qna.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.exception.CannotDeleteException;

public class QuestionTest {
	public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
	public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

	private static User user;

	@BeforeAll
	static void setup() {
		user = new User(1L, "eastStar1129", "password", "JangDongGyu", "eaststar1129@abcd.com");
	}

	@Test
	@DisplayName("아이디 값이 존재하지않는 생성 테스트")
	void create_none_id_test() {
		Question q3 = new Question("title3", "contents3").writeBy(user);
		assertAll(() -> assertNull(q3.getId()), 
				() -> assertEquals(q3.getTitle(), "title3"),
				() -> assertEquals(q3.getContents(), "contents3"), 
				() -> assertTrue(q3.isOwner(user)));
	}

	@Test
	@DisplayName("생성 테스트")
	void create_test() {
		Question q3 = new Question(1L, "title3", "contents3").writeBy(user);
		assertAll(() -> assertEquals(q3.getId(), 1L), 
				() -> assertEquals(q3.getTitle(), "title3"),
				() -> assertEquals(q3.getContents(), "contents3"), 
				() -> assertTrue(q3.isOwner(user)));
	}

	@Test
	@DisplayName("답변 생성 테스트")
	void add_answer_test() {
		Question q3 = new Question(1L, "title3", "contents3").writeBy(user);
		Answer answer1 = new Answer(user, q3, "Answers Contents1");
		Answer answer2 = new Answer(user, q3, "Answers Contents2");
		q3.addAnswer(answer1);
		q3.addAnswer(answer2);
		assertEquals(q3.answers().size(), 2);
	}

	@Test
	@DisplayName("제거 테스트")
	void delete_answer_test() throws CannotDeleteException {
		Question q3 = new Question(1L, "title3", "contents3").writeBy(user);
		Answer answer1 = new Answer(user, q3, "Answers Contents1");
		Answer answer2 = new Answer(user, q3, "Answers Contents2");
		q3.addAnswer(answer1);
		q3.addAnswer(answer2);

		List<DeleteHistory> deleteHistories = q3.delete(user);
		assertAll(() -> assertTrue(answer1.isDeleted()), () -> assertTrue(answer2.isDeleted()),
				() -> assertTrue(q3.isDeleted()), () -> assertEquals(deleteHistories.size(), 3));
	}
}
