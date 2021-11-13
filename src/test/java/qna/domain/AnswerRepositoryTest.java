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

	Question q1;
	Question q2;

	@BeforeEach
	public void setup() {
		q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
		q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
		q1 = questions.save(q1);
		q2 = questions.save(q2);
	}

	@Test
	@DisplayName("답변을 생성하면, ID가 존재해야 한다")
	void saveTest() {
		// given
		Answer answer = new Answer(UserTest.JAVAJIGI, q1, "Answers Contents1");

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
		Answer answer = new Answer(UserTest.JAVAJIGI, q1, "Answers Contents1");
		String updateContents = "업데이트 된 내용입니다";
		answers.save(answer);
		answer.setContents(updateContents);

		// when
		answers.save(answer);

		// then
		assertThat(answer.getContents()).isEqualTo(updateContents);
	}

	@Test
	@DisplayName("답변을 삭제하면, 답변을 찾을 수 없어야 한다")
	void deleteTest() {
		// given
		Answer answer = new Answer(UserTest.JAVAJIGI, q1, "Answers Contents1");
		answers.save(answer);
		answers.delete(answer);

		// when
		Optional<Answer> answerOptional = answers.findById(answer.getId());

		// then
		assertThat(answerOptional).isEmpty();
	}

	@Test
	@DisplayName("답변을 아이디로 찾을 수 있어야 한다")
	void findByQuestionIdAndDeletedFalseTest() {
		// given
		Answer answer1 = new Answer(UserTest.JAVAJIGI, q1, "Answers Contents1");
		Answer answer2 = new Answer(UserTest.SANJIGI, q2, "Answers Contents2");
		answer1 = answers.save(answer1);
		answer2 = answers.save(answer2);

		// when
		Optional<Answer> founded = answers.findByIdAndDeletedFalse(answer1.getId());

		// then
		assertThat(founded).isPresent();
	}

	@Test
	@DisplayName("답변을 질문으로 찾을 수 있어야 한다")
	void findByQuestionIdAndDeletedFalse() {

		// given
		Answer answer1 = new Answer(UserTest.JAVAJIGI, q1, "Answers Contents1");
		Answer answer2 = new Answer(UserTest.SANJIGI, q2, "Answers Contents2");

		answers.save(answer1);
		answers.save(answer2);

		// when
		List<Answer> founded = answers.findByQuestionAndDeletedFalse(q1);

		// then
		assertThat(founded.size()).isEqualTo(1);
	}
}
