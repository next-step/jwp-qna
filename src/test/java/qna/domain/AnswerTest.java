package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
public class AnswerTest {
	public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
	public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

	private final AnswerRepository answerRepository;

	@Autowired
	public AnswerTest(AnswerRepository answerRepository) {
		this.answerRepository = answerRepository;
	}

	@Test
	void save_test() {
		Answer actual = answerRepository.save(A1);
		assertThat(actual.getId()).isNotNull();
	}

	@Test
	void findByContents_test() {
		answerRepository.save(A1);
		String expected = "Answers Contents1";
		String actual = answerRepository.findAllByContents(expected)
				.get(0)
				.getContents();
		assertThat(actual).isEqualTo(expected);
	}
}
