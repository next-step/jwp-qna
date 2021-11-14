package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

class AnswersTest {

	@Test
	@DisplayName("Answer 추가")
	public void add() {
		Answers answers = new Answers();
		answers.add(AnswerTest.A1);
		assertThat(answers.hasAnswer(AnswerTest.A1)).isTrue();
		assertThat(answers.getAll().get(0).getQuestion()).isEqualTo(QuestionTest.Q1);
	}

	@Test
	@DisplayName("Answer들 삭제 처리")
	public void delete() throws CannotDeleteException {

		Answer a1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answer entity unit test");
		Answer a2 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answer entity unit test2");
		Answers answers = new Answers(Arrays.asList(a1, a2));
		assertThat(answers.getAll().stream().filter(it -> it.isDeleted()).count()).isEqualTo(0);
		answers.delete(UserTest.JAVAJIGI);
		assertThat(answers.getAll().stream().filter(it -> it.isDeleted()).count()).isEqualTo(2);
	}
}