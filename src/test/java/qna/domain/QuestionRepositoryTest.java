package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {
	private Question expected1;
	private Question expected2;
	private Question expected3;

	@Autowired
	QuestionRepository questionRepository;

	@BeforeEach
	void init() {
		expected1 = new Question("title1", "contents1");
		expected2 = new Question("title2", "contents2");
		expected3 = new Question("title3", "contents3");
	}

	@Test
	@DisplayName("Question 생성")
	void save() {
		Question actual = questionRepository.save(expected1);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getTitle()).isEqualTo(expected1.getTitle()),
			() -> assertThat(actual.getContents()).isEqualTo(expected1.getContents())
		);
	}

	@Test
	@DisplayName("Question 삭제")
	void delete() {
		Question expected = questionRepository.save(expected1);
		questionRepository.delete(expected);

		Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(expected.getId());

		assertThat(actual.isPresent()).isFalse();
	}

	@Test
	@DisplayName("삭제되지 않은 Question 조회")
	void findByDeletedFalse() {
		Question actual1 = questionRepository.save(expected1);
		Question actual2 = questionRepository.save(expected2);
		Question actual3 = questionRepository.save(expected3);

		questionRepository.delete(actual3);

		List<Question> actual = questionRepository.findByDeletedFalse();

		assertThat(actual)
			.contains(actual1, actual2)
			.doesNotContain(actual3);
	}
}
