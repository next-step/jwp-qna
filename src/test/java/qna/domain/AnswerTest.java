package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AnswerTest {
	public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
	public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
	@Autowired
	private AnswerRepository answerRepository;

	@Test
	@DisplayName("답변을 저장한다.")
	void save() {
		Answer actual = answerRepository.save(A1);
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getContents()).isEqualTo(A1.getContents())
		);
	}

	@Test
	@DisplayName("질문 ID의 삭제되지 않은 답변 목록을 가져온다.")
	void findByQuestionIdAndDeletedFalse() {
		final Answer answer1 = answerRepository.save(A1);
		final Answer answer2 = answerRepository.save(A2);
		final List<Answer> actual = answerRepository.findByQuestionIdAndDeletedFalse(
			QuestionTest.Q1.getId());

		assertThat(actual).contains(answer1, answer2);
	}

	@Test
	@DisplayName("삭제되지 않은 주어진 id의 질문을 가져온다.")
	void findByIdAndDeletedFalse() {
		final Answer answer = answerRepository.save(A1);
		final Answer actual = answerRepository.findByIdAndDeletedFalse(answer.getId()).get();

		assertThat(actual).isEqualTo(answer);
	}
}
