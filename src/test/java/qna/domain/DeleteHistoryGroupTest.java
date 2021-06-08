package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeleteHistoryGroupTest {

	public static final User JAVAJIGI = new User(1L, "javajigi", "password1", "name1",
		"javajigi@slipp.net");

	private Answer firstAnswerWrittenByJavajigi;
	private Answer secondAnswerWrittenByJavajigi;
	private Question questionWrittenByJavajigi;
	private DeleteHistory deleteHistoryOfQuestionWrittenByJavajigi;

	@BeforeEach
	void initialize() {
		questionWrittenByJavajigi = new Question(1L, "title1", "contents1").writeBy(JAVAJIGI);
		firstAnswerWrittenByJavajigi = new Answer(1L, JAVAJIGI, questionWrittenByJavajigi,
			"Answers Contents1");

		deleteHistoryOfQuestionWrittenByJavajigi = new DeleteHistory(ContentType.QUESTION,
			questionWrittenByJavajigi.id(), questionWrittenByJavajigi.writer(),
			LocalDateTime.now());
	}

	@DisplayName("DeleteHistoryGroup : generate()")
	@Test
	void generate() {
		//given

		//when
		DeleteHistoryGroup deleteHistoryGroup = DeleteHistoryGroup.generate();

		//then
		assertAll(
			() -> assertThat(deleteHistoryGroup).isNotNull(),
			() -> assertThat(deleteHistoryGroup.size()).isEqualTo(0)
		);
	}

	@DisplayName("DeleteHistoryGroup : generateByQuestion()")
	@Test
	void generateByQuestion() {
		//given

		//when
		DeleteHistoryGroup deleteHistoryGroup = DeleteHistoryGroup
			.generateByQuestion(questionWrittenByJavajigi);

		//then
		assertAll(
			() -> assertThat(deleteHistoryGroup).isNotNull(),
			() -> assertThat(deleteHistoryGroup.size()).isEqualTo(2)
		);
	}

	@DisplayName("DeleteHistoryGroup : deleteHistories()")
	@Test
	void deleteHistories() {
		//given
		DeleteHistoryGroup deleteHistoryGroup = DeleteHistoryGroup
			.generateByQuestion(questionWrittenByJavajigi);

		//when
		List<DeleteHistory> deleteHistories = deleteHistoryGroup.deleteHistories();

		//then
		assertThat(deleteHistories.size()).isEqualTo(2);
	}

	@DisplayName("DeleteHistoryGroup : add()")
	@Test
	void add() {
		//given
		DeleteHistoryGroup deleteHistoryGroup = DeleteHistoryGroup.generate();
		DeleteHistory deleteHistoryOfQuestionWrittenByJavajigi = questionWrittenByJavajigi
			.generateDeleteHistoryOfQuestion();

		//when
		deleteHistoryGroup.add(deleteHistoryOfQuestionWrittenByJavajigi);

		//then
		assertThat(deleteHistoryGroup.size()).isEqualTo(1);
	}

	@DisplayName("DeleteHistoryGroup : addAll()")
	@Test
	void addAll() {
		//given
		secondAnswerWrittenByJavajigi = new Answer(2L, JAVAJIGI, questionWrittenByJavajigi,
			"Answers Contents2");
		DeleteHistoryGroup deleteHistoryGroup = DeleteHistoryGroup.generate();
		List<DeleteHistory> deleteHistoryAllOfAnswersWrittenByJavajigi = questionWrittenByJavajigi
			.generateDeleteHistoryAllOfAnswers();

		//when
		deleteHistoryGroup.addAll(deleteHistoryAllOfAnswersWrittenByJavajigi);

		//then
		assertThat(deleteHistoryGroup.size()).isEqualTo(2);
	}
}
