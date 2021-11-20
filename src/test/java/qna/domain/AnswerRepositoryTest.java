package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {
	private User user;
	private Question question;

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private UserRepository userRepository;


	@BeforeEach
	void setup() {
		user = userRepository.save(new User(1L, "user1", "password", "name", "user1@slipp.net"));
		question = questionRepository.save(new Question("title1", "contents1").writeBy(user));
	}

	@Test
	@DisplayName("Answer Entity Create 및 ID 생성 테스트")
	void save() {
		final Answer answer = answerRepository.save(new Answer(user, question, "Answers Contents1"));
		assertThat(answer.getId()).isNotNull();
	}

	@Test
	@DisplayName("Answer Entity Read 테스트")
	void findById() {
		final Answer saved = answerRepository.save(new Answer(user, question, "Answers Contents1"));
		final Answer found = answerRepository.findById(saved.getId()).orElseGet(()->null);
		assertThat(found).isEqualTo(saved);
	}

	@Test
	@DisplayName("Answer Entity Update 테스트")
	void update() {
		final Answer saved = answerRepository.save(new Answer(user, question, "Answers Contents1"));
		saved.setContents("updated!");
		final Answer found = answerRepository.findById(saved.getId()).orElseThrow(() -> new RuntimeException("테스트실패"));
		assertThat(found.getContents()).isEqualTo("updated!");
	}

	@Test
	@DisplayName("Answer Entity Delete 테스트")
	void delete() {
		final Answer saved = answerRepository.save(new Answer(user, question, "Answers Contents1"));
		answerRepository.delete(saved);
		answerRepository.flush();
		final Answer found = answerRepository.findById(saved.getId()).orElseGet(() -> null);
		assertThat(found).isNull();
	}

	@Test
	@DisplayName("Question Entity를 가지고 있는 Answer Entity Save 테스트")
	void saveWithQuestion() {
		final Answer saved = answerRepository.save(new Answer(user, question, "Answers Contents1"));
		final Answer found = answerRepository.findById(saved.getId()).orElseThrow(() -> new RuntimeException("테스트실패"));
		assertThat(found.getQuestion()).isEqualTo(saved.getQuestion());
	}
}
