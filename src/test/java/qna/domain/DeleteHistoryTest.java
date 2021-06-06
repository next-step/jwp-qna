package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryTest {
	// public static DeleteHistory DELETE_HISTORY_1 = new DeleteHistory(ContentType.QUESTION,
	// 	QuestionTest.Q1.getId(), QuestionTest.Q1.getWriter(), LocalDateTime.now());
	// public static DeleteHistory DELETE_HISTORY_2 = new DeleteHistory(ContentType.ANSWER,
	// 	AnswerTest.A2.getId(), AnswerTest.A2.getWriter(), LocalDateTime.now());

	@Autowired
	private DeleteHistoryRepository deleteHistories;
	@Autowired
	private UserRepository users;
	@Autowired
	private AnswerRepository answers;
	@Autowired
	private QuestionRepository questions;
	private User savedJavajigi;
	private User savedSangiji;
	private Question savedQuestion;
	private Answer savedAnswer;
	private DeleteHistory deleteHistory1;
	private DeleteHistory deleteHistory2;

	@DisplayName("테스트 초기화")
	@BeforeEach
	void setup() {
		모든_레포지토리_데이터_삭제();
		초기_정보_저장();
	}

	private void 모든_레포지토리_데이터_삭제() {
		users.deleteAll();
		questions.deleteAll();
		answers.deleteAll();
	}

	private void 초기_정보_저장() {
		작성자정보_저장();
		질문정보_저장();
		답변정보_저장();
		삭제_히스토리_객체_초기화();
	}

	private void 삭제_히스토리_객체_초기화() {
		deleteHistory1 = new DeleteHistory(ContentType.QUESTION, savedQuestion.id(),
			savedQuestion.writer(), LocalDateTime.now());
		deleteHistory2 = new DeleteHistory(ContentType.ANSWER, savedAnswer.id(),
			savedAnswer.writer(), LocalDateTime.now());
	}

	private void 답변정보_저장() {
		if (Objects.isNull(savedAnswer)) {
			Answer tempAnswer = AnswerTest.A1;
			tempAnswer.toQuestion(savedQuestion);
			tempAnswer.writtenBy(savedSangiji);
			savedAnswer = answers.save(tempAnswer);
		}
	}

	private void 질문정보_저장() {
		if (Objects.isNull(savedQuestion)) {
			savedQuestion = questions.save(QuestionTest.Q1.writeBy(savedJavajigi));
		}
	}

	private void 작성자정보_저장() {
		if (Objects.isNull(savedJavajigi)) {
			savedJavajigi = users.save(UserTest.JAVAJIGI);
		}
		if (Objects.isNull(savedSangiji)) {
			savedSangiji = users.save(UserTest.SANJIGI);
		}
	}

	@DisplayName("DeleteHistory 저장 : save()")
	@Test
	void save() {
		//given

		//when
		DeleteHistory actual = deleteHistories.save(deleteHistory1);
		DeleteHistory actual2 = deleteHistories.save(deleteHistory2);

		//then
		assertAll(
			() -> assertThat(actual.equals(deleteHistory1)).isTrue(),
			() -> assertThat(actual2.equals(deleteHistory2)).isTrue()
		);
	}

	@DisplayName("DeleteHistory 조회 : findById()")
	@Test
	void findById() {
		//given
		DeleteHistory expected = deleteHistories.save(deleteHistory1);
		DeleteHistory expected2 = deleteHistories.save(deleteHistory2);

		//when
		DeleteHistory actual = deleteHistories.findById(expected.id()).get();
		DeleteHistory actual2 = deleteHistories.findById(expected2.id()).get();

		//then
		assertAll(
			() -> assertThat(actual.equals(expected)).isTrue(),
			() -> assertThat(actual2.equals(expected2)).isTrue()
		);
	}

	@DisplayName("DeleteHistory 삭제 : delete()")
	@Test
	void delete() {
		//given
		DeleteHistory expected = deleteHistories.save(deleteHistory1);
		DeleteHistory beforeDeleteDeleteHistory = deleteHistories.findById(expected.id()).get();

		//when
		deleteHistories.delete(expected);
		deleteHistories.flush();
		Optional<DeleteHistory> afterDeleteDeleteHistoryOptional = deleteHistories.findById(expected.id());

		//then
		assertAll(
			() -> assertThat(beforeDeleteDeleteHistory).isNotNull(),
			() -> assertThat(afterDeleteDeleteHistoryOptional.isPresent()).isFalse()
		);
	}
}
