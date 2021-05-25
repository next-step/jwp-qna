package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
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

	@BeforeEach
	void init() {
		answerRepository.save(A2);
	}

	@Test
	@DisplayName(value = "answer entity 가 DB에 저장되면 id 값, 저장 날짜가 할당된다")
	void save() {
		Answer persistedA1 = answerRepository.save(A1);
		assertAll(
			() -> assertThat(persistedA1.getId()).isNotNull(),
			() -> assertThat(persistedA1.getCreatedAt()).isNotNull()
		);
	}

	@Test
	@DisplayName(value = "저장된 entity 를 select 하여 반환한다")
	void select() {
		assertThat(answerRepository.findByIdAndDeletedFalse(A2.getId()).get()).isEqualTo(A2);
	}

}
