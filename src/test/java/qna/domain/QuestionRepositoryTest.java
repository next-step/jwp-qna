package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {

	@Autowired
	private QuestionRepository questionRepository;

	@Test
	void save() {
		final Question expected = new Question("title", "contents");
		final Question actual = questionRepository.save(expected);
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
			() -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
			() -> assertThat(actual.getWriterId()).isEqualTo(expected.getWriterId()),
			() -> assertThat(actual.isDeleted()).isEqualTo(expected.isDeleted())
		);
		assertThat(actual).isEqualTo(actual);
	}

	@Test
	void findByDeletedFalse() {
		final Question question = questionRepository.save(new Question("title", "contents"));
		assertThat(question.isDeleted()).isFalse();
		final List<Question> questions = questionRepository.findByDeletedFalse();
		assertThat(questions).containsExactly(question);
		questions.forEach(q -> assertThat(q.isDeleted()).isFalse());
	}

	@Test
	void findByIdAndDeletedFalse() {
		final Question expected = questionRepository.save(new Question("title", "contents"));
		assertThat(expected.isDeleted()).isFalse();
		final Optional<Question> maybeActual = questionRepository.findByIdAndDeletedFalse(expected.getId());
		assertThat(maybeActual.isPresent()).isTrue();
		final Question actual = maybeActual.get();
		assertThat(actual).isEqualTo(expected);
		assertThat(actual.isDeleted()).isFalse();
	}
}
