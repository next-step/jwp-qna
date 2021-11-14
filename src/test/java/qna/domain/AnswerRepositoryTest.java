package qna.domain;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnswerRepositoryTest {
	@Autowired
	AnswerRepository answerRepository;


	@Test
	@DisplayName("Answer 생성 테스트")
	public void AnswerRepositoryCreateTest() {
		//given
		//when
		Long savedId = answerRepository.save(AnswerTest.A1).getId();
		Answer answer = answerRepository.findByIdAndDeletedFalse(savedId).get();

		//then
		assertThat(answer.getContents()).isEqualTo(AnswerTest.A1.getContents());
	}

	@Test
	@DisplayName("Answer 동일성 테스트")
	public void AnswerRepositoryEqualsTest() {
		//given
		//when
		Answer savedAnswer = answerRepository.save(AnswerTest.A1);
		Answer findAnswer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId()).get();

		//then
		assertThat(savedAnswer).isEqualTo(findAnswer);
		assertThat(savedAnswer).isSameAs(findAnswer);
	}

	@Test
	@DisplayName("Answer 변경 테스트")
	public void AnswerRepositoryUpdateTest() {
		//given
		Long saved_id = answerRepository.save(AnswerTest.A1).getId();

		//when
		Answer beforeAnswer = answerRepository.findById(saved_id).get();
		beforeAnswer.setContents("변경된 내용1");
		answerRepository.flush();
		Answer findAnswer = answerRepository.findById(saved_id).get();

		//then
		assertThat(findAnswer.getContents()).isEqualTo("변경된 내용1");
	}

	@Test
	@DisplayName("Answer 삭제 테스트")
	public void AnswerRepositoryDeleteTest() {
		//given
		Answer savedOne = answerRepository.save(AnswerTest.A1);
		Answer savedTwo = answerRepository.save(AnswerTest.A2);

		//when
		answerRepository.deleteById(savedOne.getId());

		//then
		assertThat(answerRepository.findAll()).hasSize(1);
		assertThat(answerRepository.findAll()).contains(savedTwo);
	}
}