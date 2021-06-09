package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {

	public static final User JAVAJIGI = User.generate(1L, "javajigi", "password1", "name1",
		"javajigi@slipp.net");
	public static final User SANJIGI = User.generate(2L, "sanjigi", "password2", "name2",
		"sanjigi@slipp.net");

	private Question questionWrittenByJavajigi;

	@BeforeEach
	void initialize() {
		questionWrittenByJavajigi = Question.generate(1L, "title1", "contents1").writeBy(JAVAJIGI);
	}

	@DisplayName("Question : equals()")
	@Test
	void equals() {
		//given
		Question clonedQuestionWrittenByJavajigi = Question.generate(1L, "title1", "contents1")
			.writeBy(JAVAJIGI);
		Question questionWrittenBySanjigi = Question.generate(2L, "title2", "contents2")
			.writeBy(SANJIGI);

		//when

		//then
		assertAll(
			() -> assertThat(questionWrittenByJavajigi.equals(clonedQuestionWrittenByJavajigi))
				.isTrue(),
			() -> assertThat(questionWrittenByJavajigi.equals(questionWrittenBySanjigi)).isFalse()
		);
	}

	@DisplayName("Question 조회 : addAnswer()")
	@Test
	void addAnswer() {
		//given
		Answer answerWrittenByJavajigi = Answer.generate(JAVAJIGI, questionWrittenByJavajigi,
			"Answers Contents1");

		//when
		questionWrittenByJavajigi.addAnswer(answerWrittenByJavajigi);

		//then
		assertAll(
			() -> assertThat(questionWrittenByJavajigi.answers().size()).isEqualTo(1),
			() -> assertThat(
				questionWrittenByJavajigi.answers().get(0).equals(answerWrittenByJavajigi))
				.isTrue(),
			() -> assertThat(answerWrittenByJavajigi.question().equals(questionWrittenByJavajigi))
				.isTrue()
		);
	}

	@DisplayName("Question Soft delete : delete()")
	@Test
	void delete() {
		//given

		//when
		questionWrittenByJavajigi.delete(JAVAJIGI);

		//then
		assertThat(questionWrittenByJavajigi.isDeleted()).isTrue();
	}
}
