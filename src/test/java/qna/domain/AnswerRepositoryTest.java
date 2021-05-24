package qna.domain;

import static java.time.LocalDateTime.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.QuestionTest.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class AnswerRepositoryTest {

	@Autowired
	private AnswerRepository answers;

	private Answer answer;

	@BeforeEach
	void setup() {
		answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
		answer = answers.save(answer);
	}

	@Test
	@DisplayName("id 로 삭제되지 않은 하나의 answer 를 가지고 올 수 있다.")
	void findByIdAndDeletedFalseTest() {
		Long answerId = answer.getId();

		Answer findAnswer = answers.findByIdAndDeletedFalse(answerId)
			.orElseThrow(IllegalArgumentException::new);

		assertAll(
			() -> assertThat(findAnswer.getId()).isEqualTo(answer.getId()),
			() -> assertThat(findAnswer.isDeleted()).isFalse()
		);
	}

	@Test
	@DisplayName("questionId 로 삭제되지 않은 answer 리스트를 가지고 올 수 있다.")
	void findByQuestionIdAndDeletedFalse() {
		Answer otherAnswer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents2");
		answers.save(otherAnswer);

		List<Answer> answersByQuestion = answers.findByQuestionIdAndDeletedFalse(Q1.getId());

		assertAll(
			() -> assertThat(answersByQuestion).hasSize(2),
			() -> assertThat(answersByQuestion).containsExactly(answer, otherAnswer)
		);
	}

	@Test
	@DisplayName("insert 되면 id 가 자동 생성 되어야 하고, createAt 에 생성일이 들어간다.")
	void insertTest() {
		assertAll(
			() -> assertThat(answer.getId()).isNotNull(),
			() -> assertThat(answer.isCreatedBefore(now())).isTrue()
		);
	}

	@Test
	@DisplayName("update 되면 updateAt 에 수정일이 들어간다.")
	void updateTest() {
		String contents = "update contents";
		answer.setContents(contents);

		assertAll(
			() -> assertThat(answer.getContents()).isEqualTo(contents),
			() -> assertThat(answer.isUpdatedBefore(now())).isTrue()
		);
	}
}