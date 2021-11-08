package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {

	@Autowired
	private QuestionRepository questionRepository;

	@AfterEach
	public void tearDown() {
		questionRepository.deleteAll();
	}

	@Test
	@DisplayName("질문 1 등록 성공")
	public void saveQuestionSuccess() {
		Question q1 = QuestionTest.Q1;
		Question save = questionRepository.save(q1);

		assertAll(() -> {
			assertThat(save.getWriterId()).isEqualTo(q1.getWriterId());
			assertThat(save.getTitle()).isEqualTo(q1.getTitle());
		});
	}

	@Test
	@DisplayName("질문 찾기 성공")
	public void findQuestionByIdSuccess() {
		questionRepository.save(QuestionTest.Q1);
		Optional<Question> optionalQuestion = questionRepository.findByIdAndDeletedFalse(QuestionTest.Q1.getId());

		assertAll(() -> {
			assertThat(optionalQuestion.isPresent()).isTrue();
			Question question = optionalQuestion.get();
			assertThat(question.getTitle()).isEqualTo(QuestionTest.Q1.getTitle());
		});

	}

	@Test
	@DisplayName("질문 삭제 플래그 true 변경 성공")
	public void updateDeletedSuccess() {
		Question save = questionRepository.save(QuestionTest.Q1);

		save.setDeleted(true);
		Question deleted = questionRepository.save(save);

		assertAll(() -> {
			assertThat(deleted.getId()).isEqualTo(save.getId());
			assertThat(deleted.isDeleted()).isTrue();
		});
	}

}