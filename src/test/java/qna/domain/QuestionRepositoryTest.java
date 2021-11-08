package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.fixture.QuestionFixture;
import qna.fixture.UserFixture;

@DisplayName("질문 저장소")
@DataJpaTest
public class QuestionRepositoryTest {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private QuestionRepository questionRepository;

	private User user;

	@BeforeEach
	void setUp() {
		user = userRepository.save(UserFixture.Y2O2U2N());
	}

	@DisplayName("질문을 저장할 수 있다.")
	@Test
	void save() {
		// given
		Question expected = QuestionFixture.Q1(user);

		// when
		Question actual = questionRepository.save(expected);

		// then
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
			() -> assertThat(actual.isDeleted()).isEqualTo(expected.isDeleted()),
			() -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
			() -> assertThat(actual.getWriterId()).isEqualTo(expected.getWriterId())
		);
	}

	@DisplayName("삭제되지 않은 질문을 찾을 수 있다.")
	@Test
	void findByDeletedFalse() {
		// given
		questionRepository.save(QuestionFixture.Q1(user));

		// when
		List<Question> actual = questionRepository.findByDeletedFalse();

		// then
		assertThat(actual).isNotEmpty();
	}

	@DisplayName("삭제되지 않은 질문을 ID로 찾을 수 있다.")
	@Test
	void findByIdAndDeletedFalse() {
		// given
		Question expected = questionRepository.save(QuestionFixture.Q1(user));

		// when
		Question actual = questionRepository.findByIdAndDeletedFalse(expected.getId())
			.orElseThrow(AssertionFailedError::new);

		// then
		assertThat(actual).isEqualTo(expected);
	}
}
