package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
public class QuestionTest {
	public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
	public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

	private final QuestionRepository questionRepository;
	private final UserRepository userRepository;
	private final AnswerRepository answerRepository;

	private User user;

	@Autowired
	public QuestionTest(QuestionRepository questionRepository, UserRepository userRepository, AnswerRepository answerRepository) {
		this.questionRepository = questionRepository;
		this.userRepository = userRepository;
		this.answerRepository = answerRepository;
	}

	@BeforeEach
	void setUp() {
		user = userRepository.save(UserTest.JAVAJIGI);
		Question question = questionRepository.save(new Question("title1", "contents1"));
		question.writeBy(user);
	}

	@Test
	void save_test() {
		Question actual = questionRepository.findAll().get(0);
		assertThat(actual.getId()).isNotNull();
	}

	@Test
	void findByTitle_test() {
		Question actual = questionRepository.findFirstByTitle("title1").get();
		assertThat(actual.getTitle()).isEqualTo("title1");
	}

	@Test
	void getWriter_test() {
		Question question = questionRepository.findAll().get(0);
		User actual = question.getWriter();
		assertThat(actual.getUserId()).isEqualTo("javajigi");
	}

	@Test
	@Transactional
	void getAnswer_test() {
		String contents = "Answers Contents1";
		Question question = questionRepository.findAll().get(0);
		Answer answer = answerRepository.save(new Answer(user, question, contents));
		question.addAnswer(answer);

		List<Answer> actual = question.getAnswers();
		assertThat(actual.get(0).getContents()).isEqualTo(contents);
	}
}
