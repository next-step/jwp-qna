package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static qna.domain.AnswerTest.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AnswerRepositoryTest {
	
	@Autowired
	AnswerRepository answerRepository;

	@Test
	void save() {

		// when
		Answer save = answerRepository.save(A1);

		// then
		assertThat(save.getWriterId()).isEqualTo(A1.getWriterId());
		assertThat(save.getQuestionId()).isEqualTo(A1.getQuestionId());
		assertThat(save.getContents()).isEqualTo(A1.getContents());
	}

	@Test
	void findById() {
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
	void findByIdAndDeletedFalse() {
		//given
		Answer answer = answerRepository.save(A1);

		// when
		Answer byIdAndDeletedFalse = answerRepository.findByIdAndDeletedFalse(answer.getId()).get();

		// then
		assertThat(byIdAndDeletedFalse.getId()).isEqualTo(A1.getId());

	}
}
