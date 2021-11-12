package qna.domain;

import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.QuestionTest.*;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.ErrorMessage;
import qna.UnAuthorizedException;

@DataJpaTest
public class QuestionRepositoryTest {

	@Autowired
	private QuestionRepository questionRepository;

	@Test
	@DisplayName("두건의 데이터가 저장되었는지 검증 ")
	void save() {

		// when
		List<Question> questions = questionRepository.saveAll(Arrays.asList(Q1, Q2));

		List<Question> questionAll = questionRepository.findAll();

		// then
		assertAll(
			() -> assertEquals(questions.get(0), Q1),
			() -> assertEquals(questions.get(1), Q2),
			() -> assertEquals(questionAll.size(), 2)
		);
	}

	@Test
	@DisplayName("전체 데이터수 - (삭제여부 true) = 전체 건수 검증")
	void given_questions_when_changeDeleteToTrue_then_excludeDeleteIsTrue () {
		List<Question> questions = questionRepository.saveAll(Arrays.asList(Q1, Q2));

		// when
		Question question = questions.get(0);
		question.setDeleted(true);
		List<Question> byDeletedFalse = questionRepository.findByDeletedFalse();

		// then
		assertEquals(byDeletedFalse.size(), 1);
	}
}
