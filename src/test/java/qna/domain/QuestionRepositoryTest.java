package qna.domain;

import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.QuestionTest.*;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {

	@Autowired
	private QuestionRepository questionRepository;

	private List<Question> questions;

	@BeforeEach
	void setUp() {
		questions = questionRepository.saveAll(Arrays.asList(Q1, Q2));
	}

	@Test
	@DisplayName("두건의 데이터가 저장되었는지 검증 ")
	void save() {
		// then
		Assertions.assertThat(questions.size()).isEqualTo(2);
	}

	@Test
	@DisplayName("전체 데이터수 - (삭제여부 true) = 전체 건수 검증")
	void given_questions_when_changeDeleteToTrue_then_excludeDeleteIsTrue() {

		// when
		Question question = questions.get(0);
		question.setDeleted(true);
		List<Question> expectedQuestions = questionRepository.findByDeletedFalse();

		// then
		assertEquals(expectedQuestions.size(), 1);

	}
}
