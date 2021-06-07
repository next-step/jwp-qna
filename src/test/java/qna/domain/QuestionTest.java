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
public class QuestionTest {

	private Question question1;

	@BeforeEach
	void initialize() {
		question1 = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
	}

	@DisplayName("Question : equals()")
	@Test
	@Order(1)
	void equals() {
		//given
		Question clonedQuestion1 = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
		Question question2 = new Question(2L, "title2", "contents2").writeBy(UserTest.SANJIGI);

		//when

		//then
		assertAll(
			() -> assertThat(question1.equals(clonedQuestion1)).isTrue(),
			() -> assertThat(question1.equals(question2)).isFalse()
		);
	}

	@DisplayName("Question 조회 : addAnswer()")
	@Test
	@Order(2)
	void addAnswer() {
		//given
		Answer answer1 = new Answer(UserTest.JAVAJIGI, question1, "Answers Contents1");

		//when
		question1.addAnswer(answer1);

		//then
		assertAll(
			() -> assertThat(question1.answers().size()).isEqualTo(1),
			() -> assertThat(question1.answers().get(0).equals(answer1)).isTrue(),
			() -> assertThat(answer1.question().equals(question1)).isTrue()
		);
	}

	@DisplayName("Question Soft delete : delete()")
	@Test
	@Order(3)
	void delete() {
		//given

		//when
		question1.delete();

		//then
		assertThat(question1.isDeleted()).isTrue();
	}
}
