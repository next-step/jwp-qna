package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerRepositoryTest {
	public static final Answer A1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
	public static final Answer A2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
	@Autowired
	private AnswerRepository answerRepository;

	@Test
	public void save () {
		Answer javajigi = answerRepository.save(A1);

		assertThat(javajigi.id()).isNotNull();
	}

	@Test
	public void findByIdAndDeletedFalse () {
		Answer javajigi = answerRepository.save(A1);

		Optional<Answer> find = answerRepository.findByIdAndDeletedFalse(javajigi.id());

		assertThat(find).isNotEmpty();
	}
}
