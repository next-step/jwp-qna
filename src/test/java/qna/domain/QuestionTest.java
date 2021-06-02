package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
public class QuestionTest {
	public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
	public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

	private final QuestionRepository questionRepository;

	@Autowired
	public QuestionTest(QuestionRepository questionRepository) {
		this.questionRepository = questionRepository;
	}

	@Test
	void save_test() {
		Question actual = questionRepository.save(Q1);
		assertThat(actual.getId()).isNotNull();
	}

	@Test
	void findByTitle_test() {
		questionRepository.save(Q1);
		Question actual = questionRepository.findFirstByTitle("title1").get();
		assertThat(actual.getTitle()).isEqualTo("title1");
	}
}
