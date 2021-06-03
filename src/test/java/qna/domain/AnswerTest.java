package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
public class AnswerTest {
	private Answer A1;
	private Answer A2;

	private final AnswerRepository answerRepository;

	@Autowired
	public AnswerTest(AnswerRepository answerRepository) {
		this.answerRepository = answerRepository;
	}

	@BeforeEach
	void setUp() {
		A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
		A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
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
