package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnswerRepositoryTest {

	@Autowired
	AnswerRepository answers;

	@Autowired
	QuestionRepository questions;

	@Autowired
	UserRepository users;

	User user1;
	Question question1;
	Answer answer1;
	Answer answer2;

	@BeforeEach
	public void setup() {
		user1 = new User("javajigi", "password", "name", "javajigi@slipp.net");
		user1 = users.save(user1);

		question1 = new Question("title1", "contents1").writeBy(user1);
		question1 = questions.save(question1);

		answer1 = new Answer(user1, question1, "Answers Contents1");
		answer2 = new Answer(user1, question1, "Answers Contents2");
		answer1 = answers.save(answer1);
		answer2 = answers.save(answer2);
	}

	@Test
	@DisplayName("답변을 생성하면, ID가 존재해야 한다")
	void saveTest() {
		// given
		Answer answer = new Answer(user1, question1, "This is a new answer");

		// when
		Answer result = answers.save(answer);

		// then
		assertAll(
			() -> assertThat(result.getId()).isNotNull(),
			() -> assertThat(result).isEqualTo(answer),
			() -> assertThat(result).isSameAs(answer)
		);
	}

	@Test
	@DisplayName("답변을 업데이트하면, 업데이트한 내용이 확인되어야 한다")
	void updateTest() {
		// given
		String updateContents = "업데이트 된 내용입니다";
		answer1.setContents(updateContents);

		// when
		answer1 = answers.save(answer1);

		// then
		assertThat(answer1.getContents()).isEqualTo(updateContents);
	}

	@Test
	@DisplayName("답변을 삭제하면, 답변을 찾을 수 없어야 한다")
	void deleteTest() {
		// given
		answers.delete(answer1);

		// when
		Optional<Answer> answerOptional = answers.findById(answer1.getId());

		// then
		assertThat(answerOptional).isEmpty();
	}

	@Test
	@DisplayName("답변을 아이디로 찾을 수 있어야 한다")
	void findByQuestionIdAndDeletedFalseTest() {
		// when
		Optional<Answer> founded = answers.findByIdAndDeletedFalse(answer1.getId());

		// then
		assertAll(
			() -> assertThat(founded).isPresent(),
			() -> assertThat(founded).containsSame(answer1)
		);
	}

	@Test
	@DisplayName("답변을 질문으로 찾을 수 있어야 한다")
	void findByQuestionIdAndDeletedFalse() {
		// when
		List<Answer> founded = answers.findByQuestionAndDeletedFalse(question1);

		// then
		assertAll(
			() -> assertThat(founded.size()).isEqualTo(2),
			() -> assertThat(founded).contains(answer1, answer2)
		);
	}
}
