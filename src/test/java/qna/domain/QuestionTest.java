package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.UserTest.JAVAJIGI;


public class QuestionTest {
	public static final Question Q1 = new Question(1L, "title1", "contents1").writeBy(JAVAJIGI);
	public static final Question Q2 = new Question(2L, "title2", "contents2").writeBy(UserTest.SANJIGI);

	@Test
	public void 질문_주인인지_확인_GREEN () {
		User maeve = new User(3L, "maeve", "password", "maeve", "maeve.woo@cyworldlabs.com");

		Question question = new Question("title", "머시깽이");

		question.writeBy(maeve);

		assertThat(question.isOwner(maeve)).isTrue();
	}

	@Test
	public void 질문_주인인지_확인_RED () {
		User maeve = new User(3L, "maeve", "password", "maeve", "maeve.woo@cyworldlabs.com");

		Question question = new Question("title", "머시깽이");

		question.writeBy(maeve);

		assertThat(question.isOwner(JAVAJIGI)).isFalse();
	}
}
