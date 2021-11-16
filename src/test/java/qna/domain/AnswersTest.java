package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

class AnswersTest {

	@Test
	void 답변_추가_테스트() {
		// given
		User user = new User("test1", "password", "test1", "test1");
		Question question = new Question("q", "q2").writeBy(user);
		Answer answer = new Answer(user, question, "a1");

		// when
		question.addAnswer(answer);

		// then
		assertAll(
			() -> assertThat(question.getAnswers().getAnswerList().size()).isEqualTo(1),
			() -> assertThat(question.getAnswers().getAnswerList().contains(answer)).isTrue(),
			() -> assertThat(question.getAnswers().getAnswerList().get(0).isOwner(user)).isTrue(),
			() -> assertThat(question.isOwner(user)).isTrue()
		);
	}

	@Test
	void 답변_삭제시_다른유저의_답변이_있을때_예외처리_테스트() {
		// given
		User user1 = new User("test1", "password", "test1", "test1");
		User user2 = new User("test2", "password", "test2", "test2");
		Answer answer1 = new Answer(user2, QuestionTest.Q1, "a1");
		Answer answer2 = new Answer(user1, QuestionTest.Q1, "a2");
		Answers answers = new Answers();

		answers.addAll(Arrays.asList(answer1, answer2));

		// when // then
		assertThatThrownBy(() -> {
			answers.deleteAll(user1);
		}).isInstanceOf(CannotDeleteException.class);
	}

	@Test
	void 답변_삭제시_다른유저의_답변일때_예외처리_테스트() {
		// given
		User user1 = new User("test1", "password", "test1", "test1");
		User user2 = new User("test2", "password", "test2", "test2");
		Answer answer1 = new Answer(user1, QuestionTest.Q1, "a1");
		Answer answer2 = new Answer(user2, QuestionTest.Q1, "a2");
		Answers answers = new Answers();

		answers.addAll(Arrays.asList(answer1, answer2));

		// when // then
		assertThatThrownBy(() -> {
			answers.delete(answer1, user2);
		}).isInstanceOf(CannotDeleteException.class);
	}

	@Test
	void 답변_삭제_테스트() throws Exception {
		// given
		User user1 = new User("test1", "password", "test1", "test1");
		User user2 = new User("test2", "password", "test2", "test2");
		Answer answer1 = new Answer(user1, QuestionTest.Q1, "a1");
		Answer answer2 = new Answer(user2, QuestionTest.Q1, "a2");
		Answers answers = new Answers();

		answers.addAll(Arrays.asList(answer1, answer2));

		// when
		DeleteHistory deleteHistory = answers.delete(answer1, user1);

		// then
		assertAll(
			() -> assertThat(answers.getAnswerList().contains(answer1)).isFalse(),
			() -> assertThat(answers.getAnswerList().contains(answer2)).isTrue(),
			() -> assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER),
			() -> assertThat(deleteHistory.getDeletedByUser()).isEqualTo(user1)
		);
	}

	@Test
	void 답변_전부_삭제_테스트() throws Exception {
		// given
		User user1 = new User("test1", "password", "test1", "test1");
		Answer answer1 = new Answer(user1, QuestionTest.Q1, "a1");
		Answer answer2 = new Answer(user1, QuestionTest.Q1, "a2");
		Answers answers = new Answers();

		answers.addAll(Arrays.asList(answer1, answer2));

		// when
		DeleteHistories deleteHistories = answers.deleteAll(user1);

		// then
		assertAll(
			() -> assertThat(answer1.isDeleted()).isTrue(),
			() -> assertThat(answer2.isDeleted()).isTrue(),
			() -> assertThat(deleteHistories.getDeleteHistoryList().size()).isEqualTo(2)
		);
	}
}