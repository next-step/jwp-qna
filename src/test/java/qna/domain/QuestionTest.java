package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QuestionTest {

	public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
	public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

	@Test
	@DisplayName("Question 생성")
	void create() {
		Question question = new Question("title", "content").writeBy(UserTest.JAVAJIGI);
		assertThat(question).isNotNull();
		assertThat(question.getTitle()).isEqualTo("title");
		assertThat(question.getContents()).isEqualTo("content");
	}
}
