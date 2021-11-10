package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {

	@Autowired
	private AnswerRepository answerRepository;

	@Test
	void save() {
		final User writer = new User("userId", "password", "name", "e@mail.com");
		final Question question = new Question("title", "contents");
		final Answer expected = new Answer(writer, question, "contents");

		final Answer actual = answerRepository.save(expected);
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getWriterId()).isEqualTo(expected.getWriterId()),
			() -> assertThat(actual.getQuestionId()).isEqualTo(expected.getQuestionId()),
			() -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
			() -> assertThat(actual.isDeleted()).isEqualTo(expected.isDeleted())
		);
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void findByQuestionIdAndDeletedFalse() {
		final User writer = new User("userId", "password", "name", "e@mail.com");
		final Question question = new Question("title", "contents");
		final Answer expected = answerRepository.save(new Answer(writer, question, "contents"));
		assertThat(expected.isDeleted()).isFalse();

		final List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());
		assertThat(answers).containsExactly(expected);
		answers.forEach(answer -> assertAll(
			() -> assertThat(answer.getQuestionId()).isEqualTo(question.getId()),
			() -> assertThat(answer.isDeleted()).isFalse()
		));
	}

	@Test
	void findByIdAndDeletedFalse() {
		final User writer = new User("userId", "password", "name", "e@mail.com");
		final Question question = new Question("title", "contents");
		final Answer expected = answerRepository.save(new Answer(writer, question, "contents"));
		assertThat(expected.isDeleted()).isFalse();

		final Optional<Answer> maybeActual = answerRepository.findByIdAndDeletedFalse(expected.getId());
		assertThat(maybeActual.isPresent()).isTrue();
		final Answer actual = maybeActual.get();
		assertThat(actual).isEqualTo(expected);
		assertThat(actual.isDeleted()).isFalse();
	}
}
