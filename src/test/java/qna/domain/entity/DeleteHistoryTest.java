package qna.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeleteHistoryTest {

	public static final User JAVAJIGI = User.generate(1L, "javajigi", "password1", "name1",
		"javajigi@slipp.net");

	private Answer answerWrittenByJavajigi;
	private Question questionWrittenByJavajigi;
	private DeleteHistory deleteHistoryOfQuestionWrittenByJavajigi;

	@BeforeEach
	void initialize() {
		questionWrittenByJavajigi = Question.generate(1L, "title1", "contents1").writeBy(JAVAJIGI);
		answerWrittenByJavajigi = Answer.generate(1L, JAVAJIGI, questionWrittenByJavajigi,
			"Answers Contents1");
		deleteHistoryOfQuestionWrittenByJavajigi = DeleteHistory.ofQuestion(
			questionWrittenByJavajigi.id(), questionWrittenByJavajigi.writer());
	}

	@DisplayName("DeleteHistory : equals()")
	@Test
	void equals() {
		//given
		DeleteHistory clonedDeleteHistoryOfQuestionWrittenByJavajigi = DeleteHistory.ofQuestion(
			questionWrittenByJavajigi.id(), questionWrittenByJavajigi.writer());
		DeleteHistory deleteHistoryOfAnswerWrittenByJavajigi = DeleteHistory.ofAnswer(
			answerWrittenByJavajigi.id(), answerWrittenByJavajigi.writer());

		//when

		//then
		assertAll(
			() -> assertThat(deleteHistoryOfQuestionWrittenByJavajigi
				.equals(clonedDeleteHistoryOfQuestionWrittenByJavajigi))
				.isTrue(),
			() -> assertThat(deleteHistoryOfQuestionWrittenByJavajigi
				.equals(deleteHistoryOfAnswerWrittenByJavajigi))
				.isFalse()
		);
	}
}
