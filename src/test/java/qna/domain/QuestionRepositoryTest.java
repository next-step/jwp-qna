package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {
	private final Question titleAQuestion = new Question("titleA", "contents1");
	private final Question titleBQuestion = new Question("titleB", "contents2");
	private final Question titleCQuestion = new Question("titleC", "contents3");

	@Autowired
	QuestionRepository questionRepository;

	@Test
	@DisplayName("Question 생성")
	void save() {
		Question actual = questionRepository.save(titleAQuestion);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getTitle()).isEqualTo(titleAQuestion.getTitle()),
			() -> assertThat(actual.getContents()).isEqualTo(titleAQuestion.getContents())
		);
	}

	@Test
	@DisplayName("Question 삭제")
	void delete() {
		Question expected = questionRepository.save(titleAQuestion);
		questionRepository.delete(expected);

		Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(expected.getId());

		assertThat(actual.isPresent()).isFalse();
	}

	@Test
	@DisplayName("삭제되지 않은 Question 조회")
	void findByDeletedFalse() {
		Question actual1 = questionRepository.save(titleAQuestion);
		Question actual2 = questionRepository.save(titleBQuestion);
		Question actual3 = questionRepository.save(titleCQuestion);

		questionRepository.delete(actual3);

		List<Question> actual = questionRepository.findByDeletedFalse();

		assertThat(actual)
			.contains(actual1, actual2)
			.doesNotContain(actual3);
	}
}
