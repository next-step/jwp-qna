package qna.domain.answer;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.answer.AnswerTest.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("Answer Repository 테스트")
class AnswerRepositoryTest {

	@Autowired
	private AnswerRepository answerRepository;

	@BeforeEach
	void setUp() {
		answerRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("답변 저장 테스트")
	void saveTest() {
		Answer answer = answerRepository.save(A1);

		assertAll(
			() -> assertThat(answer).isNotNull(),
			() -> assertThat(answer.getId()).isNotNull(),
			() -> assertThat(answer.getId()).isEqualTo(A1.getId()),
			() -> assertThat(answer.getContents()).isEqualTo(A1.getContents()),
			() -> assertThat(answer.isDeleted()).isEqualTo(A1.isDeleted()),
			() -> assertThat(answer.getQuestion()).isEqualTo(A1.getQuestion()),
			() -> assertThat(answer.getWriter()).isEqualTo(A1.getWriter())
		);
	}

	@Test
	@DisplayName("답변 저장 후 조회 테스트")
	void findByIdTest() {
		Answer answer = answerRepository.save(A1);

		Answer foundAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId())
			.orElseThrow(() -> new IllegalArgumentException("entity is not found"));

		assertAll(
			() -> assertThat(foundAnswer).isNotNull(),
			() -> assertThat(foundAnswer.getId()).isEqualTo(answer.getId()),
			() -> assertThat(foundAnswer.getContents()).isEqualTo(answer.getContents()),
			() -> assertThat(foundAnswer.isDeleted()).isEqualTo(answer.isDeleted()),
			() -> assertThat(foundAnswer.getQuestion()).isEqualTo(answer.getQuestion()),
			() -> assertThat(foundAnswer.getWriter()).isEqualTo(answer.getWriter())
		);
	}

	@Test
	@DisplayName("삭제 여부 true 로 변경 후 답변 조회 테스트")
	void findDeletedTest() {
		// given
		Answer answer = answerRepository.save(A1);
		Long id = answer.getId();

		// when
		answer.setDeleted(true);
		Optional<Answer> byIdAndDeletedFalse = answerRepository.findByIdAndDeletedFalse(id);

		// then
		assertAll(
			() -> assertThat(byIdAndDeletedFalse).isEmpty(),
			() -> assertThat(answer.isDeleted()).isTrue(),
			() -> assertThat(answerRepository.findById(id)).isNotEmpty(),
			() -> assertThat(answerRepository.findByIdAndDeletedFalse(id)).isEmpty()
		);
	}

	@Test
	@DisplayName("답변 저장 후 삭제 테스트")
	void findDeletedAnswerTest() {
		// given
		Answer answer = answerRepository.save(A1);
		Long id = answer.getId();

		// when
		answerRepository.deleteById(id);

		// then
		assertAll(
			() -> assertThat(answerRepository.findById(answer.getId())).isEmpty(),
			() -> assertThat(answerRepository.findByIdAndDeletedFalse(answer.getId())).isEmpty()
		);
	}

}