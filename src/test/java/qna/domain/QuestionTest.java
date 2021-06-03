package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
public class QuestionTest {
	public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
	public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

	private final QuestionRepository questionRepository;
	private final UserRepository userRepository;

	@Autowired
	public QuestionTest(QuestionRepository questionRepository, UserRepository userRepository) {
		this.questionRepository = questionRepository;
		this.userRepository = userRepository;
	}

	@BeforeEach
	void setUp() {
		User user = userRepository.save(UserTest.JAVAJIGI);
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
}
