package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
public class AnswerTest {
	private final AnswerRepository answerRepository;
	private final UserRepository userRepository;
	private final QuestionRepository questionRepository;

	private User user;
	private Question question;

	@Autowired
	public AnswerTest(AnswerRepository answerRepository, UserRepository userRepository, QuestionRepository questionRepository) {
		this.answerRepository = answerRepository;
		this.userRepository = userRepository;
		this.questionRepository = questionRepository;
	}

	@BeforeEach
	void setUp() {
		user = userRepository.save(UserTest.JAVAJIGI);
		answerRepository.save(new Answer("Answers Contents1"));
		question = questionRepository.save(new Question("title1", "contents1"));
	}

	@Test
	void save_test() {
		Answer actual = answerRepository.findAll().get(0);
		assertThat(actual.getId()).isNotNull();
	}

	@Test
	void findByContents_test() {
		String expected = "Answers Contents1";
		String actual = answerRepository.findAllByContents(expected)
				.get(0)
				.getContents();
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void getWriter_and_writeBy_test() {
		Answer answer = answerRepository.findAllByContents("Answers Contents1").get(0);

		User actual = answer.getWriter();
		assertThat(actual).isNull();

		answer.writeBy(user);
		actual = answer.getWriter();
		assertThat(actual.getUserId()).isEqualTo("javajigi");
	}

	@Test
	void getQuestion_and_toQuestion_test() {
		Answer answer = answerRepository.findAllByContents("Answers Contents1").get(0);

		Question actual = answer.getQuestion();
		assertThat(actual).isNull();

		answer.toQuestion(question);
		actual = answer.getQuestion();
		assertThat(actual.getTitle()).isEqualTo("title1");
	}
}
