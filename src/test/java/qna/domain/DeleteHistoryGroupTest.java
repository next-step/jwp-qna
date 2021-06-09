package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeleteHistoryGroupTest {

	public static final User JAVAJIGI = User.generate(1L, "javajigi", "password1", "name1",
		"javajigi@slipp.net");

	private Answer firstAnswerWrittenByJavajigi;
	private Question questionWrittenByJavajigi;

	@BeforeEach
	void initialize() {
		questionWrittenByJavajigi = Question.generate(1L, "title1", "contents1").writeBy(JAVAJIGI);
		firstAnswerWrittenByJavajigi = Answer.generate(1L, JAVAJIGI, questionWrittenByJavajigi,
			"Answers Contents1");
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

	@DisplayName("DeleteHistoryGroup : deleteHistories()")
	@Test
	void deleteHistories() {
		//given
		DeleteHistoryGroup deleteHistoryGroup = questionWrittenByJavajigi.delete(JAVAJIGI);

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
		DeleteHistory deleteHistoryOfQuestionWrittenByJavajigi = DeleteHistory
			.ofQuestion(questionWrittenByJavajigi.id(), questionWrittenByJavajigi.writer());

		//when
		deleteHistoryGroup.add(deleteHistoryOfQuestionWrittenByJavajigi);

		//then
		assertThat(deleteHistoryGroup.size()).isEqualTo(1);
	}

	@DisplayName("DeleteHistoryGroup : addAll()")
	@Test
	void addAll() {
		//given
		DeleteHistoryGroup deleteHistoryGroup = DeleteHistoryGroup.generate();
		DeleteHistory deleteHistoryOfQuestionWrittenByJavajigi = DeleteHistory
			.ofQuestion(questionWrittenByJavajigi.id(), questionWrittenByJavajigi.writer());
		DeleteHistory deleteHistoryOfAnswerWrittenByJavajigi = DeleteHistory
			.ofAnswer(firstAnswerWrittenByJavajigi.id(), firstAnswerWrittenByJavajigi.writer());
		List<DeleteHistory> deleteHistories = Lists
			.newArrayList(deleteHistoryOfQuestionWrittenByJavajigi,
				deleteHistoryOfAnswerWrittenByJavajigi);

		//when
		deleteHistoryGroup.addAll(deleteHistories);

		//then
		assertThat(deleteHistoryGroup.size()).isEqualTo(2);
	}
}
