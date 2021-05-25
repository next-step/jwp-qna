package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {
	public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
	public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

	@Autowired
	private AnswerRepository answerRepository;

	@Test
	@DisplayName(value = "answer entity 가 DB에 저장되면 id 값, 저장 날짜가 할당된다")
	void dbConnect() {
		Answer persistedA1 = answerRepository.save(A1);
		assertAll(
			() -> assertThat(persistedA1.getId()).isNotNull(),
			() -> assertThat(persistedA1.getCreatedAt()).isNotNull()
		);
	}

}
