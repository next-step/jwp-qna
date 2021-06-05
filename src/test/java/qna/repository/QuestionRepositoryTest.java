package qna.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.CannotDeleteException;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
public class QuestionRepositoryTest {

	private final QuestionRepository questions;
	private final UserRepository users;

	private final User writer;
	private final User other;

	@Autowired
	public QuestionRepositoryTest(QuestionRepository questions, UserRepository users) {
		this.questions = questions;
		this.users = users;

		writer = users.save(UserTest.JAVAJIGI);
		other = users.save(UserTest.SANJIGI);
	}

	@Test
	@DisplayName("저장")
	void save() {
		Question expected = new Question("title1", "contents1");
		Question actual = questions.save(expected);
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
			() -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
		);
	}

	@Test
	@DisplayName("삭제 되지 않는 질문 조회")
	void findByDeletedFalse() {
		Question expected1 = new Question("title1", "contents1");
		Question expected2 = new Question("title2", "contents2");
		Question expected3 = new Question("title3", "contents3");

		expected3.setDeleted(true);

		questions.save(expected1);
		questions.save(expected2);
		questions.save(expected3);

		List<Question> actual = questions.findByDeletedFalse();
		assertThat(actual).contains(expected1, expected2);
	}

	@Test
	@DisplayName("특정 유저의 삭제 되지 않는 질문 조회")
	void findByIdAndDeletedFalse() {
		Question expected1 = new Question("title1", "contents1");
		Question expected2 = new Question("title2", "contents2");

		expected2.setDeleted(true);

		questions.save(expected1);
		questions.save(expected2);

		Question actual1 = questions.findByIdAndDeletedFalse(expected1.getId()).orElse(null);
		Question actual2 = questions.findByIdAndDeletedFalse(expected2.getId()).orElse(null);
		assertThat(actual1).isEqualTo(expected1);
		assertThat(actual2).isNull();
	}

	@Test
	@DisplayName("질문 삭제 성공 : 동일 작성자, 답변이 없는 경우")
	void deleteSuccessWithNoAnswer() {
		Question question = new Question(1L, "title1", "contents1").writeBy(writer);
		question = questions.save(question);
		question.delete(writer);
		Question result = questions.findByIdAndDeletedFalse(question.getId()).orElse(null);
		assertThat(result).isNull();
	}

	@Test
	@DisplayName("질문 삭제 성공 : 동일 작성자, 동일 작성자의 답변")
	void deleteSuccessWithAnswer() {
		Question question = new Question(1L, "title1", "contents1").writeBy(writer);
		Answer otherWriterAnswer =  new Answer(writer, question, "Answers Contents2");;

		question.addAnswer(otherWriterAnswer);
		question = questions.save(question);
		question.delete(writer);

		Question result = questions.findByIdAndDeletedFalse(question.getId()).orElse(null);
		assertThat(result).isNull();
	}

	@Test
	@DisplayName("질문 삭제 실패 : 동일 작성자가 아닌 경우")
	void deleteFailOfWriter() {
		Question question = new Question(1L, "title1", "contents1").writeBy(writer);
		Question savedQuestion = questions.save(question);
		assertThatThrownBy(() -> savedQuestion.delete(other))
			.isInstanceOf(CannotDeleteException.class)
			.hasMessage("질문을 삭제할 권한이 없습니다.");
	}

	@Test
	@DisplayName("질문 삭제 실패 : 동일 작성자가 아닌 경우")
	void deleteFailOfAnswer() {
		Question question = new Question(1L, "title1", "contents1").writeBy(writer);
		Answer otherWriterAnswer =  new Answer(other, question, "Answers Contents2");;

		question.addAnswer(otherWriterAnswer);
		Question savedQuestion = questions.save(question);

		assertThatThrownBy(() -> savedQuestion.delete(writer))
			.isInstanceOf(CannotDeleteException.class)
			.hasMessage("답변을 삭제할 권한이 없습니다.");
	}
}