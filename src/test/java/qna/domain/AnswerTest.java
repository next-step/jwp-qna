package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.NotFoundException;
import qna.UnAuthorizedException;

public class AnswerTest {

	public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
	public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

	@Test
	@DisplayName("Answer 생성 테스트")
	void create() {
		Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
		assertThat(answer.getContents()).isEqualTo("Answers Contents1");
	}

	@Test
	@DisplayName("Answer 생성 시 UnAuthorizedException 테스트(Writer Null일 경우")
	void validate_Writer_Null일_경우_UnAuthorizedException() {
		assertThatThrownBy(() ->
			new Answer(null, QuestionTest.Q1, "Answer Contents")
		).isInstanceOf(UnAuthorizedException.class);
	}

	@Test
	@DisplayName("Answer 생성 시 NotFoundException 테스트(Question Null일 경우")
	void validate_Question_Null일_경우_NotFoundException() {
		assertThatThrownBy(() ->
			new Answer(UserTest.JAVAJIGI, null, "Answer Contents")
		).isInstanceOf(NotFoundException.class);
	}

}
