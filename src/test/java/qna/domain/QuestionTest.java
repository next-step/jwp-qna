package qna.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QuestionTest {
	public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
	public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

	@Test
	@DisplayName("아이디 값이 존재하지않는 생성 테스트")
	void create_none_id_test() {
		assertAll(() -> assertNull(Q1.getId()), 
				() -> assertEquals(Q1.getTitle(), "title1"),
				() -> assertEquals(Q1.getContents(), "contents1"), 
				() -> assertTrue(Q1.isOwner(UserTest.JAVAJIGI)));
	}

	@Test
	@DisplayName("생성 테스트")
	void create_test() {
		Question q3 = new Question(1L, "title3", "contents3").writeBy(UserTest.JAVAJIGI);
		assertAll(() -> assertEquals(q3.getId(), 1L), 
				() -> assertEquals(q3.getTitle(), "title3"),
				() -> assertEquals(q3.getContents(), "contents3"), 
				() -> assertTrue(q3.isOwner(UserTest.JAVAJIGI)));
	}
}
