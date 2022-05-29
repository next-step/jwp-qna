package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class DeleteHistoryRepositoryTest {
	private Question question;
	private User javajigi;
	private User sanjigi;

	@Autowired
	DeleteHistoryRepository deleteHistoryRepository;

	@BeforeEach
	void init(
		@Autowired final UserRepository userRepository,
		@Autowired final QuestionRepository questionRepository
	) {
		javajigi = userRepository.save(UserTest.JAVAJIGI);
		sanjigi = userRepository.save(UserTest.SANJIGI);
		question = questionRepository.save(QuestionTest.Q1);
	}

	@Test
	@DisplayName("삭제 History 생성")
	void save() {
		DeleteHistory expected = new DeleteHistory(ContentType.ANSWER, question.getId(), javajigi, LocalDateTime.now());
		DeleteHistory actual = deleteHistoryRepository.save(expected);

		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getContentType()).isEqualTo(expected.getContentType())
		);
	}

	@Test
	@DisplayName("삭제 History 조회")
	void findAll() {
		DeleteHistory actual1 = deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, question.getId(), javajigi, LocalDateTime.now()));
		DeleteHistory actual2 = deleteHistoryRepository.save(new DeleteHistory(ContentType.ANSWER, question.getId(), sanjigi, LocalDateTime.now()));

		assertThat(deleteHistoryRepository.findAll()).contains(actual1, actual2);
	}
}
