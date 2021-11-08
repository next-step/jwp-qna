package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.fixture.AnswerFixture;
import qna.fixture.DeleteHistoryFixture;
import qna.fixture.QuestionFixture;
import qna.fixture.UserFixture;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private AnswerRepository answerRepository;
	@Autowired
	private DeleteHistoryRepository deleteHistoryRepository;

	private User user;
	private Question question;
	private Answer answer;

	@BeforeEach
	void setUp() {
		user = userRepository.save(UserFixture.Y2O2U2N());
		question = questionRepository.save(QuestionFixture.Q1(user));
		answer = answerRepository.save(AnswerFixture.A1(user, question));
	}

	@DisplayName("질문 삭제 기록을 저장할 수 있다.")
	@Test
	void save_question_delete_history() {
		// given
		DeleteHistory expected = DeleteHistoryFixture.Q(question.getId(), user.getId());

		// when
		DeleteHistory actual = deleteHistoryRepository.save(expected);

		// then
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getContentId()).isEqualTo(expected.getContentId()),
			() -> assertThat(actual.getContentType()).isEqualTo(expected.getContentType()),
			() -> assertThat(actual.getDeletedById()).isEqualTo(expected.getDeletedById())
		);
	}

	@DisplayName("답변 삭제 기록을 저장할 수 있다.")
	@Test
	void save_answer_delete_history() {
		// given
		DeleteHistory expected = DeleteHistoryFixture.A(answer.getId(), user.getId());

		// when
		DeleteHistory actual = deleteHistoryRepository.save(expected);

		// then
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getContentId()).isEqualTo(expected.getContentId()),
			() -> assertThat(actual.getContentType()).isEqualTo(expected.getContentType()),
			() -> assertThat(actual.getDeletedById()).isEqualTo(expected.getDeletedById())
		);
	}
}
