package qna.domain;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class QuestionRepositoryTest {

	@Autowired
	private QuestionRepository questions;

	@Test
	@DisplayName("id 로 삭제가 안된 하나의 Question 을 가지고 올 수 있다.")
	void findByIdAndDeletedFalseTest() {
		Question question = new Question("title", "contents");
		questions.save(question);

		Question findQuestion = questions.findByIdAndDeletedFalse(question.getId())
			.orElseThrow(IllegalArgumentException::new);

		assertAll(
			() -> assertThat(findQuestion.getId()).isNotNull(),
			() -> assertThat(findQuestion.getId()).isEqualTo(question.getId()),
			() -> assertThat(findQuestion.isDeleted()).isFalse()
		);
	}

	@Test
	@DisplayName("삭제 안된 Question 들을 모두 가져올 수 있다.")
	void findByDeletedFalseTest() {
		Question active = new Question("Active Question", false);
		Question deleted = new Question("Deleted Question", true);
		questions.saveAll(asList(active, deleted));

		List<Question> activeQuestions = questions.findByDeletedFalse();

		assertThat(activeQuestions).containsExactly(active);
	}

}