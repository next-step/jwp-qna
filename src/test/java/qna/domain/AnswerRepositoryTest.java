package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnswerRepositoryTest {
	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Test
	void save() {
		User user = userRepository.save(new User(1L, "javajigi", "password", "name", "javajigi@slipp.net"));
		Question question = questionRepository.save(new Question("title1", "contents1"));
		Answer answer = new Answer(user, question,"Answers Contents1");

		Answer actual = answerRepository.save(answer);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getQuestion()).isEqualTo(answer.getQuestion()),
			() -> assertThat(actual.getWriter()).isEqualTo(answer.getWriter()),
			() -> assertThat(actual.getContents()).isEqualTo(answer.getContents()),
			() -> assertThat(actual.getCreatedAt()).isNotNull()
		);
	}
}
