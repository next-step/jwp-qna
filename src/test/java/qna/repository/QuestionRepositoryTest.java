package qna.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.domain.Question;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
public class QuestionRepositoryTest {

	@Autowired
	private QuestionRepository questions;
	private final UserRepository users;

	private User testUser;

	@Autowired
	public QuestionRepositoryTest(QuestionRepository questions, UserRepository users) {
		this.questions = questions;
		this.users = users;

		this.testUser = users.save(UserTest.JAVAJIGI);
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
	@DisplayName("질문 삭제 : 성공")
	void deleteSuccess() {
		Question question = new Question(1L, "title1", "contents1").writeBy(testUser);
		question = questions.save(question);
		question.delete(testUser);
		Question result = questions.findByIdAndDeletedFalse(question.getId()).orElse(null);
		assertThat(result).isNull();
	}
}