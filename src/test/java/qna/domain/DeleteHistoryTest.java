package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DeleteHistoryTest {
	public User JAVAJIGI;
	private Question Q1;
	private Answer A1;
	public DeleteHistory HISTORY;

	@Autowired
	private DeleteHistoryRepository deleteHistoryRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@BeforeEach
	void setUp() {
		JAVAJIGI = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
		Q1 = questionRepository.save(new Question("title1", "contents1").writeBy(JAVAJIGI));
		A1 = answerRepository.save(new Answer(JAVAJIGI, Q1, "Answers Contents1"));
		HISTORY = DeleteHistory.ofAnswer(A1.getId(),
			JAVAJIGI);
	}

	@Test
	@DisplayName("삭제 이력 남기기")
	void save() {
		final DeleteHistory actual = deleteHistoryRepository.save(HISTORY);
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getEraser().getName()).isEqualTo(JAVAJIGI.getName())
		);
	}

	@Test
	@DisplayName("삭제를 실행한 유저 확인")
	void findDeleteHistoryWithUser() {
		final DeleteHistory actual = deleteHistoryRepository.save(HISTORY);
		final User user = userRepository.findByUserId(actual.getEraser().getUserId()).get();
		assertThat(actual.getEraser()).isEqualTo(user);
	}
}
