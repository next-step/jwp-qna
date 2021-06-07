package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeleteHistoryTest {

	private Answer answer1;
	private Question question1;
	private DeleteHistory deleteHistory;

	@BeforeEach
	void initialize() {
		question1 = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
		answer1 = new Answer(UserTest.JAVAJIGI, question1, "Answers Contents1");
		deleteHistory = new DeleteHistory(ContentType.QUESTION, question1.id(), question1.writer(),
			LocalDateTime.now());
	}

	@DisplayName("DeleteHistory : equals()")
	@Test
	void equals() {
		//given
		DeleteHistory clonedDeleteHistory = new DeleteHistory(ContentType.QUESTION,
			question1.id(), question1.writer(), LocalDateTime.now());
		DeleteHistory deleteHistory2 = new DeleteHistory(ContentType.ANSWER,
			answer1.id(), answer1.writer(), LocalDateTime.now());

		//when

		//then
		assertAll(
			() -> assertThat(deleteHistory.equals(clonedDeleteHistory)).isTrue(),
			() -> assertThat(deleteHistory.equals(deleteHistory2)).isFalse()
		);
	}
}
