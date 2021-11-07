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

import qna.fixture.AnswerFixture;
import qna.fixture.QuestionFixture;
import qna.fixture.UserFixture;

@DataJpaTest
public class AnswerRepositoryTest {
	@Autowired
	private UserRepository users;
	@Autowired
	private QuestionRepository questions;
	@Autowired
	private AnswerRepository answers;

	private User user;
	private Question question;

	@BeforeEach
	void setUp() {
		user = users.save(UserFixture.Y2O2U2N());
		question = questions.save(QuestionFixture.Q1(user.getId()));
	}

	@DisplayName("답변을 저장할 수 있다.")
	@Test
	void save() {
		// given
		Answer expected = AnswerFixture.A1(question.getId(), user.getId());

		// when
		Answer actual = answers.save(expected);

		// then
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
			() -> assertThat(actual.isDeleted()).isEqualTo(expected.isDeleted()),
			() -> assertThat(actual.getQuestionId()).isEqualTo(expected.getQuestionId()),
			() -> assertThat(actual.getWriterId()).isEqualTo(expected.getWriterId())
		);
	}

	@DisplayName("질문 ID로 삭제되지 않은 답변을 찾을 수 있다.")
	@Test
	void findByQuestionIdAndDeletedFalse() {
		// given
		answers.save(AnswerFixture.A1(question.getId(), user.getId()));

		// when
		List<Answer> actual = answers.findByQuestionIdAndDeletedFalse(question.getId());

		// then
		assertThat(actual).isNotEmpty();
	}

	@DisplayName("ID로 삭제되지 않은 답변을 찾을 수 있다.")
	@Test
	void findByIdAndDeletedFalse() {
		// given
		Answer expected = answers.save(AnswerFixture.A1(question.getId(), user.getId()));

		// when
		Answer actual = answers.findByIdAndDeletedFalse(expected.getId())
			.orElseThrow(AssertionFailedError::new);

		// then
		assertThat(actual).isEqualTo(expected);
	}
}
