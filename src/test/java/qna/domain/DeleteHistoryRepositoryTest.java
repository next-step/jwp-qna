package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DeleteHistoryRepositoryTest {

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
		초기_정보_저장();
	}

	private void 초기_정보_저장() {
		작성자정보_저장();
		질문정보_저장();
		답변정보_저장();
		삭제_히스토리_객체_초기화();
	}

	private void 작성자정보_저장() {
		savedJavajigi = users.save(UserTest.JAVAJIGI);
		savedSangiji = users.save(UserTest.SANJIGI);
	}

	private void 질문정보_저장() {
		savedQuestion = questions.save(new Question(1L, "title1", "contents1")
			.writeBy(UserTest.JAVAJIGI).writeBy(savedJavajigi));
	}

	private void 답변정보_저장() {
		Answer tempAnswer = new Answer(UserTest.JAVAJIGI, savedQuestion, "Answers Contents1");
		tempAnswer.addQuestion(savedQuestion);
		tempAnswer.writtenBy(savedSangiji);
		savedAnswer = answers.save(tempAnswer);
	}

	private void 삭제_히스토리_객체_초기화() {
		deleteHistory1 = new DeleteHistory(ContentType.QUESTION, savedQuestion.id(),
			savedQuestion.writer(), LocalDateTime.now());
		deleteHistory2 = new DeleteHistory(ContentType.ANSWER, savedAnswer.id(),
			savedAnswer.writer(), LocalDateTime.now());
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
		Optional<DeleteHistory> afterDeleteDeleteHistoryOptional = deleteHistories
			.findById(expected.id());

		//then
		assertAll(
			() -> assertThat(beforeDeleteDeleteHistory).isNotNull(),
			() -> assertThat(afterDeleteDeleteHistoryOptional.isPresent()).isFalse()
		);
	}
}
