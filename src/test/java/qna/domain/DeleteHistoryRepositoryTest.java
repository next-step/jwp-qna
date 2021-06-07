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

	public static final User JAVAJIGI = new User(1L, "javajigi", "password1", "name1",
		"javajigi@slipp.net");
	public static final User SANJIGI = new User(2L, "sanjigi", "password2", "name2",
		"sanjigi@slipp.net");

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
	private Question savedQuestionWrittenByJavajigi;
	private Answer savedAnswerWrittenBySanjigi;
	private DeleteHistory deleteHistoryOfQuestionWrittenByJavajigi;
	private DeleteHistory deleteHistoryOfAnswerWrittenBySanjigi;

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
		savedJavajigi = users.save(JAVAJIGI);
		savedSangiji = users.save(SANJIGI);
	}

	private void 질문정보_저장() {
		savedQuestionWrittenByJavajigi = questions.save(new Question(1L, "title1", "contents1")
			.writeBy(savedJavajigi));
	}

	private void 답변정보_저장() {
		savedAnswerWrittenBySanjigi = answers
			.save(new Answer(savedSangiji, savedQuestionWrittenByJavajigi,
				"Answers Contents1"));
	}

	private void 삭제_히스토리_객체_초기화() {
		deleteHistoryOfQuestionWrittenByJavajigi = new DeleteHistory(ContentType.QUESTION,
			savedQuestionWrittenByJavajigi.id(),
			savedQuestionWrittenByJavajigi.writer(), LocalDateTime.now());
		deleteHistoryOfAnswerWrittenBySanjigi = new DeleteHistory(ContentType.ANSWER,
			savedAnswerWrittenBySanjigi.id(),
			savedAnswerWrittenBySanjigi.writer(), LocalDateTime.now());
	}

	@DisplayName("DeleteHistory 저장 : save()")
	@Test
	void save() {
		//given

		//when
		DeleteHistory actualDeleteHistoryOfQuestionWrittenByJavajigi = deleteHistories
			.save(deleteHistoryOfQuestionWrittenByJavajigi);
		DeleteHistory actualDeleteHistoryOfAnswerWrittenBySanjigi = deleteHistories
			.save(deleteHistoryOfAnswerWrittenBySanjigi);

		//then
		assertAll(
			() -> assertThat(actualDeleteHistoryOfQuestionWrittenByJavajigi
				.equals(deleteHistoryOfQuestionWrittenByJavajigi)).isTrue(),
			() -> assertThat(actualDeleteHistoryOfAnswerWrittenBySanjigi
				.equals(deleteHistoryOfAnswerWrittenBySanjigi)).isTrue()
		);
	}

	@DisplayName("DeleteHistory 조회 : findById()")
	@Test
	void findById() {
		//given
		DeleteHistory expectedDeleteHistoryOfQuestionWrittenByJavajigi = deleteHistories
			.save(deleteHistoryOfQuestionWrittenByJavajigi);
		DeleteHistory expectedDeleteHistoryOfAnswerWrittenBySanjigi = deleteHistories
			.save(deleteHistoryOfAnswerWrittenBySanjigi);

		//when
		DeleteHistory actualDeleteHistoryOfQuestionWrittenByJavajigi = deleteHistories
			.findById(expectedDeleteHistoryOfQuestionWrittenByJavajigi.id()).get();
		DeleteHistory actualDeleteHistoryOfAnswerWrittenBySanjigi = deleteHistories
			.findById(expectedDeleteHistoryOfAnswerWrittenBySanjigi.id()).get();

		//then
		assertAll(
			() -> assertThat(actualDeleteHistoryOfQuestionWrittenByJavajigi
				.equals(expectedDeleteHistoryOfQuestionWrittenByJavajigi)).isTrue(),
			() -> assertThat(actualDeleteHistoryOfAnswerWrittenBySanjigi
				.equals(expectedDeleteHistoryOfAnswerWrittenBySanjigi)).isTrue()
		);
	}

	@DisplayName("DeleteHistory 삭제 : delete()")
	@Test
	void delete() {
		//given
		DeleteHistory expectedDeleteHistoryOfQuestionWrittenByJavajigi = deleteHistories
			.save(deleteHistoryOfQuestionWrittenByJavajigi);
		DeleteHistory deleteHistoryOfQuestionWrittenByJavajigiBeforeDelete = deleteHistories
			.findById(expectedDeleteHistoryOfQuestionWrittenByJavajigi.id()).get();

		//when
		deleteHistories.delete(expectedDeleteHistoryOfQuestionWrittenByJavajigi);
		Optional<DeleteHistory> deleteHistoryOfQuestionWrittenByJavajigiAfterDelete = deleteHistories
			.findById(expectedDeleteHistoryOfQuestionWrittenByJavajigi.id());

		//then
		assertAll(
			() -> assertThat(deleteHistoryOfQuestionWrittenByJavajigiBeforeDelete).isNotNull(),
			() -> assertThat(deleteHistoryOfQuestionWrittenByJavajigiAfterDelete.isPresent())
				.isFalse()
		);
	}
}
