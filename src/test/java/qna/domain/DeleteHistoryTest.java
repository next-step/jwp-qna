package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeleteHistoryTest {

	public static final User JAVAJIGI = new User(1L, "javajigi", "password1", "name1",
		"javajigi@slipp.net");

	private Answer answerWrittenByJavajigi;
	private Question questionWrittenByJavajigi;
	private DeleteHistory deleteHistoryOfQuestionWrittenByJavajigi;

	@BeforeEach
	void initialize() {
		questionWrittenByJavajigi = new Question(1L, "title1", "contents1").writeBy(JAVAJIGI);
		answerWrittenByJavajigi = new Answer(JAVAJIGI, questionWrittenByJavajigi,
			"Answers Contents1");
		deleteHistoryOfQuestionWrittenByJavajigi = new DeleteHistory(ContentType.QUESTION,
			questionWrittenByJavajigi.id(), questionWrittenByJavajigi.writer(),
			LocalDateTime.now());
	}

	@DisplayName("DeleteHistory : equals()")
	@Test
	void equals() {
		//given
		DeleteHistory clonedDeleteHistoryOfQuestionWrittenByJavajigi = new DeleteHistory(
			ContentType.QUESTION, questionWrittenByJavajigi.id(),
			questionWrittenByJavajigi.writer(), LocalDateTime.now());
		DeleteHistory deleteHistoryOfAnswerWrittenByJavajigi = new DeleteHistory(ContentType.ANSWER,
			answerWrittenByJavajigi.id(), answerWrittenByJavajigi.writer(), LocalDateTime.now());

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
