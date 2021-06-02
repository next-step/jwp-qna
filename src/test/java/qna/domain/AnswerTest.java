package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {
	@Autowired
	private AnswerRepository answerRepository;

	public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
	public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

	@Test
	@DisplayName("jpa 데이터 인서트")
	void save() {
		Answer saveA1 = answerRepository.save(A1);
		Answer saveA2 = answerRepository.save(A2);

		assertThat(answerRepository.count()).isEqualTo(2);
	}

	@Test
	@DisplayName("jpa 데이터 인서트 후 동일성 확인")
	void is_same_object() {
		Answer saveA1 = answerRepository.save(A1);

		assertThat(saveA1).isEqualTo(answerRepository.findById(saveA1.getId()).get());
	}

	@Test
	@DisplayName("jpa 작성 메소드 사용(findByIdAndDeletedFalse)")
	void use_written_method_findByIdAndDeletedFalse() {
		Answer saveA1 = answerRepository.save(A1);
		A2.setDeleted(true);
		Answer saveA2 = answerRepository.save(A2);

		assertThat(saveA1).isEqualTo(answerRepository.findByIdAndDeletedFalse(saveA1.getId()).get());
		assertThat(answerRepository.findByIdAndDeletedFalse(saveA2.getId()).isPresent()).isFalse();
	}

	@Test
	@DisplayName("jpa 작성 메소드 사용(findByQuestionIdAndDeletedFalse)")
	void use_written_method_findByQuestionIdAndDeletedFalse() {
		Answer saveA1 = answerRepository.save(A1);
		A2.setDeleted(true);
		Answer saveA2 = answerRepository.save(A2);

		assertThat(saveA1).isEqualTo(answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId()).get(0));
		assertThat(answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId()).get(0)).isEqualTo(
			answerRepository.findById(saveA1.getId()).get());
	}
}
