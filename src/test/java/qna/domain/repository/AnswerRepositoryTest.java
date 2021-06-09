package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.entity.Answer;
import qna.domain.entity.Question;
import qna.domain.entity.User;

@DataJpaTest
public class AnswerRepositoryTest {

	public static final User JAVAJIGI = User.generate(1L, "javajigi", "password1", "name1",
		"javajigi@slipp.net");
	public static final User SANJIGI = User.generate(2L, "sanjigi", "password2", "name2",
		"sanjigi@slipp.net");

	@Autowired
	private UserRepository users;
	@Autowired
	private AnswerRepository answers;
	@Autowired
	private QuestionRepository questions;
	private Answer answerWrittenByJavajigi;
	private Answer answerWrittenBySanjigi;
	private User savedJavajigi;
	private User savedSangiji;
	private Question savedQuestionWrittenByJavajigi;
	private Question savedQuestionWrittenBySanjigi;

	@DisplayName("테스트 초기화")
	@BeforeEach
	void setup() {
		초기_정보_저장();
	}

	private void 초기_정보_저장() {
		각_답변별_작성자정보_저장();
		각_답변별_질문정보_저장();
		답변_인스턴스_생성();
		연관_관계_매핑();
	}

	private void 연관_관계_매핑() {
		answerWrittenByJavajigi.writtenBy(savedJavajigi);
		answerWrittenBySanjigi.writtenBy(savedSangiji);
	}

	private void 답변_인스턴스_생성() {
		answerWrittenByJavajigi = Answer.generate(JAVAJIGI, savedQuestionWrittenByJavajigi,
			"Answers Contents1");
		answerWrittenBySanjigi = Answer.generate(SANJIGI, savedQuestionWrittenBySanjigi,
			"Answers Contents2");
	}

	private void 각_답변별_질문정보_저장() {
		Question tempQuestion1 = Question.generate(1L, "title1", "contents1").writeBy(JAVAJIGI);
		tempQuestion1.writeBy(savedJavajigi);
		savedQuestionWrittenByJavajigi = questions.save(tempQuestion1);

		Question tempQuestion2 = Question.generate(2L, "title2", "contents2").writeBy(SANJIGI);
		tempQuestion2.writeBy(savedSangiji);
		savedQuestionWrittenBySanjigi = questions.save(tempQuestion2);
	}

	private void 각_답변별_작성자정보_저장() {
		savedJavajigi = users.save(JAVAJIGI);
		savedSangiji = users.save(SANJIGI);
	}

	@DisplayName("Answer 저장 : save()")
	@Test
	void save() {
		//given

		//when
		Answer actualAnswerWrittenByJavajigi = answers.save(answerWrittenByJavajigi);
		Answer actualAnswerWrittenBySanjigi = answers.save(answerWrittenBySanjigi);

		//then
		assertAll(
			() -> assertThat(actualAnswerWrittenByJavajigi).isNotNull(),
			() -> assertThat(actualAnswerWrittenBySanjigi).isNotNull()
		);
	}

	@DisplayName("Answer 조회 : findById()")
	@Test
	void findById() {
		//given
		Answer expectedAnswerWrittenByJavajigi = answers.save(answerWrittenByJavajigi);
		Answer expectedAnswerWrittenBySanjigi = answers.save(answerWrittenBySanjigi);

		//when
		Answer actualAnswerWrittenByJavajigi = answers
			.findById(expectedAnswerWrittenByJavajigi.id()).get();
		Answer actualAnswerWrittenBySanjigi = answers
			.findById(expectedAnswerWrittenBySanjigi.id()).get();

		//then
		assertAll(
			() -> assertThat(actualAnswerWrittenByJavajigi.equals(expectedAnswerWrittenByJavajigi))
				.isTrue(),
			() -> assertThat(actualAnswerWrittenBySanjigi.equals(expectedAnswerWrittenBySanjigi))
				.isTrue()
		);
	}

	@DisplayName("Answer Soft delete - 조회 : findByIdAndDeletedFalse(), 수정 : delete()")
	@Test
	void delete() {
		//given
		Answer expectedAnswerWrittenByJavajigi = answers.saveAndFlush(answerWrittenByJavajigi);

		//when
		Optional<Answer> answerBeforeSoftDelete = answers
			.findByIdAndDeletedFalse(expectedAnswerWrittenByJavajigi.id());
		expectedAnswerWrittenByJavajigi.delete();
		Optional<Answer> answerAfterSoftDelete = answers
			.findByIdAndDeletedFalse(expectedAnswerWrittenByJavajigi.id());

		//then
		assertAll(
			() -> assertThat(answerBeforeSoftDelete.isPresent()).isTrue(),
			() -> assertThat(answerAfterSoftDelete.isPresent()).isFalse()
		);
	}

	@DisplayName("Answer 작성자 변경 - 조회 : findById(), 수정 : writtenBy()")
	@Test
	void writtenBy() {
		//given
		Answer expectedAnswerWrittenByJavajigi = answers.save(answerWrittenByJavajigi);

		//when
		expectedAnswerWrittenByJavajigi.writtenBy(savedSangiji);
		Optional<Answer> answerAfterChangeWriter = answers
			.findById(expectedAnswerWrittenByJavajigi.id());

		//then
		assertAll(
			() -> assertThat(answerAfterChangeWriter.get().writer().equals(savedSangiji)).isTrue(),
			() -> assertThat(answerAfterChangeWriter.get().writer().equals(savedJavajigi)).isFalse()
		);
	}

	@DisplayName("Answer 질문 변경 - 조회 : findById(), 수정 : changeQuestion()")
	@Test
	void changeQuestion() {
		//given
		Answer expectedAnswerWrittenByJavajigi = answers.save(answerWrittenByJavajigi);

		//when
		expectedAnswerWrittenByJavajigi.changeQuestion(savedQuestionWrittenBySanjigi);
		Optional<Answer> answerAfterChangeQuestion = answers
			.findById(expectedAnswerWrittenByJavajigi.id());

		//then
		assertAll(
			() -> assertThat(answerAfterChangeQuestion.get().question().equals(
				savedQuestionWrittenBySanjigi)).isTrue(),
			() -> assertThat(answerAfterChangeQuestion.get().question().equals(
				savedQuestionWrittenByJavajigi)).isFalse()
		);
	}

	@DisplayName("Answer 삭제 : delete()")
	@Test
	void hardDelete() {
		//given
		Answer expectedAnswerWrittenByJavajigi = answers.save(answerWrittenByJavajigi);
		Answer answerBeforeDelete = answers.findById(expectedAnswerWrittenByJavajigi.id()).get();

		//when
		answerBeforeDelete.delete();
		Optional<Answer> afterDeleteAnswerOptional = answers
			.findById(expectedAnswerWrittenByJavajigi.id());

		//then
		assertAll(
			() -> assertThat(answerBeforeDelete).isNotNull(),
			() -> assertThat(afterDeleteAnswerOptional.get().isDeleted()).isTrue()
		);
	}
}
