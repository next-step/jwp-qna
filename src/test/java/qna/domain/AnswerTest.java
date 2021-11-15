package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {

	private Answer a1;
	private Answer a2;
	private User user1;
	private User user2;
	private Question q1;
	private Question q2;

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setUp() {
		user1 = new User("test1", "test1", "test1", "test1");
		user2 = new User("test2", "test2", "test2", "test2");
		q1 = new Question("test", "test").writeBy(user1);
		q2 = new Question("test2", "test2").writeBy(user2);
		a1 = new Answer(user1, q1, "Answers Contents1");
		a2 = new Answer(user2, q2, "Answers Contents2");
	}

	@Test
	void 답변_저장_테스트() {
		// given // when
		Answer answer = answerRepository.save(a1);

		// then
		assertAll(
			() -> assertThat(answer.getCreatedDate()).isEqualTo(a1.getCreatedDate()),
			() -> assertThat(answer.getId()).isEqualTo(a1.getId()),
			() -> assertThat(answer.getQuestion()).isEqualTo(a1.getQuestion())
		);
	}

	@Test
	void 답변_조회_테스트() {
		// given
		Answer expectAnswer = answerRepository.save(a2);

		// when
		Optional<Answer> answer = answerRepository.findById(expectAnswer.getId());

		// then
		assertAll(
			() -> assertThat(answer.isPresent()).isTrue(),
			() -> assertThat(answer.get()).isEqualTo(expectAnswer)
		);
	}

	@Test
	void 답변_수정_테스트() {
		// given
		Answer answer = answerRepository.save(new Answer(user1, q1, "Test1"));

		// when
		answer.setQuestion(q2);
		Answer expectAnswer = answerRepository.save(answer);

		// then
		assertThat(expectAnswer.getQuestion()).isEqualTo(q2);
	}

	@Test
	void 답변_삭제_테스트() {
		// given
		User expectUser = userRepository.save(new User("testuser", "test", "test", "test"));
		Answer answer = answerRepository.save(new Answer(expectUser, q2, "Test1"));

		// when
		answerRepository.delete(answer);
		Optional<Answer> expectAnswer = answerRepository.findById(expectUser.getId());

		// then
		assertThat(expectAnswer.isPresent()).isFalse();
	}
}