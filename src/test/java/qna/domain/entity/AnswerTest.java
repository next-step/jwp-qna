package qna.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AnswerTest {

	public static final User JAVAJIGI = User.generate(1L, "javajigi", "password1", "name1",
		"javajigi@slipp.net");
	public static final User SANJIGI = User.generate(2L, "sanjigi", "password2", "name2",
		"sanjigi@slipp.net");

	private Answer answerWrittenByJavajigi;
	private Question questionWrittenByJavajigi;

	@BeforeEach
	void initialize() {
		questionWrittenByJavajigi = Question.generate(1L, "title1", "contents1").writeBy(JAVAJIGI);
		answerWrittenByJavajigi = Answer.generate(1L, JAVAJIGI, questionWrittenByJavajigi,
			"Answers Contents1");
	}

	@DisplayName("Answer : equals()")
	@Test
	void equals() {
		//given
		Answer clonedAnswerWrittenByJavajigi = Answer
			.generate(1L, JAVAJIGI, questionWrittenByJavajigi,
				"Answers Contents1");
		Answer answerWrittenBySanjigi = Answer.generate(2L, SANJIGI, questionWrittenByJavajigi,
			"Answers Contents2");

		//when

		//then
		assertAll(
			() -> assertThat(answerWrittenByJavajigi.equals(clonedAnswerWrittenByJavajigi))
				.isTrue(),
			() -> assertThat(answerWrittenByJavajigi.equals(answerWrittenBySanjigi))
				.isFalse()
		);
	}

	@DisplayName("Answer : changeQuestion()")
	@Test
	void changeQuestion() {
		//given
		Question anotherQuestionWrittenBySanjigi = Question.generate(2L, "title2", "contents2")
			.writeBy(SANJIGI);

		//when
		answerWrittenByJavajigi.changeQuestion(anotherQuestionWrittenBySanjigi);

		//then
		assertAll(
			() -> assertThat(
				answerWrittenByJavajigi.question().equals(anotherQuestionWrittenBySanjigi))
				.isTrue(),
			() -> assertThat(anotherQuestionWrittenBySanjigi.answers().size()).isEqualTo(1),
			() -> assertThat(
				anotherQuestionWrittenBySanjigi.answers().get(0).equals(answerWrittenByJavajigi))
				.isTrue()
		);
	}

	@DisplayName("Answer Soft delete : delete()")
	@Test
	void delete() {
		//given

		//when
		answerWrittenByJavajigi.delete();

		//then
		assertThat(answerWrittenByJavajigi.isDeleted()).isTrue();
	}
}
