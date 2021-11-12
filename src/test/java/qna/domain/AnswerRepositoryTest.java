package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static qna.domain.AnswerTest.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {

	@Autowired
	AnswerRepository answerRepository;

	@Test
	@DisplayName("입력된 정보와 저장된정보가 동일한지 확인")
	void given_saveAnswer_When_save_then_saved_equals_saveAnswer() {

		// when
		Answer expected = answerRepository.save(A1);

		// then
		assertThat(expected.getWriterId()).isEqualTo(A1.getWriterId());
		assertThat(expected.getQuestionId()).isEqualTo(A1.getQuestionId());
		assertThat(expected.getContents()).isEqualTo(A1.getContents());
	}

	@Test
	@DisplayName("Answer 저장한 정보의 id로 조회했을 때 조회한 값이 동일한지 확인")
	void given_saveAnswer_when_findById_saved_equals_saveAnswer() {

		//given
		Answer answer = answerRepository.save(A1);

		// when
		Answer findAnsewer = answerRepository.findById(answer.getId()).get();

		// then
		assertThat(findAnsewer.getWriterId()).isEqualTo(A1.getWriterId());
		assertThat(findAnsewer.getQuestionId()).isEqualTo(A1.getQuestionId());
		assertThat(findAnsewer.getContents()).isEqualTo(A1.getContents());
	}

	@Test
	@DisplayName("저장된 정보가 false 기본값으로 저장되었는지 확인")
	void given_saveAnswer_when_findByIdAndDeletedFalse_then_isFalse() {

		//given
		Answer answer = answerRepository.save(A1);

		// when
		Answer expected = answerRepository.findByIdAndDeletedFalse(answer.getId()).get();

		// then
		assertThat(expected.isDeleted()).isFalse();
	}

}
