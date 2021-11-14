package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {
	@Autowired
	QuestionRepository questionRepository;

	@Test
	@DisplayName("Question 생성 테스트")
	public void QuestionRepositoryCreateTest() {
		//given
		//when
		Question savedQuestion = questionRepository.save(QuestionTest.Q1);

		//then
		Question findQuestion = questionRepository.findById(savedQuestion.getId()).get();
		assertThat(findQuestion.getContents()).isEqualTo(savedQuestion.getContents());
	}

	@Test
	@DisplayName("Question 동일성 테스트")
	public void QuestionRepositoryEqualsTest() {
		//given
		//when
		Question savedQuestion = questionRepository.save(QuestionTest.Q1);
		Question findQuestion = questionRepository.findById(savedQuestion.getId()).get();

		//then
		assertThat(savedQuestion).isEqualTo(findQuestion);
		assertThat(savedQuestion).isSameAs(findQuestion);
	}

	@Test
	@DisplayName("Question 변경 테스트")
	public void QuestionRepositoryUpdateTest() {
		//given
		Question savedQuestion = questionRepository.save(QuestionTest.Q1);
		//when
		savedQuestion.setContents("변경된 내용입니다.");
		questionRepository.flush();

		//then
		Question findQuestion = questionRepository.findById(savedQuestion.getId()).get();
		assertThat(findQuestion.getContents()).isEqualTo(savedQuestion.getContents());
	}

	@Test
	@DisplayName("Answer 삭제 테스트")
	public void AnswerRepositoryDeleteTest() {
		//given
		Question savedOne = questionRepository.save(QuestionTest.Q1);
		Question savedTwo = questionRepository.save(QuestionTest.Q2);

		//when
		questionRepository.deleteById(savedOne.getId());

		//then
		assertThat(questionRepository.findAll()).hasSize(1);
		assertThat(questionRepository.findAll()).contains(savedTwo);
	}
}