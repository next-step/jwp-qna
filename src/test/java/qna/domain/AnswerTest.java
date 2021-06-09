package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AnswerTest {

	private Answer answer1;
	private Question question1;

	@BeforeEach
	void initialize() {
		question1 = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
		answer1 = new Answer(UserTest.JAVAJIGI, question1, "Answers Contents1");
	}

	@DisplayName("Answer : equals()")
	@Test
	@Order(1)
	void equals() {
		//given
		Answer clonedAnswer1 = new Answer(UserTest.JAVAJIGI, question1, "Answers Contents1");
		Answer answer2 = new Answer(UserTest.SANJIGI, question1, "Answers Contents2");

		//when

		//then
		assertAll(
			() -> assertThat(answer1.equals(clonedAnswer1)).isTrue(),
			() -> assertThat(answer1.equals(answer2)).isFalse()
		);
	}

	@DisplayName("Answer : addQuestion()")
	@Test
	@Order(2)
	void addQuestion() {
		//given
		Question question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);

		//when
		answer1.addQuestion(question);

		//then
		assertAll(
			() -> assertThat(answer1.question().equals(question)).isTrue(),
			() -> assertThat(question.answers().size()).isEqualTo(1),
			() -> assertThat(question.answers().get(0).equals(answer1)).isTrue()
		);
	}

	@DisplayName("Answer Soft delete : delete()")
	@Test
	@Order(3)
	void delete() {
		//given

		//when
		answer1.delete();

		//then
		assertThat(answer1.isDeleted()).isTrue();
	}
}
